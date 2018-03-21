package com.example.janus.confinder;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ConventionFinderPresenter implements ConventionFinderContract.Presenter {

        ConventionFinderContract.View conventionFinderView;
        Geocoder geocoder;
        GetConventions consFromWeb;

        public ConventionFinderPresenter(ConventionFinderContract.View conventionFinderView, GetConventions consFromWeb, Geocoder geocoder) {
            this.conventionFinderView = conventionFinderView;
            this.consFromWeb = consFromWeb;
            this.geocoder = geocoder;
        }

        public void getConventions() {

            new SearchForConventions().execute();

        }

    private class SearchForConventions extends AsyncTask<Void, Convention, Void> {
        @Override
        protected void onProgressUpdate(Convention... values) {
            conventionFinderView.mapConvention(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
            String strDate = mdformat.format(calendar.getTime());

            for (Convention convention: consFromWeb.getConventionsFromWeb()) {

                if (convention.getStartDate().compareTo(strDate) > 0) {

                    try {
                        List<Address> addresses = geocoder.getFromLocationName(convention.getAddress(), 1);

                        if (addresses.size() > 0) {
                            convention.setLongitude(addresses.get(0).getLongitude());
                            convention.setLatitude(addresses.get(0).getLatitude());

                            publishProgress(convention);
                        }

                    } catch (IOException e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            conventionFinderView.completeMap();
        }
    }

}
