package com.example.janus.confinder.data;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

// This class implementsw the convention locator service. It takes in a list of convention event addresses and uses a geocoder
// to find their respective longitude and latitude vales. I created this because Geocoder locks up the UI while it runs. So putting
// it into an asynctask allows the UI to be updated in real time with a cute animation of icons appearing on the map. I used async task
// in order to take advantage of the publishProgress method to pass back convention locations as they are generated..

public class ConventionsLocator implements ConventionLocatorService {

    private Geocoder geocoder;

    public ConventionsLocator(Geocoder geocoder) {
            this.geocoder = geocoder;
        }

    @Override
    @SuppressWarnings("unchecked")
        public void getConventionLocations(List<ConventionEvent> conventions, final ConventionLocatorServiceCallback conventionLocatorServiceCallback) {

        new AsyncTask<List<ConventionEvent>, ConventionLocation, Void>() {
            @Override
            protected Void doInBackground(List<ConventionEvent>... params) {

                List<ConventionEvent> conventionEvents = params[0];

                // Check to see if the event is in the future
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                String strDate = mdformat.format(calendar.getTime());

                // Loop through the list of ConventionEvents to obtain their coordinates
                for (int conventionIndex = 0; conventionIndex < conventionEvents.size(); conventionIndex++) {

                    if (conventionEvents.get(conventionIndex).getStartDate().compareTo(strDate) > 0) {

                        // Create a ConventionLocation object using data from the ConventionEvent object
                        ConventionLocation conventionLocation = new ConventionLocation();

                        conventionLocation.setName(conventionEvents.get(conventionIndex).getName());
                        conventionLocation.setWebsite(conventionEvents.get(conventionIndex).getWebsite());

                        // Gets the convention coordinates using a geocoder
                        List<Address> addresses = null;

                        try {
                            addresses = geocoder.getFromLocationName(conventionEvents.get(conventionIndex).getAddress(), 1);
                        } catch (IOException e) {
                            cancel(true);
                        }

                        if (addresses.size() > 0) {
                            conventionLocation.setLongitude(addresses.get(0).getLongitude());
                            conventionLocation.setLatitude(addresses.get(0).getLatitude());

                            // pass the completed ConventionLocation to the caller
                            publishProgress(conventionLocation);
                        }

                    }
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                conventionLocatorServiceCallback.onConventionLocationsComplete();
            }

            @Override
            protected void onProgressUpdate(ConventionLocation... values) {
                conventionLocatorServiceCallback.onConventionLocated(values[0]);
            }

            @Override
            protected void onCancelled() {
                conventionLocatorServiceCallback.onNetworkError();
            }

        }.execute(conventions);
    }

}
