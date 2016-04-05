package com.passion.attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.passion.attendance.Models.Event;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/30/2016.
 */
public class EventsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ListView eventsListView = (ListView) findViewById(R.id.content_list);
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<Event> messages = db.retrieveEvents();
        EventListAdapter eventListAdapter = new EventListAdapter(this, messages);

        eventsListView.setAdapter(eventListAdapter);
    }

}
