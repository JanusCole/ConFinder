package com.example.janus.confinder.data;

import com.example.janus.confinder.data.Convention;

import java.util.List;

public interface ConventionsDataSource {

    public interface ConventionsDataSourceCallback {
        void onConventionsComplete(List<Convention> conventions);
        void onNetworkError();
    }

    void getConventions(ConventionsDataSourceCallback conventionsDataSourceCallback);

}
