package me.ttt.takatan.account.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CreditCard {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "name")
    public String name;

    public CreditCard(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
