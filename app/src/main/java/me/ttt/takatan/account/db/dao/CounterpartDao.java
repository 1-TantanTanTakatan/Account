package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.Counterpart;

@Dao
public interface CounterpartDao {
    @Query("SELECT * FROM Counterpart")
    List<Counterpart> getAll();

    @Query("SELECT * FROM Counterpart WHERE id = (:id)")
    Counterpart findById(int id);

    @Query("SELECT * FROM Counterpart WHERE Name = (:name)")
    Counterpart findByName(String name);

    @Insert
    void insert(Counterpart counterpart);

    @Update
    void update(Counterpart counterpart);

    @Delete
    void delete(Counterpart counterpart);
}