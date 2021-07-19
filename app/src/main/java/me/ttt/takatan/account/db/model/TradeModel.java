package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.TradeDao;
import me.ttt.takatan.account.db.entity.Trade;

public class TradeModel {
    public TradeDao dao;

    public TradeModel(AppDatabase db) {
        this.dao = db.tradeDao();
    }

    protected int getBalance(String name) {
        return dao.sumCredit(name) - dao.sumDebit(name);
    }

    @SuppressLint("StaticFieldLeak")
    public void setTradeList(ArrayAdapter<String> adapter, ArrayList<Integer> idList) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<Trade> trades = dao.getAll();
                for(Trade trade : trades) {
                    list.add(trade.date.toString());
                    list.add("");
                    list.add(trade.description);
                    list.add(String.format("%,d", trade.price));
                    idList.add(trade.id);
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
    public void insert(String date, String credit, String debit, String description, int price) {
        Trade trade = new Trade(Date.valueOf(date), credit, debit, description, price);
        new AsyncTask<Trade, Void, Void>() {
            @Override
            protected Void doInBackground(Trade... trades) {
                Trade trade = trades[0];
                dao.insert(trade);
                return null;
            }
        }.execute(trade);
    }

    @SuppressLint("StaticFieldLeak")
    public void update(int id, String date, String credit, String debit, String description, int price) {
        Trade trade = new Trade(Date.valueOf(date), credit, debit, description, price);
        new AsyncTask<Trade, Void, Void>() {
            @Override
            protected Void doInBackground(Trade... trades) {
                Trade trade = trades[0];
                dao.update(trade);
                return null;
            }
        }.execute(trade);
    }

    @SuppressLint("StaticFieldLeak")
    public void renameCategory(String before, String after) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... string) {
                String before = string[0];
                String after = string[1];
                dao.renameCredit(before, after);
                dao.renameDebit(before, after);
                return null;
            }
        }.execute(before, after);
    }
}
