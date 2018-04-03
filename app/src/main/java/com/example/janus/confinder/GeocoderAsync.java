package com.example.janus.confinder;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.janus.confinder.data.Convention;

import java.io.IOException;
import java.util.List;

public  class GeocoderAsync {

    Geocoder geocoder;

    public interface GeocoderCallback {
        void onLocationReturned(List<Address> addresses);
        void onGeocoderError();
    }

    public GeocoderAsync (Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void getLocation (String address, final GeocoderCallback geocoderCallback) {

        new AsyncTask <String, Void, List<Address>> () {
            @Override
            protected List<Address> doInBackground(String... params) {

                String address = params[0];

                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    cancel(true);
                }

                geocoderCallback.onLocationReturned(addresses);

                return addresses;
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                geocoderCallback.onLocationReturned(addresses);
            }

            @Override
            protected void onCancelled() {
                geocoderCallback.onGeocoderError();
            }

        }.execute(address);

    }

}