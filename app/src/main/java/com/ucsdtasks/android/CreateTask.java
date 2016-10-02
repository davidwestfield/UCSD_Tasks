package com.ucsdtasks.android;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.ucsdtasks.backend.UCSDTask;

public class CreateTask extends AppCompatActivity {

    String task_name;
    String location;
    String description;
    String starting_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
    }

    public void pushTask(View view) {

        // Linking UI to code level
        final EditText task_name_ET = (EditText) findViewById(R.id.task_name);
        final EditText starting_offer_ET = (EditText) findViewById(R.id.starting_offer);
        final EditText description_ET = (EditText) findViewById(R.id.description);

        // Error checking
        if (task_name_ET.getText().toString().matches("") ||
                starting_offer_ET.getText().toString().matches("")) {
            Snackbar.make(view, "Field(s) cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Storing and parsing from string to wanted data type
        task_name = task_name_ET.getText().toString();

        // TODO: Location should open a map, not be a text edit.
        // final EditText location_ET = (EditText) findViewById(R.id.specify_location);
        // location = location_ET.getText().toString();

        Bundle extras = getIntent().getExtras();
        double longitude = 0;
        double latitude = 0;
        if (extras != null) {
            longitude = extras.getDouble("Longitude");
            latitude = extras.getDouble("Latitude");
        }
        description = description_ET.getText().toString();
        starting_offer = starting_offer_ET.getText().toString();
        createTask(task_name, longitude, latitude, starting_offer, description);
        Toast.makeText(this, "UCSDTask submitted", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public void createTask(String title, Double lon, Double lat, String asking_price, String description) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference push = ref.child("tasks").push();
        UCSDTask task = new UCSDTask(push.getKey(), title, uid, asking_price, description);
        push.setValue(task);

        GeoFire geoFire = new GeoFire(ref.child("geofire"));
        geoFire.setLocation(key, new GeoLocation(lat, lon));
    }
}
