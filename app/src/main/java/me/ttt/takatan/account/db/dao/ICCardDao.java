package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.ICCard;

@Dao
public interface ICCardDao {
    @Query("SELECT * FROM ICCard")
    List<ICCard> getAll();

    @Query("SELECT * FROM ICCard WHERE id = (:id)")
    ICCard findById(int id);

    @Query("SELECT * FROM ICCard WHERE Name = (:name)")
    ICCard findByName(String name);

    @Insert
    void insert(ICCard icCard);

    @Update
    void update(ICCard icCard);
}