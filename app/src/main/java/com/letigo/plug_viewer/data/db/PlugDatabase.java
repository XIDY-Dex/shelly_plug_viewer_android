package com.letigo.plug_viewer.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PlugEntity.class}, version = 1)
public abstract class PlugDatabase extends RoomDatabase {
    public abstract PlugsDao plugsDao();
}
