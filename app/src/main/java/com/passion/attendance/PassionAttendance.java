package com.passion.attendance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Aayush on 12/9/2015.
 */
public class PassionAttendance {
    /**
     * @param id
     * @param name
     * @param organization
     * @param imageUrl
     * @param contactNumber
     * @param shift
     * @param extras
     * @param preferences
     */

    public static final String PACKAGE_NAME = "com.passion.attendance";
    public static final String PREFERENCE_STAFF = "staff";

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
            HashMap<String, String> map = new HashMap<String, String>();
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

}
