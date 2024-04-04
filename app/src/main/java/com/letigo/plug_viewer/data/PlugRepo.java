package com.letigo.plug_viewer.data;

import com.letigo.plug_viewer.data.api.PlugState;
import com.letigo.plug_viewer.data.db.PlugEntity;

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
