package me.ttt.takatan.account;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.model.*;

public class EditActivity extends AppCompatActivity {
    private final static String TAG = EditActivity.class.getSimpleName();

    public static final String STATE_EXTRA = "state";

    private final int BANKACCOUNT = 1231;
    private final int ICCARD = 1232;
    private final int QRPAY = 1233;
    private final int CREDITCARD = 1234;
    private final int COST = 1235;

    private int state;
    private CategoryModel model;
    private EditText input;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        TextView title = findViewById(R.id.edit_title);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        // カテゴリー編集画面の状態分け
        state = getIntent().getIntExtra(STATE_EXTRA, 0);
        switch (state) {
            case BANKACCOUNT:
                Log.d(TAG, "state - BankAccount");
                title.setText(R.string.menu_main_bankaccount);
                model = new BankAccountModel(db);
                break;
            case ICCARD:
                Log.d(TAG, "state - ICCard");
                title.setText(R.string.menu_main_iccard);
                model = new ICCardModel(db);
                break;
            case QRPAY:
                Log.d(TAG, "state - QRPay");
                title.setText(R.string.menu_main_qrpay);
                model = new QRPayModel(db);
                break;
            case CREDITCARD:
                Log.d(TAG, "state - CreditCard");
                title.setText(R.string.menu_main_creditcard);
                model = new CreditCardModel(db);
                break;
            case COST:
                Log.d(TAG, "state - Cost");
                title.setText(R.string.menu_main_cost);
                model = new CostModel(db);
                break;
            default:
        }

        // テキスト入力と追加ボタンの処理
        input = findViewById(R.id.input_category);
        Button button = findViewById(R.id.edit_button);
        button.setOnClickListener(v -> {
            Log.d(TAG, "onClick - insert");
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                model.insert(name, new ErrorHandler(this));
                adapter.clear();
                model.getAll(adapter);
                input.getEditableText().clear();
            }
        });

        // リストの表示と編集画面
        ListView list = findViewById(R.id.list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, pos, id) -> {
            Log.d(TAG, "onClick - item");
            String before = (String) parent.getItemAtPosition(pos);
            final EditText editText = new EditText(this);
            editText.getEditableText().append(before);
            new AlertDialog.Builder(this)
                    .setTitle(title.getText())
                    .setMessage(R.string.edit_rename)
                    .setView(editText)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String after = editText.getText().toString().trim();
                        model.update(before, after, new ErrorHandler(this));
                        adapter.clear();
                        model.getAll(adapter);
                    })
                    .show();
        });
    }

    void sendError() {
        Toast.makeText(this, getString(R.string.error_duplicate), Toast.LENGTH_SHORT).show();
    }

    class ErrorHandler extends Handler {
        private final WeakReference<EditActivity> activityRef;

        ErrorHandler(EditActivity activity) {
            super(Looper.getMainLooper());
            activityRef = new WeakReference<>(activity);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            EditActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            sendError();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // リストの再設定
        adapter.clear();
        model.getAll(adapter);
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
}
