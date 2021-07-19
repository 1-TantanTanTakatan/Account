package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.CostDao;
import me.ttt.takatan.account.db.entity.Cost;

public class CostModel extends CategoryModel {
    private CostDao dao;

    public CostModel(AppDatabase db) {
        super(db);
        dao = db.costDao();
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void getAll(ArrayAdapter<String> adapter) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<Cost> costs = dao.getAll();
                for(Cost cost : costs) {
                    list.add(cost.name);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<String> result){
                adapter.addAll(result);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void setPieDate(PieChart pieChart, PieDataSet pieDataSet) {
        new AsyncTask<PieDataSet, Void, PieData>() {
            @Override
            protected PieData doInBackground(PieDataSet... pieDataSets) {
                ArrayList<PieEntry> entryList = new ArrayList<>();

                List<Cost> costs = dao.getAll();
                TradeModel tradeModel = new TradeModel(db);
                for(Cost cost : costs) {
                    entryList.add(new PieEntry(tradeModel.getBalance(cost.name), cost.name));
                }

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
    public void setSumList(ArrayAdapter<String> adapter) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<Cost> costs = dao.getAll();
                TradeModel tradeModel = new TradeModel(db);
                for(Cost cost : costs) {
                    list.add(cost.name);
                    list.add(String.format("%,d", tradeModel.getBalance(cost.name)));
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<String> result){
                adapter.addAll(result);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    protected int getBalanceSum() {
        int sum = 0;
        List<Cost> costs = dao.getAll();
        for(Cost cost : costs) {
            sum += getBalance(cost.name);
        }
        return sum;
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void insert(String name, Handler handler) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... names) {
                String name = names[0];
                if(!new CategoryModel(db).duplicateAll(name)) {
                    Cost cost = new Cost(0, name);
                    dao.insert(cost);
                } else {
                    handler.sendMessage(handler.obtainMessage(0));
                }
                return null;
            }
        }.execute(name);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void update(String before, String after, Handler handler) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... names) {
                String before = names[0];
                String after = names[1];
                if(!new CategoryModel(db).duplicateAll(after)) {
                    Cost cost = new Cost(dao.findByName(before).id, after);
                    dao.update(cost);
                    new TradeModel(db).renameCategory(before, after);
                } else {
                    handler.sendMessage(handler.obtainMessage(0));
                }
                return null;
            }
        }.execute(before, after);
    }

    protected boolean duplicate(String name) {
        if(dao.findByName(name)!=null) {
            return true;
        } else {
            return false;
        }
    }
}
