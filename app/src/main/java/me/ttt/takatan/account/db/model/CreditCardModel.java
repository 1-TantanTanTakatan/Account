package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.CreditCardDao;
import me.ttt.takatan.account.db.entity.CreditCard;

public class CreditCardModel extends CategoryModel {
    private CreditCardDao dao;

    public CreditCardModel(AppDatabase db) {
        super(db);
        dao = db.creditCardDao();
    }

    @SuppressLint("StaticFieldLeak")
    public void getAll(ArrayAdapter<String> adapter) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<CreditCard> creditCards = dao.getAll();
                for(CreditCard creditCard : creditCards) {
                    list.add(creditCard.name);
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
        List<CreditCard> creditCards = dao.getAll();
        for(CreditCard creditCard : creditCards) {
            sum += getBalance(creditCard.name);
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
                    CreditCard creditCard = new CreditCard(0, name);
                    dao.insert(creditCard);
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
                    CreditCard creditCard = new CreditCard(dao.findByName(before).id, after);
                    dao.update(creditCard);
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