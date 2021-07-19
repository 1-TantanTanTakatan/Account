package me.ttt.takatan.account.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.lang.ref.WeakReference;
import java.util.UUID;

import me.ttt.takatan.account.Bluetooth.message.TradeMessage;
import me.ttt.takatan.account.R;
import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.model.TradeModel;

public class ConnectActivity extends AppCompatActivity {
    private final static String TAG = ConnectActivity.class.getSimpleName();
    public final static UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final String STATE_EXTRA = "state";
    public static final String STATE_DATE = "date";
    public static final String STATE_NAME = "name";
    public static final String STATE_METHOD = "method";
    public static final String STATE_PRICE = "price";

    private final int SEND = 1231;
    private final int RECEIVE = 1232;

    private int state;
    private TradeModel tradeModel;
    private Agent agent;
    private BluetoothAdapter adapter;
    private BluetoothInitializer initializer;
    private ProgressBar progress;
    private TradeMessage message;
    private String name;
    private String method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        tradeModel = new TradeModel(db);

        progress = findViewById(R.id.main_progress);

        initializer = new BluetoothInitializer(this) {
            @Override
            protected void onReady(BluetoothAdapter adapter) {
                ConnectActivity.this.adapter = adapter;
            }
        };
        initializer.initialize();

        state = getIntent().getIntExtra(STATE_EXTRA, 0);
        switch (state) {
            case SEND:
                String date = getIntent().getStringExtra(STATE_DATE);
                name = getIntent().getStringExtra(STATE_NAME);
                method = getIntent().getStringExtra(STATE_METHOD);
                String price = getIntent().getStringExtra(STATE_PRICE);
                long time = System.currentTimeMillis();
                message = new TradeMessage(time, date, Integer.parseInt(price), null);
                agent = new ClientAgent(this, new CommHandler(this));
                ((ClientAgent) agent).connect();
                break;
            case RECEIVE:
                findViewById(R.id.main_button).setEnabled(false);
                name = getIntent().getStringExtra(STATE_NAME);
                method = getIntent().getStringExtra(STATE_METHOD);
                agent = new ServerAgent(this, new CommHandler(this));
                ((ServerAgent) agent).start(adapter);
                break;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        Log.d(TAG, "onActivityResult: reqCode=" + reqCode + " resCode=" + resCode);
        initializer.onActivityResult(reqCode, resCode, data); // delegate
        if (agent != null) {
            agent.onActivityResult(reqCode, resCode, data); // delegate
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private static class CommHandler extends Handler {
        WeakReference<ConnectActivity> ref;

        CommHandler(ConnectActivity activity) {
            super();
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage");
            ConnectActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case Agent.MSG_STARTED:
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    break;
                case Agent.MSG_FINISHED:
                    Toast.makeText(activity, R.string.toast_connection_closed, Toast.LENGTH_SHORT).show();
                    activity.finish();
                    break;
                case Agent.MSG_RECEIVED:
                    activity.getMessage((TradeMessage) msg.obj);
                    break;
            }
        }
    }

    public void setProgress(boolean isConnecting) {
        progress.setIndeterminate(isConnecting);
    }

    public void onClickSendButton(View v) {
        Log.d(TAG, "onClickSendButton");
        long time = System.currentTimeMillis();
        message = new TradeMessage(time, message.date, message.price, adapter.getName());
        agent.send(message);
        tradeModel.insert(message.date, name, method, getString(R.string.lend), message.price);
    }

    private void getMessage(TradeMessage message) {
        tradeModel.insert(message.date, method, name, getString(R.string.borrow), message.price);
        finish();
    }
}