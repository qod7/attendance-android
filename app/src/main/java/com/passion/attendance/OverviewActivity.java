package com.passion.attendance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.passion.attendance.Models.Attendance;
import com.passion.attendance.Models.Event;
import com.passion.attendance.Models.Message;
import com.passion.attendance.Models.Staff;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OverviewActivity extends AppCompatActivity {

    CalendarFragment mCalendarView;
    View mCalendarContainer;
    ViewGroup mOverViewView;
    NestedScrollView mOverViewScrollView;

    DatabaseHandler mDatabaseHandler;

    private CalendarListener mCalendarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mOverViewView = (ViewGroup) findViewById(R.id.overview_container);
        mOverViewScrollView = (NestedScrollView) findViewById(R.id.overview_scrollview);

        mCalendarView = CalendarFragment.newInstance();
        mCalendarContainer = findViewById(R.id.calendar_container);

        InitializeCalendar();

        mCalendarView.setCalendarListener(getCalendarListener());

        loadData();
    }

    private void loadData() {
        if (mDatabaseHandler == null)
            mDatabaseHandler = new DatabaseHandler(this);

        String token = getIntent().getStringExtra(PassionAttendance.KEY_TOKEN);

        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        Boolean getStaff = false;

        Staff staff = mDatabaseHandler.retrieveStaff();
        if (staff == null || staff.getId() == -1) {
            headers.add("get_staff");
            getStaff = true;
        } else {
            // Update the list of events
            headers.add("get_events");
            ArrayList<Event> Events = mDatabaseHandler.retrieveEvents();
            if (Events.isEmpty())
                values.add("-1");
            else
                values.add(String.valueOf(Events.get(Events.size() - 1).getId()));

            // Update the list of messages
            headers.add("get_messages");
            ArrayList<Message> Messages = mDatabaseHandler.retrieveMessages();
            if (Messages.isEmpty())
                values.add("-1");
            else
                values.add(String.valueOf(Messages.get(Messages.size() - 1).getId()));

            // Update the list of messages
            headers.add("get_attendances");
            ArrayList<Attendance> Attendances = mDatabaseHandler.retrieveAttendances();
            if (Attendances.isEmpty())
                values.add("-1");
            else
                values.add(String.valueOf(Attendances.get(Attendances.size() - 1).getId()));

            // TODO Insert code for retrieving shifts
            /*
                Some code here
             */
        }

        // Send a GET request to the server to load data
        OkHttpClient httpClient = new OkHttpClient();
        String host = PassionAttendance.HOST;

        Request.Builder requestBuilder = new Request.Builder()
                .addHeader("Authorization", String.format("Token %s", token));

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .addPathSegment("api")
                .addPathSegment("get_data");

        if (getStaff) urlBuilder.addPathSegment("get_staff");
        else for (int i = 0; i < headers.size(); i++)
            urlBuilder.addQueryParameter(headers.get(i), values.get(i));

        HttpUrl url = urlBuilder.build();

        Request request = requestBuilder.url(url)
                .build();

        Object[] params = {httpClient, request};

        new LoadUserData().execute(params);
    }

    private void InitializeCalendar() {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_container, mCalendarView);
        t.commit();
    }

    private CalendarListener getCalendarListener() {
        if (mCalendarListener == null) {
            mCalendarListener = new CalendarListener() {
                @Override
                public void onViewCreated() {
                    Toast.makeText(OverviewActivity.this, "View Created", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChangeMonth(int month, int year) {
                    Toast.makeText(OverviewActivity.this, "Month Changed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSelectDate(LocalDate date, View view) {
                    DateTime Selected = DateTime.now();
                    Selected.withDate(date);

                    try {
                        NepaliDate selectedNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(date.toDate()),
                                currentNepaliDate = mCalendarView.getCalendarAdapter().getSelectedNepaliDate();
                        if (selectedNepaliDate.getMonthNumber() == currentNepaliDate.getMonthNumber()) {
                            int y = (int) mCalendarContainer.getY();
                            mOverViewScrollView.smoothScrollTo(0, y);
                            Log.i("SCROLL", "Scrolled to " + y);
                        }
                    } catch (NepaliDateException e1) {
                        e1.printStackTrace();
                    }

                    Toast.makeText(OverviewActivity.this, "Date Selected", Toast.LENGTH_SHORT).show();

                }
            };
        }
        return mCalendarListener;
    }

    private class LoadUserData extends AsyncTask<Object, Void, Boolean> {

        private String mResponse;

        @Override
        protected Boolean doInBackground(Object... params) {
            OkHttpClient httpClient = (OkHttpClient) params[0];
            Request request = (Request) params[1];

            try {
                Response response = httpClient.newCall(request).execute();
                mResponse = response.body().string();
            } catch (IOException e) {
                return false;
            }

            return true;
        }
    }
}
