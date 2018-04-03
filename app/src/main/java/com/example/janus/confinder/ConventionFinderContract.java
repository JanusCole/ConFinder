package com.example.janus.confinder;

import com.example.janus.confinder.data.Convention;

public  class ConventionFinderContract {

    public interface View {
        void mapConvention(Convention convention);
        void completeMap();
        void displayNetworkError();
    }

    public interface Presenter {
        void getConventions();
    }

}