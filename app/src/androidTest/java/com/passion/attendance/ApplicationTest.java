package com.passion.attendance;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.passion.attendance.Models.TimeRange;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testGetStringFromTimeRangeMap() throws Exception {
        HashMap<TimeRange, Boolean> extras = new HashMap<>();
        String output = new JSONObject(extras).toString();
        Log.i("String Hashmap", output);
    }
}