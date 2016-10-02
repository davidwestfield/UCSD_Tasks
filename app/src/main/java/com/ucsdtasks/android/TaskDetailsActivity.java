package com.ucsdtasks.android;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ucsdtasks.backend.UCSDTask;

/**
 * Class TaskDetailsActivity
 *
 * Generates a task's details on the user's screen.
 */

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // get the UCSD task from parceable, pass it to update texts
        updateTexts((UCSDTask) getIntent().getExtras().getParcelable("TASK"));
    }


    /**
     * Method updateTexts
     *
     * Grabs the text's from the UCSDTask and displays it on screen.
     */

    private void updateTexts(UCSDTask task) {
        TextView titleText = (TextView) findViewById(R.id.title);
        TextView bidText = (TextView) findViewById(R.id.bid);
        TextView authorText = (TextView) findViewById(R.id.author);
        TextView descriptionText = (TextView) findViewById(R.id.description);

        // Sets texts from task
        titleText.setText(task.getTitle());
        bidText.setText(task.getAskingPrice());
        authorText.setText(task.getAuthorID());
        descriptionText.setText(task.getDescription());
    }
}

