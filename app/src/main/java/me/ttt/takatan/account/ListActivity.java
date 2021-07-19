package me.ttt.takatan.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.model.TradeModel;

public class ListActivity extends AppCompatActivity {
    private final static String TAG = PLActivity.class.getSimpleName();

    private TradeModel tradeModel;
    private ArrayAdapter<String> costAdapter;
    private ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        tradeModel = new TradeModel(db);

        // リストの初期設定
        GridView costList = (GridView) findViewById(R.id.cost_list);
        costAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        costList.setAdapter(costAdapter);
        idList = new ArrayList<>();
        costList.setOnItemClickListener((parent, view, pos, id) -> {
            Log.d(TAG, "onClick - item");

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // リストの再設定
        costAdapter.clear();
        idList.clear();
        tradeModel.setTradeList(costAdapter, idList);
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