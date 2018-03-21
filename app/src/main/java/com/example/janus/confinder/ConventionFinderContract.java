package com.example.janus.confinder;

import com.google.gson.annotations.SerializedName;

public  class ConventionFinderContract {

    public interface View {
        void mapConvention(Convention convention);
        void completeMap();
    }

    public interface Presenter {
        void getConventions();
    }

}