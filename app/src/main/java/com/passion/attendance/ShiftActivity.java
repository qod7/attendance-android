package com.passion.attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.passion.attendance.Models.Shift;

public class ShiftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ListView shiftsListView = (ListView) findViewById(R.id.content_list);
        DatabaseHandler db = new DatabaseHandler(this);
        Shift shift = db.retrieveShift();
        ShiftAdapter shiftAdapter = new ShiftAdapter(this, shift);

        shiftsListView.setAdapter(shiftAdapter);

    }
}
