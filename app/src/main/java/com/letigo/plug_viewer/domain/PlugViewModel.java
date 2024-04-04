package com.letigo.plug_viewer.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.letigo.plug_viewer.data.api.PlugState;
import com.letigo.plug_viewer.data.db.PlugEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlugViewModel extends ViewModel {
    private final PlugRepoImpl repo = new PlugRepoImpl();
    private PlugEntity selectedPlug = null;
    public boolean threadRunning = true;
    private final Thread plugPoller = new Thread(new Runnable() {
        @Override
        public void run() {
            while (threadRunning) {
                try {
                    repo.getPlugInfo(selectedPlug.address).enqueue(new Callback<PlugState>() {
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
                    Log.d("INTERNET_ERROR", "АШЫБКА БЛИН!!!");
                    _errorOccured.postValue(true);
                }
            }
        }
    });

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

    public void startPollPlug(int plugId) {
        selectedPlug = repo.getPlugById(plugId);
        threadRunning = true;
        plugPoller.start();
    }
    public void endPollPlug() {
        threadRunning = false;
        plugPoller.interrupt();
    }
}
