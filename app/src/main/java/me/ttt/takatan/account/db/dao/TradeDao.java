package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.BankAccount;
import me.ttt.takatan.account.db.entity.Cost;
import me.ttt.takatan.account.db.entity.Trade;

@Dao
public interface TradeDao {
    @Query("SELECT * FROM Trade ORDER BY Date DESC")
    List<Trade> getAll();

    @Query("SELECT * FROM Trade WHERE id = (:id)")
    Trade findById(int id);

    @Query("SELECT SUM(price) FROM Trade WHERE credit = (:name)")
    int sumCredit(String name);

    @Query("SELECT SUM(price) FROM Trade WHERE debit = (:name)")
    int sumDebit(String name);

    @Insert
    void insert(Trade trade);

    @Update
    void update(Trade trade);

    @Query("UPDATE Trade SET credit = (:after) WHERE credit = (:before)")
    void renameCredit(String before, String after);

    @Query("UPDATE Trade SET debit = (:after) WHERE debit = (:before)")
    void renameDebit(String before, String after);

    @Delete
    void delete(Trade trade);
}