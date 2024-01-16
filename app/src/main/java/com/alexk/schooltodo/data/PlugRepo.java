package com.alexk.schooltodo.data;

import com.alexk.schooltodo.data.api.PlugState;
import com.alexk.schooltodo.data.db.PlugEntity;

import java.util.ArrayList;

import retrofit2.Call;

public interface PlugRepo {
    ArrayList<PlugEntity> getAllPlugs();
    void insertPlug(PlugEntity entity);
    void updatePlug(PlugEntity entity);
    void deletePlug(PlugEntity entity);
    PlugEntity getPlugById(int id);

    Call<PlugState> getPlugInfo(String url);
    PlugEntity getPlugByName(String plugName);
}
