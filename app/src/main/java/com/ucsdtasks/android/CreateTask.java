package com.ucsdtasks.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.description;

public class CreateTask extends AppCompatActivity {

    String task_name;
    String location;
    String description;
    double starting_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
    }

    public void pushTask (View view) {

        // Linking UI to code level
        final EditText task_name_ET = (EditText) findViewById(R.id.task_name);
        final EditText starting_offer_ET = (EditText) findViewById(R.id.starting_offer);
        final EditText location_ET = (EditText) findViewById(R.id.specify_location);
        final EditText description_ET = (EditText) findViewById(R.id.description);

        // Error checking
        if(task_name_ET.getText().toString().matches("") ||
                starting_offer_ET.getText().toString().matches("")) {
            Snackbar.make(view, "Field(s) cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Storing and parsing from string to wanted data type
        task_name = task_name_ET.getText().toString();

        // TODO: Location should open a map, not be a text edit.
        // location = location_ET.getText().toString();

        Bundle extras = getIntent().getExtras();
        double longitude, latitude = 0;
        if (extras != null) {
            longitude = extras.getDouble("Longitude");
            latitude = extras.getDouble("Latitude");
        }
        description = description_ET.getText().toString();
        starting_offer = Double.parseDouble(starting_offer_ET.getText().toString());
        Toast.makeText(this, "Task submitted", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
    
}
