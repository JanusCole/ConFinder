package com.example.janus.confinder.data;

import android.os.AsyncTask;

import java.util.List;

public class RemoteConventionsDataSource implements ConventionsDataSource {

        ConventionsAPI consFromWeb;

        public RemoteConventionsDataSource(ConventionsAPI consFromWeb) {
            this.consFromWeb = consFromWeb;
        }

        @Override
        public void getConventions(final ConventionsDataSourceCallback conventionsDataSourceCallback) {

            new AsyncTask<Void, Void, List<Convention>> () {

            @Override
            protected List<Convention> doInBackground(Void... params) {

                List<Convention> conventions = consFromWeb.getConventions();

                return conventions;
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
