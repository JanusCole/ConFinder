package com.example.janus.confinder.data;

import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

// This is a simple implementation of a Retrofit client to obtain the Convention Events

public class RemoteConventionsEventService implements ConventionsEventService {

    private String baseURL = "http://www.januscole.com";

    public interface ConventionsService {
        @GET("/cons")
        Call<List<ConventionEvent>> getConventions();
    }

    @Override
    public void getConventionEvents(final ConventionEventsCallback conventionEventsCallback) {

        // Create the Retrofit client
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        ConventionsService conventionEventsClient = retrofit.create(ConventionsService.class);

        Call<List<ConventionEvent>> conventionResults = conventionEventsClient.getConventions();

        // Call Retrofit asyncronously
        conventionResults.enqueue(new Callback<List<ConventionEvent>>() {
            @Override
            public void onResponse(Call<List<ConventionEvent>> call, Response<List<ConventionEvent>> response) {
                conventionEventsCallback.onConventionsComplete(response.body());
            }

            @Override
            public void onFailure(Call<List<ConventionEvent>> call, Throwable t) {
                conventionEventsCallback.onNetworkError();
            }
        });

    }

    @VisibleForTesting
    public void setBaseURL (String baseURL) {
        this.baseURL = baseURL;
    }


}
