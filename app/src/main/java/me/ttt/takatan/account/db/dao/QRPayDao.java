package me.ttt.takatan.account.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.ttt.takatan.account.db.entity.QRPay;

@Dao
public interface QRPayDao {
    @Query("SELECT * FROM QRPay")
    List<QRPay> getAll();

    @Query("SELECT * FROM QRPay WHERE id = (:id)")
    QRPay findById(int id);

    @Query("SELECT * FROM QRPay WHERE Name = (:name)")
    QRPay findByName(String name);

    @Insert
    void insert(QRPay qrPay);

    @Update
    void update(QRPay qrPay);
}
