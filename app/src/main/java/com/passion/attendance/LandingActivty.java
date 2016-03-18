package com.passion.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LandingActivty extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button GetStartedButton = (Button) findViewById(R.id.get_started_button);

        GetStartedButton.setOnClickListener(this);
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

        switch (id){
            case R.id.get_started_button:
                startActivityForResult(new Intent(this, LoginActivity.class), PassionAttendance.ACTIVTY_LOGIN);
                break;
        }
    }
}
