package me.ttt.takatan.account;

import android.annotation.SuppressLint;
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
import me.ttt.takatan.account.db.model.AssetModel;

public class AssetActivity extends AppCompatActivity {
    private final static String TAG = AssetActivity.class.getSimpleName();

    private AssetModel assetModel;
    private PieChart pieChart;
    private PieDataSet pieDataSet;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        assetModel = new AssetModel(db);

        // 円グラフの初期設定
        pieChart = (PieChart) findViewById(R.id.pieChartAsset);
        pieChart.setDrawHoleEnabled(false);
        pieDataSet = new PieDataSet(null, getString(R.string.asset));
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value);
            }
        });

        // 資産リストの初期設定
        GridView assetList = (GridView) findViewById(R.id.asset_list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        assetList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        String[] labels = {"Cash",
                getString(R.string.trade_bankaccount),
                getString(R.string.trade_iccard),
                getString(R.string.trade_qrpay)};

        // 円グラフの再設定
        assetModel.setPieDate(pieChart, pieDataSet, labels);

        // リストの再設定
        adapter.clear();
        assetModel.setSumList(adapter, labels);
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