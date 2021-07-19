package me.ttt.takatan.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.model.CostModel;
import me.ttt.takatan.account.db.model.TradeModel;

public class PLActivity extends AppCompatActivity {
    private final static String TAG = PLActivity.class.getSimpleName();

    private CostModel costModel;
    private PieChart pieChart;
    private PieDataSet pieDataSet;
    private ArrayAdapter<String> sumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pl);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        costModel = new CostModel(db);

        // 円グラフの初期設定
        pieChart = (PieChart) findViewById(R.id.pieChartCost);
        pieChart.setDrawHoleEnabled(false);
        pieDataSet = new PieDataSet(null, getString(R.string.category));
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value);
            }
        });

        // 合計リストの初期設定
        GridView sumList = (GridView) findViewById(R.id.cost_sum_list);
        sumAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        sumList.setAdapter(sumAdapter);

        findViewById(R.id.pl_trade_list).setOnClickListener(v -> {
            // 資産情報画面へ
            Log.d(TAG, "onClick - TradeList");
            Intent intent = new Intent(PLActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        // 円グラフの再設定
        costModel.setPieDate(pieChart, pieDataSet);

        // リストの再設定
        sumAdapter.clear();
        costModel.setSumList(sumAdapter);
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