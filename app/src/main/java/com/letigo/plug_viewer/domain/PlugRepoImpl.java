package com.letigo.plug_viewer.domain;

import com.letigo.plug_viewer.PlugApp;
import com.letigo.plug_viewer.data.PlugRepo;
import com.letigo.plug_viewer.data.api.PlugState;
import com.letigo.plug_viewer.data.db.PlugEntity;
import com.letigo.plug_viewer.data.db.PlugsDao;

import java.util.ArrayList;

import retrofit2.Call;

public class PlugRepoImpl implements PlugRepo {
    private PlugsDao dao = PlugApp
            .getInstance()
            .getDatabase()
            .plugsDao();
    @Override
    public ArrayList<PlugEntity> getAllPlugs() {
        return (ArrayList<PlugEntity>) dao.getAllPlugs();
    }

    @Override
    public void insertPlug(PlugEntity entity) {
        dao.addPlug(entity);
    }

    @Override
    public void updatePlug(PlugEntity entity) {
        dao.updatePlug(entity);
    }

    @Override
    public void deletePlug(PlugEntity entity) {
        dao.deletePlug(entity);
    }

    @Override
    public PlugEntity getPlugById(int id) {
        return dao.getPlugById(id);
    }

    @Override
    public Call<PlugState> getPlugInfo(String url) {
        return PlugApp.getInstance().getApi(url).getPlugInfo();
    }

    @Override
    public PlugEntity getPlugByName(String plugName) {
        return dao.getPlugByName(plugName);
    }
}
