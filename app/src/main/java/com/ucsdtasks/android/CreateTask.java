package com.ucsdtasks.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CreateTask extends AppCompatActivity {

    String task_name;
    String location;
    double starting_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void pushTask (View view) {

        // Linking UI to code level
        final EditText task_name_ET = (EditText) findViewById(R.id.task_name);
        final EditText starting_offer_ET = (EditText) findViewById(R.id.starting_offer);
        final EditText location_ET = (EditText) findViewById(R.id.specify_location);

        // Storing and parsing from string to wanted data type
        task_name = task_name_ET.getText().toString();
        location = starting_offer_ET.getText().toString();
        starting_offer = Double.parseDouble(location_ET.getText().toString());
    }

    public void doneEnteringData (View view) {

        Snackbar.make(view, "Data stored", Snackbar.LENGTH_SHORT).show();
    }

}
