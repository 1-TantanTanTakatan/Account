package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.ICCardDao;
import me.ttt.takatan.account.db.entity.ICCard;

public class ICCardModel extends CategoryModel {
    private ICCardDao dao;

    public ICCardModel(AppDatabase db) {
        super(db);
        dao = db.icCardDao();
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void getAll(ArrayAdapter<String> adapter) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<ICCard> icCards = dao.getAll();
                for(ICCard icCard : icCards) {
                    list.add(icCard.name);
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
        List<ICCard> icCards = dao.getAll();
        for(ICCard icCard : icCards) {
            sum += getBalance(icCard.name);
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
                    ICCard icCard = new ICCard(0, name);
                    dao.insert(icCard);
                }
                return null;
            }
        }.execute(name);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void update(String before, String after, Handler handler){
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... names) {
                String before = names[0];
                String after = names[1];
                if(!new CategoryModel(db).duplicateAll(after)) {
                    ICCard icCard = new ICCard(dao.findByName(before).id, after);
                    dao.update(icCard);
                    new TradeModel(db).renameCategory(before, after);
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
