package com.alexk.schooltodo.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.alexk.schooltodo.data.api.PlugState;
import com.alexk.schooltodo.data.db.PlugEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlugViewModel extends ViewModel {
    private PlugRepoImpl repo = new PlugRepoImpl();

    private MutableLiveData<PlugState> _plugInfo = new MutableLiveData<PlugState>(null);
    private MutableLiveData<Boolean> _errorOccured = new MutableLiveData<Boolean>(false);
    private MutableLiveData<Boolean> _loading = new MutableLiveData<Boolean>(false);
    public LiveData<Boolean> errorStatus() {
        return _errorOccured;
    }
    public LiveData<PlugState> plugInfo() {
        return _plugInfo;
    }
    public LiveData<Boolean> isLoading() {
        return _loading;
    }

    public void getPlugInfo(int plugId) {
        _loading.postValue(true);
        PlugEntity plug = repo.getPlugById(plugId);
        repo.getPlugInfo(plug.address).enqueue(new Callback<PlugState>() {
            @Override
            public void onResponse(Call<PlugState> call, Response<PlugState> response) {
                _loading.postValue(false);
                _errorOccured.postValue(false);
                _plugInfo.postValue(response.body());
            }

            @Override
            public void onFailure(Call<PlugState> call, Throwable t) {
                _loading.postValue(false);
                Log.d("INTERNET_ERROR", t.getMessage());
                _errorOccured.postValue(true);
            }
        });
    }

    public String getPlugName(int plugId) {
        return repo.getPlugById(plugId).plugName;
    }

    public void pollPlug(int plugId) {
        PlugEntity plug = repo.getPlugById(plugId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        repo.getPlugInfo(plug.address).enqueue(new Callback<PlugState>() {
                            @Override
                            public void onResponse(Call<PlugState> call, Response<PlugState> response) {
                                _loading.postValue(false);
                                _errorOccured.postValue(false);
                                _plugInfo.postValue(response.body());
                            }

                            @Override
                            public void onFailure(Call<PlugState> call, Throwable t) {
                                _loading.postValue(false);
                                _errorOccured.postValue(false);
                            }
                        });
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.d("INTERNET_ERROR", e.getMessage());
                        _errorOccured.postValue(true);
                    }
                }
            }
        }).start();
    }
}
