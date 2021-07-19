package me.ttt.takatan.account.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import me.ttt.takatan.account.db.dao.*;
import me.ttt.takatan.account.db.entity.*;

@Database(entities = {BankAccount.class, Cost.class, CreditCard.class, ICCard.class, QRPay.class, Trade.class, Counterpart.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BankAccountDao bankAccountDao();
    public abstract CostDao costDao();
    public abstract CreditCardDao creditCardDao();
    public abstract ICCardDao icCardDao();
    public abstract QRPayDao qrPayDao();
    public abstract TradeDao tradeDao();
    public abstract CounterpartDao counterpartDao();
}