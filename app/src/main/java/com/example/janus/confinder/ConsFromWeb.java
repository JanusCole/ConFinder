package com.example.janus.confinder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ConsFromWeb {

    @GET("/cons")
    Call<List<Convention>> getConventions();

}
