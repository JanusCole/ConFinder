package com.example.janus.confinder;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitConsFromWeb implements GetConventions {

    private String jsonBaseURL;

    public RetrofitConsFromWeb(String jsonBaseURL) {
        this.jsonBaseURL = jsonBaseURL;
    }

    @Override
    public List<Convention> getConventionsFromWeb() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(jsonBaseURL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        ConsFromWeb stockQuoteClient = retrofit.create(ConsFromWeb.class);

        Call<List<Convention>> conventionResults = stockQuoteClient.getConventions();

        List<Convention> conventionList = null;

        try {
            conventionList =  conventionResults.execute().body();

        } catch (IOException e) {
            Log.d("I/O ERROR", "Retrofit " + e.getMessage());
        }

        return conventionList;

    }
}
