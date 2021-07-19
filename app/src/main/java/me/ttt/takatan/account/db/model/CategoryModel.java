package me.ttt.takatan.account.db.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.ttt.takatan.account.db.AppDatabase;
import me.ttt.takatan.account.db.entity.Counterpart;

public class CategoryModel {
    protected final AppDatabase db;

    public CategoryModel(AppDatabase db) {
        this.db = db;
    }

    public void getAll(ArrayAdapter<String> adapter){
        adapter.add("Cash");
        new BankAccountModel(db).getAll(adapter);
        new ICCardModel(db).getAll(adapter);
        new QRPayModel(db).getAll(adapter);
        new CreditCardModel(db).getAll(adapter);
        new CostModel(db).getAll(adapter);
    };

    protected int getBalance(String name) {
        return new TradeModel(db).getBalance(name);
    }

    public void insert(String name, Handler handler){}

    public void update(String before, String after, Handler handler){}

    protected boolean duplicateAll(String name) {
        boolean result = new BankAccountModel(db).duplicate(name) ||
                new CreditCardModel(db).duplicate(name) ||
                new ICCardModel(db).duplicate(name) ||
                new QRPayModel(db).duplicate(name) ||
                new CostModel(db).duplicate(name) ||
                new CounterpartModel(db).duplicate(name);
        return result;
    }
}
