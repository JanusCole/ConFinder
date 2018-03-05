package com.example.janus.confinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// This app reads in data for upcoming Comic Cons across the world and displays them on a Google Maps API.
// Then it zooms in on the user's location (currently hard coded to Boston because I live near Boston : -)
// It gets the Comic Con data from a REST API that is consumed using Retrofit.

// TODO Get Location From User

// TODO Either move the Retrofit call to a separate HandlerThread and then call the Geocoder in a different HandlerThread or
// TODO set up the Retrofit call as an async and then call Gecoder in a HandlerThread from the callback. Undecided which way to go.

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private AlertDialog networkActivityDialog;

    private HandlerThread getConsHandlerThread;
    private Handler getConsHandlerThreadHandler;

    // TODO Change to get user location dynamically
    private LatLng userLocation = new LatLng(42.36793719999999,-71.076245); // Boston Coordinates
    private final LatLng USA_LOCATION = new LatLng(38,-94); // Center of Continental USA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupStartSearchDialog();

// Set up the dialog box for network activity
        setupNetworkActivityDialog();

        getConsHandlerThread = new HandlerThread(getString(R.string.get_cons));
        getConsHandlerThread.start();

        getConsHandlerThreadHandler = new Handler(getConsHandlerThread.getLooper());

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

// Start the search for conventions
    private void searchForCons() {

        networkActivityDialog.show();

        EspressoIdlingResource.increment();

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(USA_LOCATION));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(USA_LOCATION, 4.0f));

        getConsHandlerThreadHandler.post(new GetCons(handleConAddresses,
                new RetrofitConsFromWeb(getString(R.string.convention_search_url)),
                new Geocoder(this)));

    }

// When you click the "CON FINDER" heading, it refreshes the map here
    public void refreshButtonOnClick(View v) {

        searchForCons();
    }

// Method for setting up the network busy message
    private void setupNetworkActivityDialog() {

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.busy_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(dialogView);

        networkActivityDialog = alertDialogBuilder.create();

    }

// Method for setting up the start search button
    private void setupStartSearchDialog() {

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.start_search_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(dialogView);

        final AlertDialog startSearchAlertDialog = alertDialogBuilder.create();

        Button startSearchButton = (Button) dialogView.findViewById(R.id.searchButton_AlertDialog);
        startSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchAlertDialog.dismiss();
                searchForCons();

            }
        });

        startSearchAlertDialog.show();

    }

// This handles results from the REST call. The thread will send back a null object when it is done.
// So that triggers the zoom functionality. Otherwise, take the geolocation and set a map marker. The thread sends
// back one Convention object at a time.
    Handler handleConAddresses = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.obj != null) {

                Convention convention = (Convention) msg.obj;

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(convention.getLatitude(), convention.getLongitude()))
                        .title(convention.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.comicconicon))
                        .snippet(convention.getWebsite()));

            } else {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 7.0f));

                networkActivityDialog.dismiss();
                EspressoIdlingResource.decrement();

            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View view = getLayoutInflater().inflate(R.layout.convention_info, null);

                    ImageView iconImage = (ImageView) view.findViewById(R.id.iconImageView);
                    Bitmap conIcon = BitmapFactory.decodeResource(getResources(), R.drawable.comicconicon);
                    iconImage.setImageBitmap(conIcon);

                    TextView conventionName = (TextView) view.findViewById(R.id.nameTextView);
                    conventionName.setText(marker.getTitle());

                    return view;
                }
            });

// This code sends the user to a website for the Convention when the map marker is clicked
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent webview = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.getSnippet()));
                    startActivity(webview);
                }
            });

        }

    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
