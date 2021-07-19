package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.BankAccount;

@Dao
public interface BankAccountDao {
    @Query("SELECT * FROM BankAccount")
    List<BankAccount> getAll();

    @Query("SELECT * FROM BankAccount WHERE id = (:id)")
    BankAccount findById(int id);

    @Query("SELECT * FROM BankAccount WHERE Name = (:name)")
    BankAccount findByName(String name);

    @Insert
    void insert(BankAccount bankAccount);

    @Update
    void update(BankAccount bankAccount);
}