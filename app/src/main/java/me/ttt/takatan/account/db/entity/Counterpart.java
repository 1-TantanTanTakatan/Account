package me.ttt.takatan.account.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Counterpart {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "name")
    public String name;

    public Counterpart(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
