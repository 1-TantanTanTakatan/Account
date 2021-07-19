package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.CreditCard;

@Dao
public interface CreditCardDao {
    @Query("SELECT * FROM CreditCard")
    List<CreditCard> getAll();

    @Query("SELECT * FROM CreditCard WHERE id = (:id)")
    CreditCard findById(int id);

    @Query("SELECT * FROM CreditCard WHERE Name = (:name)")
    CreditCard findByName(String name);

    @Insert
    void insert(CreditCard creditCard);

    @Update
    void update(CreditCard creditCard);
}