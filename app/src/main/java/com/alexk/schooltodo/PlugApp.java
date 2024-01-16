package com.alexk.schooltodo;

import android.app.Application;
import android.widget.Toast;

import androidx.room.Room;

import com.alexk.schooltodo.data.api.PlugApi;
import com.alexk.schooltodo.data.db.PlugDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlugApp extends Application {
    public static PlugApp instance;
    private PlugDatabase database;
    private PlugApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, PlugDatabase.class, "plug-db").allowMainThreadQueries().build();
    }

    public static PlugApp getInstance() {
        return instance;
    }

    public PlugDatabase getDatabase() {
        return database;
    }

    public PlugApi getApi(String url) {
        try {
            this.api = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url)
                    .build()
                    .create(PlugApi.class);
            return api;
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(this, "Ошибка: введен неправильный адрес лампочки!", Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
