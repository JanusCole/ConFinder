package com.example.janus.confinder.data;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class RemoteConventionsService implements ConventionsService {

        private ConventionsService conventionsWebService;

    public RemoteConventionsService(ConventionsService conventionsWebService) {
            this.conventionsWebService = conventionsWebService;
        }

        @Override
        public void getConventions(final ConventionsDataSourceCallback conventionsDataSourceCallback) {

            new AsyncTask<Void, Void, List<Convention>> () {

                private List <Convention> conventionResult = new ArrayList<>();

                @Override
            protected List<Convention> doInBackground(Void... params) {

                conventionsWebService.getConventions(new ConventionsDataSourceCallback() {
                    @Override
                    public void onConventionsComplete(List<Convention> conventions) {
                        conventionResult =  conventions;
                    }

                    @Override
                    public void onNetworkError() {
                        cancel(true);
                    }
                });

                return conventionResult;
            }

                @Override
                protected void onPostExecute(List<Convention> conventions) {
                    conventionsDataSourceCallback.onConventionsComplete(conventions);
                }

                @Override
            protected void onCancelled() {
                conventionsDataSourceCallback.onNetworkError();
            }
        }.execute();
  }


}
