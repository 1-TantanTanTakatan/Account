package me.ttt.takatan.account.db;

import androidx.room.TypeConverter;

import java.sql.Date;

public class DateConverter {
    @TypeConverter
    public Date fromDate(Long date) {
        if(date==null){
            return null;
        } else {
            return new Date(date);
        }
    }

    @TypeConverter
    public Long dateToLong(Date date) {
        return date.getTime();
    }
}