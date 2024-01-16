package com.alexk.schooltodo.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plugs")
public class PlugEntity {
    @PrimaryKey(autoGenerate = true)
    public int plugId = 0;

    @ColumnInfo(name = "plugName")
    public String plugName;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "added")
    public Long added = System.currentTimeMillis();



}