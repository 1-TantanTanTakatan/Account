package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.CounterpartDao;
import me.ttt.takatan.account.db.entity.Cost;
import me.ttt.takatan.account.db.entity.Counterpart;
import me.ttt.takatan.account.db.entity.CreditCard;

public class CounterpartModel extends CategoryModel {
    private CounterpartDao dao;

    public CounterpartModel(AppDatabase db) {
        super(db);
        dao = db.counterpartDao();
    }

    @SuppressLint("StaticFieldLeak")
    public void setSumList(ArrayAdapter<String> adapter) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<Counterpart> counterparts = dao.getAll();
                TradeModel tradeModel = new TradeModel(db);
                for(Counterpart counterpart : counterparts) {
                    if(tradeModel.getBalance(counterpart.name)==0) {
                        dao.delete(counterpart);
                    } else {
                        list.add(counterpart.name);
                        list.add(String.format("%,d", tradeModel.getBalance(counterpart.name)));
                    }
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

    @Override
    @SuppressLint("StaticFieldLeak")
    public void insert(String name, Handler handler) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... names) {
                String name = names[0];
                if(!new CategoryModel(db).duplicateAll(name)) {
                    Counterpart counterpart = new Counterpart(0, name);
                    dao.insert(counterpart);
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
                    Counterpart counterpart = new Counterpart(dao.findByName(before).id, after);
                    dao.update(counterpart);
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
