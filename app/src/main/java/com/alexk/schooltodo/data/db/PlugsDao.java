package com.alexk.schooltodo.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PlugsDao {
    @Insert
    void addPlug(PlugEntity plug);

    @Update
    void updatePlug(PlugEntity plug);

    @Delete
    void deletePlug(PlugEntity plug);

    @Query("SELECT * FROM `plugs`")
    List<PlugEntity> getAllPlugs();


    @Query("SELECT * FROM `plugs` WHERE plugId = :plugID")
    PlugEntity getPlugById(int plugID);

    @Query("SELECT * FROM `plugs` WHERE plugName LIKE :plugName")
    PlugEntity getPlugByName(String plugName);

}
