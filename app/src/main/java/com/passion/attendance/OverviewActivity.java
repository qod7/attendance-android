package com.passion.attendance;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.passion.attendance.Models.Attendance;
import com.passion.attendance.Models.Event;
import com.passion.attendance.Models.Message;
import com.passion.attendance.Models.Shift;
import com.passion.attendance.Models.Staff;
import com.squareup.picasso.Picasso;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OverviewActivity extends AppCompatActivity {

    CalendarFragment mCalendarFragment;
    OverviewFragment mOverviewFragment;

    View mCalendarContainer;
    View mOverviewContainer;

    ViewGroup mOverViewView;
    NestedScrollView mOverViewScrollView;

    DatabaseHandler mDatabaseHandler;

    BroadcastReceiver mBroadcastReceiver;

    private CalendarListener mCalendarListener;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mProfileImageView;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mOverViewView = (ViewGroup) findViewById(R.id.overview_container);
        mOverViewScrollView = (NestedScrollView) findViewById(R.id.overview_scrollview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mProfileImageView = (ImageView) findViewById(R.id.profile_image);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    mCalendarFragment.getCalendarAdapter().notifyDataSetChanged();
                } catch (NullPointerException e) {
                    Log.e("Calendar", "Calendar not initialized");
                }
            }
        };

        setSupportActionBar(mToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);

        mCalendarContainer = findViewById(R.id.calendar_container);
        mOverviewContainer = findViewById(R.id.detail_container);

        InitializeCalendar();

        mCalendarFragment.setCalendarListener(getCalendarListener());

        loadData(true);

        if (BuildConfig.DEBUG) loadDummyNotification();
    }

    private void loadDummyNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        TaskStackBuilder stackBuilder;
        PendingIntent pendingIntent;
        ArrayList<String> details = new ArrayList<>();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        Message message = Message.getDummyMessage();

        //Adding message information to notification
        notificationBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        notificationBuilder.setContentTitle(message.getTitle());
        notificationBuilder.setContentText(message.getContent());

        // Setting up action for notification
        Intent messageIntent = new Intent(this, MessagesActivity.class);
        messageIntent.putExtra(
                PassionAttendance.KEY_SOURCE,
                PassionAttendance.KEY_NOTIFICATION
        );
        messageIntent.putExtra(
                PassionAttendance.KEY_MESSAGE,
                message.toString()
        );

        // Setting up stack for notification action
        stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MessagesActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(messageIntent);

        pendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        notificationBuilder.setContentIntent(pendingIntent);

        // Adding details to message
        inboxStyle.setBigContentTitle("Message details");
        details.add("Sent: " + message.getSentOn().toString());

        for (String detail : details) inboxStyle.addLine(detail);

        notificationBuilder.setStyle(inboxStyle);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(
                0,
                notificationBuilder.build()
        );
    }

    private void loadData(Boolean silence) {
        if (mDatabaseHandler == null)
            mDatabaseHandler = new DatabaseHandler(this);

        String token = getIntent().getStringExtra(PassionAttendance.KEY_TOKEN);

        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        Boolean getStaff = false;

        Staff staff = mDatabaseHandler.retrieveStaff();
        headers.add("get_staff");
        if (staff == null || staff.getId() == -1) {
            values.add("-1");
            getStaff = true;
        } else {
            values.add(String.valueOf(staff.getId()));
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
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new CurlLoggingInterceptor())
                .build();

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

        new LoadUserData(this, silence).execute(params);
    }

    private void InitializeCalendar() {
        Bundle args = new Bundle();

        args.putBoolean(CalendarFragment.START_WITH_NEPALI_DATE, true);
        args.putInt(CalendarFragment.START_MONTH, 7);
        args.putInt(CalendarFragment.START_DAY, 1);
        args.putInt(CalendarFragment.START_YEAR, 2072);

        mCalendarFragment = CalendarFragment.newInstance();
//        mCalendarFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_container, mCalendarFragment);
        t.commit();
    }

    private CalendarListener getCalendarListener() {
        if (mCalendarListener == null) {
            mCalendarListener = new CalendarListener() {
                @Override
                public void onSelectDate(LocalDate date, View view) {
                    DateTime Selected = DateTime.now();
                    Selected.withDate(date);

                    try {
                        NepaliDate selectedNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(date.toDate()),
                                currentNepaliDate = mCalendarFragment.getCalendarAdapter().getSelectedNepaliDate();
                        if (selectedNepaliDate.getMonthNumber() == currentNepaliDate.getMonthNumber()) {
                            if (mOverviewFragment != null) {
                                Bundle args = mOverviewFragment.getArguments();
                                args.putInt(PassionAttendance.KEY_YEAR, date.getYear());
                                args.putInt(PassionAttendance.KEY_MONTH, date.getMonthOfYear());
                                args.putInt(PassionAttendance.KEY_DAY, date.getDayOfMonth());
                                args.putBoolean(PassionAttendance.KEY_TODAY, false);

                                Log.i("RandomColor", String.valueOf(ColorList.getRandomColor(OverviewActivity.this)));

                                mOverviewFragment.setTodayVisible(false);
                            }

                            mAppBarLayout.setExpanded(false);
                        }
                    } catch (NepaliDateException e1) {
                        e1.printStackTrace();
                    }
                }
            };
        }
        return mCalendarListener;
    }

    private class LoadUserData extends AsyncTask<Object, Void, Boolean> {

        private String mResponse;
        ProgressDialog progressDialog;
        private Context mContext;
        boolean mSilence;

        public LoadUserData(Context context, Boolean silence) {
            mContext = context;
            mSilence = silence;
        }

        @Override
        protected void onPreExecute() {
            if (!mSilence) {
                progressDialog = new ProgressDialog(mContext, android.R.style.Theme_DeviceDefault_Light);
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Syncing data with server");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                JSONObject response;
                Boolean status;
                try {
                    response = new JSONObject(mResponse);
                    try {
                        status = response.getBoolean(PassionAttendance.KEY_STATUS);
                    } catch (JSONException e) {
                        Log.e("HTTP", "Server Error");
                    }
                    try {
                        JSONObject staff = response.getJSONObject(PassionAttendance.KEY_STAFF);
                        mDatabaseHandler.insertStaff(new Staff(staff.toString()));
                    } catch (JSONException e) {
                        Log.i("HTTP", "Staff object not found");
                    }
                    try {
                        JSONArray messages = response.getJSONArray(PassionAttendance.KEY_MESSAGE);
                        ArrayList<Message> m = new ArrayList<>();
                        for (int i = 0; i < messages.length(); i++) {
                            m.add(new Message(messages.getJSONObject(i).toString()));
                        }
                        mDatabaseHandler.insertMessage(m);
                    } catch (JSONException e) {
                        Log.i("HTTP", "Message object not found");
                    }
                    try {
                        JSONArray events = response.getJSONArray(PassionAttendance.KEY_EVENT);
                        ArrayList<Event> e = new ArrayList<>();
                        for (int i = 0; i < events.length(); i++) {
                            e.add(new Event(events.getJSONObject(i).toString()));
                        }
                        mDatabaseHandler.insertEvent(e);
                    } catch (JSONException e) {
                        Log.i("HTTP", "Event object not found");
                    }
                    try {
                        JSONArray attendance = response.getJSONArray(PassionAttendance.KEY_ATTENDANCE);
                        ArrayList<Attendance> a = new ArrayList<>();
                        for (int i = 0; i < attendance.length(); i++) {
                            a.add(new Attendance(attendance.getJSONObject(i).toString()));
                        }
                        mDatabaseHandler.insertAttendance(a);
                    } catch (JSONException e) {
                        Log.i("HTTP", "Attendance object not found");
                    }
                    try {
                        JSONObject shift = response.getJSONObject(PassionAttendance.KEY_SHIFT);
                        mDatabaseHandler.insertShift(new Shift(shift.toString()));
                    } catch (JSONException e) {
                        Log.i("HTTP", "Shift object not found");
                    }
                } catch (JSONException e) {
                }
            }
            if (BuildConfig.DEBUG) {
                mDatabaseHandler.insertStaff(Staff.getDummyStaff());
                mDatabaseHandler.insertShift(Shift.getDummyShift());
                mDatabaseHandler.insertEvent(Event.getDummyEvent());
                mDatabaseHandler.insertMessage(Message.getDummyMessage());
                mDatabaseHandler.insertAttendance(Attendance.getDummyAttendance());
            }

            if (!mSilence) progressDialog.dismiss();

            updateViews();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DatabaseHandler db;

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_messages:
                startActivity(new Intent(this, MessagesActivity.class));
                return true;
            case R.id.menu_events:
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            case R.id.menu_shifts:
                startActivity(new Intent(this, ShiftActivity.class));
                return true;
            case R.id.menu_sync:
                db = new DatabaseHandler(this);
                db.clearDatabase();
                loadData(false);
                return true;
            case R.id.menu_logout:
                AlertDialog d = new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHandler db = new DatabaseHandler(OverviewActivity.this);
                                db.clearDatabase();

                                setResult(PassionAttendance.ACTION_LOGOUT);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateViews() {
        Staff staff = Staff.getDummyStaff();
        mCollapsingToolbarLayout.setTitle(staff.getName());
        mToolbar.setTitle(staff.getName());

        int width = mProfileImageView.getMeasuredWidth();
        int height = mProfileImageView.getMeasuredHeight();

        Picasso.with(this)
                .load(staff.getImageUrl())
                .centerInside()
                .resize(width, height)
                .placeholder(R.drawable.user_placeholder)
                .into(mProfileImageView);

        ProgressBar p = (ProgressBar) findViewById(R.id.overview_progress);
        p.setVisibility(View.GONE);

        mOverviewFragment = OverviewFragment.newInstance();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.detail_container, mOverviewFragment);
        t.commit();

        mCalendarFragment.getCalendarAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter i = new IntentFilter();
        i.addAction(PassionAttendance.ACTION_REMOVE_ITEM);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, i);

        mCalendarFragment.getCalendarAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
