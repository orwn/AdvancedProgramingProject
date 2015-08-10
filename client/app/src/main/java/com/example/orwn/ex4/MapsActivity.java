package com.example.orwn.ex4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.orwn.ex4.Chat.ReloadService;
import com.example.orwn.ex4.FriendList.FriendListFragment;
import com.example.orwn.ex4.Manu.MenuFragment;
import com.example.orwn.ex4.Screens.FriendInfoFragment;
import com.example.orwn.ex4.Swipe.OnSwipeTouchListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity  implements
         GoogleApiClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationRequest mLocationRequest;
    private SwipeRefreshLayout swipeLayout;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

  //  public static int count = 0;

    GoogleApiClient mGoogleApiClient;

    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Moving the splash activity out from the stack


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {




            FriendListFragment fif = new FriendListFragment();


            FragmentTransaction ft = getFragmentManager().beginTransaction();


            ft.add(R.id.mainList,fif);
            // ft.addToBackStack("FriendList");

            ft.commit();

        }
        setContentView(R.layout.activity_maps);


        setUpMapIfNeeded();


        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        Button btn_menu = (Button) findViewById(R.id.button_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = "blabla";
                FragmentManager fm = getFragmentManager();

                MenuFragment mf = new MenuFragment();
                FragmentTransaction ft = fm.beginTransaction();
                FriendListFragment fif = new FriendListFragment();

                int count = fm.getBackStackEntryCount();
                // If there are names at the backstack entry
                if (count > 0) {
                    name = getFragmentManager().getBackStackEntryAt(0).getName();
                }
                Log.i("Name", name);
                // Does not accept double click
                if (name != "menu Fragmet") ;
                {
                    ft.replace(R.id.map, mf);
                    ft.addToBackStack("menu Fragmet");

                    ft.commit();
                }


            }

            //  }
        });

        createLocationRequest();
        /*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.maps_layout);
            ll.setOnTouchListener(new OnSwipeTouchListener(MapsActivity.this) {
                int count;
                FriendListFragment fif = new FriendListFragment();

                @Override
                public void onSwipeLeft() {
                    if (count == 1) {
                        // FriendListFragment fif = new FriendListFragment();


                        FragmentTransaction ft = getFragmentManager().beginTransaction();


                        ft.remove(fif);
                        // ft.addToBackStack("FriendList");

                        ft.commit();
                        count--;
                    }
                }

                @Override
                public void onSwipeRight() {

                    if (count == 0) {
                        //FriendListFragment fif = new FriendListFragment();


                        FragmentTransaction ft = getFragmentManager().beginTransaction();


                        ft.replace(R.id.map_container, fif);
                        // ft.addToBackStack("FriendList");

                        ft.commit();
                        count++;
                    }
                }
            });
        }
*/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //mMap.setOn
        // Setting a swipe down event
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.map_swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Starting refresh service

                Intent intent = new Intent(MapsActivity.this, ReloadService.class);
                startService(intent);
            }
        });

        // Setting the colors of the layout
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Setting the filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReloadService.DONE);
        registerReceiver(reloadDone, intentFilter);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            swipeLayout.setOnTouchListener(new OnSwipeTouchListener(MapsActivity.this)
            {
                int count;
                FriendListFragment fif = new FriendListFragment();

                @Override
                public void onSwipeLeft() {
                    if (count == 1) {
                        // FriendListFragment fif = new FriendListFragment();


                        FragmentTransaction ft = getFragmentManager().beginTransaction();


                        ft.remove(fif);
                        // ft.addToBackStack("FriendList");

                        ft.commit();
                        count--;
                    }
                }

                @Override
                public void onSwipeRight() {

                    if (count == 0) {
                        //FriendListFragment fif = new FriendListFragment();


                        FragmentTransaction ft = getFragmentManager().beginTransaction();


                        ft.replace(R.id.map_container, fif);
                        // ft.addToBackStack("FriendList");

                        ft.commit();
                        count++;
                    }
                }
            });

        }

        //mMap.getUiSettings().setRotateGesturesEnabled(false);
        //mMap.getUiSettings().setAllGesturesEnabled(false);
       // mMap.getUiSettings().



    }
    private BroadcastReceiver reloadDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            Toast.makeText(MapsActivity.this, "Reload is done", Toast.LENGTH_SHORT).show();
        }
    };

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }




    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }





                    }
                });
                t.start();;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
//        unregisterReceiver(reloadDone);
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));


    }

    @Override
    public void onLocationChanged(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.clear();

                // Adding new markers
                mMap.addMarker(new MarkerOptions().title("Me").position(new LatLng(location.getLatitude(), location.getLongitude())));
                mMap.addMarker(new MarkerOptions().title("F1").position(new LatLng(location.getLatitude()+0.0001, location.getLongitude())));
                mMap.addMarker(new MarkerOptions().title("F2").position(new LatLng(location.getLatitude()+0.0004, location.getLongitude())));
                mMap.addMarker(new MarkerOptions().title("F3").position(new LatLng(location.getLatitude()+0.0007, location.getLongitude())));
                mMap.addMarker(new MarkerOptions().title("F4").position(new LatLng(location.getLatitude()+0.0010, location.getLongitude())));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {



                        // Setting makrkers click listeners
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        FriendInfoFragment fif;
                        String[] friends_names  = getResources().getStringArray(R.array.Friends_Names);
                        String[] friends_statuses  = getResources().getStringArray(R.array.Friends_Statuses);

                        switch (marker.getTitle()) {
                            case "Me":

                                fif = new FriendInfoFragment(getResources().getString(R.string.THIS_IS_YOU_MSG), "", R.drawable.sheldon_cooper);
                                break;
                            case "F1":

                                fif = new FriendInfoFragment(friends_names[0], friends_statuses[0], R.drawable.ic_launcher);
                                break;
                            case "F2":

                                fif = new FriendInfoFragment(friends_names[1], friends_statuses[1], R.drawable.ic_launcher);
                                break;
                            case "F3":

                                fif = new FriendInfoFragment(friends_names[2], friends_statuses[2], R.drawable.ic_launcher);
                                break;
                            case "F4":

                                fif = new FriendInfoFragment(friends_names[3], friends_statuses[3], R.drawable.ic_launcher);
                                break;

                            default:
                                fif = new FriendInfoFragment("null", "null", R.drawable.ic_launcher);
                                break;

                        }

                        ft.replace(R.id.map, fif);

                        ft.addToBackStack("menu Fragmet");


                        ft.commit();


                        Log.i("Marker Click","Clicked");

                        return false;
                    }
                });
               /* mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude()+4, location.getLongitude())));
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude()+10, location.getLongitude())));
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude() + 200, location.getLongitude())));
    */        }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect to location updates", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Starting location updates", Toast.LENGTH_SHORT).show();
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        setUpMapIfNeeded();
    }
}
