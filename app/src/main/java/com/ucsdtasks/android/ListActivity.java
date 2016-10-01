package com.ucsdtasks.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Class ListActivity
 *
 * Generates a list view of the tasks to be done in the user's area.
 */

public class ListActivity extends AppCompatActivity {
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Values to be shown in ListView
        String[] values = new String[] {
                "Sample 1",
                "Sample 2",
                "Sample 3",
                "Sample 4",
                "Sample 5"
        };

        // (Context, Layout for row, ID of TextView to write data to, Array of data)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


        /**
         * Method onItemClick
         *
         * Listens for clicks on the list's items.
         *
         * @param position   Index of item clicked
         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Grabs name of clicked item
                String itemName = (String) listView.getItemAtPosition(position);
            }
        });
    }
}