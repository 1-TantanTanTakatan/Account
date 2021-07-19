package me.ttt.takatan.account.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

import me.ttt.takatan.account.db.DateConverter;

@Entity
public class Trade {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo
    public Date date;

    @ColumnInfo
    public String credit;

    @ColumnInfo
    public String debit;

    @ColumnInfo
    public String description;

    @ColumnInfo
    public int price;

    public Trade(Date date, String credit, String debit, String description, int price) {
        this.date = date;
        this.credit = credit;
        this.debit = debit;
        this.description = description;
        this.price = price;
    }
}