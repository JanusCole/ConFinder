package com.example.janus.confinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.janus.confinder.data.Convention;
import com.example.janus.confinder.data.ConventionsService;
import com.example.janus.confinder.data.ConventionsServiceImplementation;
import com.example.janus.confinder.data.ConventionsWebAPI;
import com.example.janus.confinder.data.RemoteConventionsService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// This is a toy app that I wrote to experiment with Google Maps.The app
// reads in data for upcoming Comic Cons across the world and displays them on a Google Maps API.
// Then it zooms in on the user's location (currently hard coded to Boston because I live near Boston : -)
// It gets the Comic Con data from a REST API that is consumed using Retrofit.

// TODO Get Location From User

public class ConventionFinderActivity extends FragmentActivity implements ConventionFinderContract.View, OnMapReadyCallback  {

    private ConventionFinderContract.Presenter conventionFinderPresenter;

    private GoogleMap mMap;

    private AlertDialog networkActivityDialog;

    // TODO Change to get user location dynamically
    private LatLng userLocation = new LatLng(42.36793719999999,-71.076245); // Boston Coordinates
    private final LatLng USA_LOCATION = new LatLng(38,-94); // Center of Continental USA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ConventionsService conventionsAPI = new ConventionsWebAPI();
        ConventionsService conventionsDataSource = new RemoteConventionsService(conventionsAPI);
        ConventionsService conventionsRepository = new ConventionsServiceImplementation(conventionsDataSource);

        conventionFinderPresenter = new ConventionFinderPresenter(this,
                conventionsRepository,
                new Geocoder(this));

        setupStartSearchDialog();

// Set up the dialog box for network activity
        networkActivityDialog = showNetworkActivityAlert(this.getLayoutInflater(), this);

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

// Start the search for conventions
    private void searchForCons() {

        networkActivityDialog.show();

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(USA_LOCATION));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(USA_LOCATION, 4.0f));

        conventionFinderPresenter.getConventions();

    }

// When you click the "CON FINDER" heading, it refreshes the map here
    public void refreshButtonOnClick(View v) {

        searchForCons();
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

    public void displayErrorMessageAlertDialog(String alertMessage, Activity activity, Context context) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(dialogView);

        TextView alertDialogMessage = (TextView) dialogView.findViewById(R.id.messageTextView_AlertDialog);
        alertDialogMessage.setText(alertMessage);

        final AlertDialog errorMessageAlertDialog = alertDialogBuilder.create();
        errorMessageAlertDialog.setCanceledOnTouchOutside(true);

        Button dialogButton = (Button) dialogView.findViewById(R.id.okButton_AlertDialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessageAlertDialog.dismiss();
            }
        });

        errorMessageAlertDialog.show();
    }

    // Method for setting up the network busy message
    public AlertDialog showNetworkActivityAlert(LayoutInflater inflater,Context context) {

        View dialogView = inflater.inflate(R.layout.busy_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(dialogView);

        return alertDialogBuilder.create();

    }


    @Override
    public void mapConvention(Convention convention) {

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(convention.getLatitude(), convention.getLongitude()))
                .title(convention.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.comicconicon))
                .snippet(convention.getWebsite()));

    }

    @Override
    public void completeMap() {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 7.0f));

        networkActivityDialog.dismiss();

    }

    @Override
    public void displayNetworkError() {
        displayErrorMessageAlertDialog(getString(R.string.network_error_message), this, this);

    }
}
