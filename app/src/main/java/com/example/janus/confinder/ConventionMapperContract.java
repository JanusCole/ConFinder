package com.example.janus.confinder;

import com.example.janus.confinder.data.ConventionEvent;
import com.example.janus.confinder.data.ConventionLocation;

// Specifies the contract between the map presenter and view

public interface ConventionMapperContract {

    interface View {
        void mapConvention(ConventionLocation conventionLocation);
        void completeMap();
        void displayNetworkError();
    }

    interface Presenter {
        void mapConventions();
    }

}