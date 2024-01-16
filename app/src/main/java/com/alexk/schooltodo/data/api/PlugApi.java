package com.alexk.schooltodo.data.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlugApi {
    @GET("/meter/0")
    Call<PlugState> getPlugInfo();
}
