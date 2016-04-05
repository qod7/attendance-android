package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Aayush on 3/28/2016.
 */
public class TimeRangeList extends ArrayList<TimeRange> {
    public TimeRangeList(int capacity) {
        super(capacity);
    }

    public TimeRangeList() {
        super();
    }

    public TimeRangeList(Collection<? extends TimeRange> collection) {
        super(collection);
    }

    public TimeRangeList(String timeRangeList){
        super();

        try{
            JSONObject j = new JSONObject(timeRangeList);

            JSONArray jsonArray = j.getJSONArray(PassionAttendance.KEY_TIME_RANGE);

            for(int i = 0; i < jsonArray.length(); i++)
                add(new TimeRange((String) jsonArray.get(i)));

        } catch (JSONException e){
            throw new ClassCastException("Could not cast String to TimeRangeList");
        }

    }

    @Override
    public String toString() {
        JSONArray jsonArray = new JSONArray();
        for (TimeRange t : this) {
            jsonArray.put(t.toString());
        }

        JSONObject j = new JSONObject();
        try {
            j.put(PassionAttendance.KEY_TIME_RANGE, jsonArray);
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast TimeRangeList to String");
        }

        return j.toString();
    }
}
