package com.passion.attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.passion.attendance.Models.Message;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/30/2016.
 */
public class MessagesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ListView messagesListView = (ListView) findViewById(R.id.content_list);
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<Message> messages = db.retrieveMessages();
        MessageListAdapter messageListAdapter = new MessageListAdapter(this, messages);

        messagesListView.setAdapter(messageListAdapter);
    }
}
