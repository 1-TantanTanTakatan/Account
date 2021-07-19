package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.Cost;

@Dao
public interface CostDao {
    @Query("SELECT * FROM Cost")
    List<Cost> getAll();

    @Query("SELECT * FROM Cost WHERE id = (:id)")
    Cost findById(int id);

    @Query("SELECT * FROM Cost WHERE Name = (:name)")
    Cost findByName(String name);

    @Insert
    void insert(Cost cost);

    @Update
    void update(Cost cost);
}