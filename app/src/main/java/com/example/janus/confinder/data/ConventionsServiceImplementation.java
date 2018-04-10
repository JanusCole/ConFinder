package com.example.janus.confinder.data;

import java.util.List;

public class ConventionsServiceImplementation implements ConventionsService {

        private ConventionsService conventionsService;

        public ConventionsServiceImplementation(ConventionsService conventionsService) {
            this.conventionsService = conventionsService;
        }

        @Override
        public void getConventions(final ConventionsDataSourceCallback conventionsDataSourceCallback) {

            conventionsService.getConventions(new ConventionsDataSourceCallback() {
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
