package com.example.janus.confinder;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class GetCons implements Runnable {

        Handler mainThreadHandler;
        Geocoder geocoder;
        GetConventions consFromWeb;

        public GetCons(Handler mainThreadHandler, GetConventions consFromWeb, Geocoder geocoder) {
            this.mainThreadHandler = mainThreadHandler;
            this.consFromWeb = consFromWeb;
            this.geocoder = geocoder;
        }

        @Override
        public void run() {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
            String strDate = mdformat.format(calendar.getTime());

            for (Convention convention: consFromWeb.getConventionsFromWeb()) {

                if (convention.getStartDate().compareTo(strDate) > 0) {
                    Message returnConvention = Message.obtain();

                    try {
                        List<Address> addresses = geocoder.getFromLocationName(convention.getAddress(), 1);

                        if (addresses.size() > 0) {
                            convention.setLongitude(addresses.get(0).getLongitude());
                            convention.setLatitude(addresses.get(0).getLatitude());

                            returnConvention.obj = convention;
                            mainThreadHandler.sendMessage(returnConvention);
                        }

                    } catch (IOException e) {
// TODO Need to handle Retrofit errors elegantly. At present, the map will be blank if there is an IO Error.
// TODO That works, but it would be better with a more helpful error message.
                    }


                }
            }

// Sends back an empty Convention object o indicate it has completed
            Message lastConvention = Message.obtain();
            lastConvention.obj = null;
            mainThreadHandler.sendMessage(lastConvention);

        }}
