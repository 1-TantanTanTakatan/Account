package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.R;
import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.BankAccountDao;
import me.ttt.takatan.account.db.dao.CreditCardDao;
import me.ttt.takatan.account.db.dao.ICCardDao;
import me.ttt.takatan.account.db.dao.QRPayDao;
import me.ttt.takatan.account.db.entity.BankAccount;
import me.ttt.takatan.account.db.entity.Cost;

public class AssetModel {
    AppDatabase db;
    BankAccountModel bankAccountModel;
    CreditCardModel creditCardModel;
    ICCardModel icCardModel;
    QRPayModel qrPayModel;
    TradeModel tradeModel;

    public AssetModel(AppDatabase db) {
        this.db = db;
        bankAccountModel = new BankAccountModel(db);
        creditCardModel = new CreditCardModel(db);
        icCardModel = new ICCardModel(db);
        qrPayModel = new QRPayModel(db);
        tradeModel = new TradeModel(db);
    }

    @SuppressLint("StaticFieldLeak")
    public void getAll(ArrayAdapter<String> adapter) {
        adapter.add("Cash");
        bankAccountModel.getAll(adapter);
        creditCardModel.getAll(adapter);
        icCardModel.getAll(adapter);
        qrPayModel.getAll(adapter);
    }


    @SuppressLint("StaticFieldLeak")
    public void setPieDate(PieChart pieChart, PieDataSet pieDataSet, String[] labels) {
        new AsyncTask<PieDataSet, Void, PieData>() {
            @Override
            protected PieData doInBackground(PieDataSet... pieDataSets) {
                ArrayList<PieEntry> entryList = new ArrayList<>();
                entryList.add(new PieEntry(tradeModel.getBalance(labels[0]), labels[0]));
                entryList.add(new PieEntry(bankAccountModel.getBalanceSum(), labels[1]));
                entryList.add(new PieEntry(icCardModel.getBalanceSum(), labels[2]));
                entryList.add(new PieEntry(qrPayModel.getBalanceSum(), labels[3]));

                pieDataSet.setValues(entryList);
                return new PieData(pieDataSet);
            }

            @Override
            protected void onPostExecute(PieData pieData){
                pieChart.setData(pieData);
                // 更新
                pieChart.invalidate();
            }
        }.execute(pieDataSet);
    }

    @SuppressLint("StaticFieldLeak")
    public void setSumList(ArrayAdapter<String> adapter, String[] labels) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(labels[0]);
                list.add(String.format("%,d", tradeModel.getBalance(labels[0])));
                list.add(labels[1]);
                list.add(String.format("%,d", bankAccountModel.getBalanceSum()));
                list.add(labels[2]);
                list.add(String.format("%,d", icCardModel.getBalanceSum()));
                list.add(labels[3]);
                list.add(String.format("%,d", qrPayModel.getBalanceSum()));
                return list;
            }

            @Override
            protected void onPostExecute(List<String> result){
                adapter.addAll(result);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
