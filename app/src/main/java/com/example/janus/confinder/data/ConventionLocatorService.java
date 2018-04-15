package com.example.janus.confinder.data;

import java.util.List;

// This service takes in a list of convention events and returns their respective coordinates for mapping

public interface ConventionLocatorService {

    interface ConventionLocatorServiceCallback {
        void onConventionLocated(ConventionLocation conventionLocation);
        void onConventionLocationsComplete();
        void onNetworkError();
    }

    void getConventionLocations(List<ConventionEvent> conventions, ConventionLocatorServiceCallback conventionLocatorServiceCallback);

}
