package com.alexk.schooltodo.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.alexk.schooltodo.data.db.PlugEntity;

import java.util.ArrayList;

public class PlugListViewModel extends ViewModel {
    private PlugRepoImpl repo = new PlugRepoImpl();
    private final MutableLiveData<ArrayList<PlugEntity>> _plugs = new MutableLiveData<ArrayList<PlugEntity>>(new ArrayList<PlugEntity>());
    public LiveData<ArrayList<PlugEntity>> plugs() {
        return _plugs;
    }

    public PlugListViewModel() {
        super();
        getPlugs();
    }

    public void getPlugs() {
        _plugs.postValue(repo.getAllPlugs());
    }
    public void addPlugs(PlugEntity entity) {
        repo.insertPlug(entity);
        getPlugs();
    }
    public void deletePlug(PlugEntity plug) {
        repo.deletePlug(plug);
        _plugs.postValue(repo.getAllPlugs());
    }

    public void updatePlug(PlugEntity entity) {
        repo.updatePlug(entity);
        _plugs.postValue(repo.getAllPlugs());
    }
}
