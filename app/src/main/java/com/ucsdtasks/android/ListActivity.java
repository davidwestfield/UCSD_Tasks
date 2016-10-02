package com.ucsdtasks.android;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.ucsdtasks.android.auth.AuthActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Class ListActivity
 *
 * Generates a list view of the tasks to be done in the user's area.
 */

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private LatLng currentLoc;
    private List<UCSDTask> tasks = new List<UCSDTask>();
    private List<String> taskTitles = new ArrayList<String>();

    private double SEARCH_RADIUS = 0.6; // TODO temporary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Checks if user has location enabled
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            populateListView();
        }
        // User does not have location enabled, requests permissions
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }


    /**
     * Method populateListView
     *
     * This callback is triggered when the user approves locations permissions.
     * It will enable location tracking and zoom to user's location.
     */

    public void populateListView() {
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Requests tasks passing in user's current location and radius to look for tasks
        //tasks = getTasks(currentLoc, SEARCH_RADIUS);

        // Task objects will have ID, title, author, bid, description, and location
        for (task : tasks) {
            taskTitles.add(task.getTitle());
        }

        // (Context, Layout for row, ID of TextView to write data to, Array of data)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, taskTitles);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


        /**
         * Method onItemClick
         *
         * Listens for clicks on the list's tasks, and launches a view detailing that task.
         *
         * @param position   Index of item clicked
         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Grabs task from clicked item
                UCSDTask ucsdTask = tasks.get(position);

                // Launches details window passing along task as a parceable
                Intent detailIntent = new Intent(ListActivity.this, TaskDetailsActivity.class);
                detailIntent.putExtra("TASK", ucsdTask);
                startActivity(detailIntent);
            }
        });
    }


    /**
     * Method onRequestPermissionsResults
     *
     * This callback is triggered when the user approves locations permissions.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        populateListView();
    }
}