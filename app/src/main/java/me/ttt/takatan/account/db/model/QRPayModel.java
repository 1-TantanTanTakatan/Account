package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.dao.QRPayDao;
import me.ttt.takatan.account.db.entity.QRPay;

public class QRPayModel extends CategoryModel {
    private QRPayDao dao;

    public QRPayModel(AppDatabase db) {
        super(db);
        dao = db.qrPayDao();
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void getAll(ArrayAdapter<String> adapter) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                ArrayList<String> list = new ArrayList<String>();
                List<QRPay> qrPays = dao.getAll();
                for(QRPay qrPay : qrPays) {
                    list.add(qrPay.name);
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
        List<QRPay> qrPays = dao.getAll();
        for(QRPay qrPay : qrPays) {
            sum += getBalance(qrPay.name);
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
                    QRPay qrPay = new QRPay(0, name);
                    dao.insert(qrPay);
                } else {
                    handler.sendMessage(handler.obtainMessage(0));
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
                    QRPay qrPay = new QRPay(dao.findByName(before).id, after);
                    dao.update(qrPay);
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
