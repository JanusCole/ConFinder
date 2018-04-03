package com.example.janus.confinder.data;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RemoteConventionsAPI implements ConventionsAPI {

    private String jsonBaseURL = "http://www.januscole.com";

    public interface ConventionsWebAPI {

        @GET("/cons")
        Call<List<Convention>> getConventions();

    }

    @Override
    public List<Convention> getConventionsFromWeb() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(jsonBaseURL)
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
}
