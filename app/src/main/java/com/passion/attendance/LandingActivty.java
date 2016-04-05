package com.passion.attendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

public class LandingActivty extends AppCompatActivity implements View.OnClickListener {

    private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button GetStartedButton = (Button) findViewById(R.id.get_started_button);

        if (BuildConfig.DEBUG) {
            runTest();
//            startActivtyWithDummyCredentials();
        }

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        GetStartedButton.setOnClickListener(this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Google Play Services not found")
                        .setMessage("Please install Google Play Servies to continue")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
                Log.i("LandingActivity", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    private void runTest() {
        LocalTime nowUTC = LocalTime.now(DateTimeZone.UTC);
        LocalTime now = LocalTime.now();

        long millis = nowUTC.getMillisOfDay();

        long diff = now.getMillisOfDay() - nowUTC.getMillisOfDay();

        Log.i("Time Zone", "Offset: " + diff);
        Log.i("Time", "UTC: " + new LocalTime(millis, DateTimeZone.UTC));
        Log.i("Time", "Local: " + new LocalTime(millis + diff, DateTimeZone.UTC));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("ACTIVITY CLOSED", requestCode + ", " + resultCode);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.get_started_button:
                startActivityForResult(new Intent(this, LoginActivity.class), PassionAttendance.ACTIVTY_LOGIN);
                break;
        }
    }

    private void startActivtyWithDummyCredentials() {
        Intent intent = new Intent(LandingActivty.this, OverviewActivity.class);

        intent.putExtra(PassionAttendance.KEY_USER, PassionAttendance.DUMMY_EMAIL);
        intent.putExtra(PassionAttendance.KEY_PASSWORD, PassionAttendance.DUMMY_PASSWORD);
        intent.putExtra(PassionAttendance.KEY_TOKEN, PassionAttendance.DUMMY_PASSWORD);

        startActivityForResult(intent, PassionAttendance.ACTIVTY_OVERVIEW);
    }
}
