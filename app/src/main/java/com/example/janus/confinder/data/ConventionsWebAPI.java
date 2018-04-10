package com.example.janus.confinder.data;

import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ConventionsWebAPI implements ConventionsService {

    private String baseURL = "http://www.januscole.com";

    public interface ConventionsInterface {

        @GET("/cons")
        Call<List<Convention>> getConventions();

    }

    @Override
    public void getConventions(ConventionsDataSourceCallback conventionsDataSourceCallback) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        ConventionsInterface stockQuoteClient = retrofit.create(ConventionsInterface.class);

        Call<List<Convention>> conventionResults = stockQuoteClient.getConventions();

        List<Convention> conventionList = null;

        try {
            conventionList =  conventionResults.execute().body();
            conventionsDataSourceCallback.onConventionsComplete(conventionList);

        } catch (IOException e) {
            conventionsDataSourceCallback.onNetworkError();
        }

    }

    @VisibleForTesting
    public void setBaseURL (String baseURL) {
        this.baseURL = baseURL;
    }


}
