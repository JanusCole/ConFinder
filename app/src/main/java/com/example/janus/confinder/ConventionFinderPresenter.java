package com.example.janus.confinder;

import android.location.Address;
import android.location.Geocoder;

import com.example.janus.confinder.data.Convention;
import com.example.janus.confinder.data.ConventionsDataSource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ConventionFinderPresenter implements ConventionFinderContract.Presenter {

        ConventionFinderContract.View conventionFinderView;
        Geocoder geocoder;
        ConventionsDataSource conventionsDataSource;

        public ConventionFinderPresenter(ConventionFinderContract.View conventionFinderView, ConventionsDataSource conventionsDataSource, Geocoder geocoder) {
            this.conventionFinderView = conventionFinderView;
            this.conventionsDataSource = conventionsDataSource;
            this.geocoder = geocoder;
        }

        public void getConventions() {

            conventionsDataSource.getConventions(new ConventionsDataSource.ConventionsDataSourceCallback() {
                @Override
                public void onConventionsComplete(List<Convention> conventions) {

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                    String strDate = mdformat.format(calendar.getTime());

                    for (int conventionIndex = 0;conventionIndex < conventions.size();conventionIndex++) {

                        Convention convention = conventions.get(conventionIndex);

                        if (convention.getStartDate().compareTo(strDate) > 0) {

                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocationName(convention.getAddress(), 1);
                            } catch (IOException e) {
                                conventionFinderView.displayNetworkError();
                            }

                            if (addresses.size() > 0) {
                                convention.setLongitude(addresses.get(0).getLongitude());
                                convention.setLatitude(addresses.get(0).getLatitude());

                                conventionFinderView.mapConvention(convention);
                            }

                        }
                    }

                    conventionFinderView.completeMap();

                }

                @Override
                public void onNetworkError() {
                    conventionFinderView.displayNetworkError();
                }
            });

        }

}
