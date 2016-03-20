package com.passion.attendance;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Aayush on 12/9/2015.
 */
public class PassionAttendance extends Application {
    public static final String PACKAGE_NAME = "com.passion.attendance";
    public static final String PREFERENCE_STAFF = "staff";

    public static final String DUMMY_EMAIL = "aayush@subedi.com";
    public static final String DUMMY_PASSWORD = "harikaji";
    public static final String DUMMY_TOKEN = "mujiharikaji";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ORGANIZATION = "organization";
    public static final String KEY_IMAGE_URL = "image";
    public static final String KEY_CONTACT_NUMBER = "contact";
    public static final String KEY_SHIFT = "shift";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_PREFERENCES = "preferences";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SENT = "sent";
    public static final String KEY_DATE = "date";
    public static final String KEY_IS_PRESENT = "is_present";

    public static final int ACTIVTY_LOGIN = 0;
    public static final int ACTIVTY_OVERVIEW = 1;

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER = "user";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_YEAR = "year";
    public static final String KEY_MONTH = "month";
    public static final String KEY_DAY = "day";

    public static final String HOST = "www.eattendance.com";

    public static String getStringFromArray(ArrayList<String> shifts) {
        StringBuilder sb = new StringBuilder();
        for (String shift : shifts) {
            sb.append(shift);
            sb.append('\n');
        }
        return sb.toString();
    }

    public static ArrayList<String> getArrayFromString(String shifts) {
        String[] op = shifts.split("\\r?\\n");

        ArrayList<String> output = new ArrayList<>();
        Collections.addAll(output, op);

        return output;
    }

    public static String getStringFromMap(HashMap<String, String> extras) {
        return new JSONObject(extras).toString();
    }

    public static HashMap<String, String> getMapFromString(String extras) {
        try {
            HashMap<String, String> map = new HashMap<>();
            JSONObject json = new JSONObject(extras);

            Iterator<?> keys = json.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = json.getString(key);
                map.put(key, value);
            }
            return map;
        } catch (JSONException e){
            return new HashMap<>();
        }
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
    }

}
