package com.example.janus.confinder.data;

import java.util.List;

public class ConventionsRepository implements ConventionsDataSource {

    ConventionsDataSource conventionsDataSource;

        public ConventionsRepository(ConventionsDataSource conventionsDataSource) {
            this.conventionsDataSource = conventionsDataSource;
        }

        @Override
        public void getConventions(final ConventionsDataSourceCallback conventionsDataSourceCallback) {

            conventionsDataSource.getConventions(new ConventionsDataSourceCallback() {
                @Override
                public void onConventionsComplete(List<Convention> conventions) {
                    conventionsDataSourceCallback.onConventionsComplete(conventions);
                }

                @Override
                public void onNetworkError() {
                    conventionsDataSourceCallback.onNetworkError();
                }
            });

        }


}
