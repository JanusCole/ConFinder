package com.example.janus.confinder.data;

import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RemoteConventionsAPI implements ConventionsAPI {

    private String baseURL = "http://www.januscole.com";

    public interface ConventionsWebAPI {

        @GET("/cons")
        Call<List<Convention>> getConventions();

    }

    @Override
    public List<Convention> getConventions() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        ConventionsWebAPI stockQuoteClient = retrofit.create(ConventionsWebAPI.class);

        Call<List<Convention>> conventionResults = stockQuoteClient.getConventions();

        List<Convention> conventionList = null;

        try {
            conventionList =  conventionResults.execute().body();

        } catch (IOException e) {
        }

        return conventionList;

    }

    @VisibleForTesting
    public void setBaseURL (String baseURL) {
        this.baseURL = baseURL;
    }


}
