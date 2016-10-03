package com.ucsdtasks.android;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ucsdtasks.backend.UCSDTask;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;
import static android.R.attr.value;
import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;
import static com.google.android.gms.internal.zznu.is;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
/**
 * Class MapsActivity
 *
 * Generates a google maps layout on users screen and requests locations permissions, which if
 * granted, will use to zoom to user's location on map and enable location tracking.
 */

    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private double radius = 40.0;
    private LocationManager locationManager;
    private GeoQuery geoQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Method onMapReady
     *
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);  // adding the +/- button on the map
        mMap.getUiSettings().setAllGesturesEnabled(true);   // pinch to zoom on map

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        else {
            startUp();
        }


    }


    private void updateGeoQuery() {
        if (geoQuery == null) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            GeoFire geoFire = new GeoFire(ref.child("geofire"));
            geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), radius);


            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                private HashMap<String, Marker> markers = new HashMap<>();
                private HashMap<String, ValueEventListener> listeners = new HashMap<>();
                final DatabaseReference tasks = ref.child("tasks");
                final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {
                    listeners.put(key, new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final UCSDTask task = dataSnapshot.getValue(UCSDTask.class);

                                    if (task.getAuthorID() == userID) {
                                        tasks.removeEventListener(this);
                                        return;
                                    }

                                    Marker marker;
                                    if (markers.containsKey(key)) {
                                        marker = markers.get(key);
                                        marker.setPosition(new LatLng(location.latitude, location.longitude));
                                    }
                                    else {
                                        marker = mMap.addMarker(
                                                new MarkerOptions()
                                                        .position(new LatLng(location.latitude, location.longitude))
                                        );
                                        markers.put(key, marker);
                                    }
                                    marker.setTitle(task.getTitle());
                                    marker.setSnippet(task.getDescription());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // TODO: Report that fetching failed to load data
                                }
                            }
                    );
                    tasks.child(key).addValueEventListener(listeners.get(key));
                }

                @Override
                public void onKeyExited(String key) {
                    markers.remove(key).remove();
                    tasks.child(key).removeEventListener(listeners.remove(key));

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    markers.get(key).setPosition(new LatLng(location.latitude, location.longitude));
                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }
        else {
            geoQuery.setLocation(new GeoLocation(latitude, longitude), radius);
        }
    }

    /**
     * Method onRequestPermissionsResults
     *
     * This callback is triggered when the user approves locations permissions.
     * It will enable location tracking and zoom to user's location.
     */

    public boolean startUp() {
        // enables location features
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        String locationProvider = LocationManager.GPS_PROVIDER;
        try {
            mMap.setMyLocationEnabled(true);
            Location location = locationManager.getLastKnownLocation(locationProvider);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            updateGeoQuery();
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f));
            locationManager.requestLocationUpdates(locationProvider, 0, 100, locationListener);
            return true;
        }
        catch (SecurityException e) {
            Toast.makeText(this, "Location not enabled", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    // This will run automatically after user approves location data
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                startUp();
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
        else {
            Toast.makeText(this, "Location not enabled", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Variable locationListener
     *
     * Needed to notify the user when a friend is nearby the marked position.
     */

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            updateGeoQuery();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.create_task) {
            Intent mainAcitivity = new Intent("android.intent.action.CREATE");
            mainAcitivity.putExtra("Longitude", longitude);
            mainAcitivity.putExtra("Latitude", latitude);
            mainAcitivity.putExtra("Zoom", mMap.getCameraPosition().zoom);
            mainAcitivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainAcitivity);
        }
        return super.onOptionsItemSelected(item);
    }

/*
    public void createTask(View view) {
        Intent mainAcitivity = new Intent("android.intent.action.CREATE");
        mainAcitivity.putExtra("Longitude", longitude);
        mainAcitivity.putExtra("Latitude", latitude);
        mainAcitivity.putExtra("Zoom", mMap.getCameraPosition().zoom);
        mainAcitivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainAcitivity);
    }

*/
}
