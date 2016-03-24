package com.passion.attendance.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Aayush on 3/14/2016.
 */
public class Shift {

    public final String[] days = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };
    ArrayList<TimeRange> timeRanges;

    public Shift(ArrayList<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public Shift(String shift) {
        try {
            JSONObject j = new JSONObject(shift);

            Iterator<String> k = j.keys();

            while (k.hasNext())
                timeRanges.add(new TimeRange(j.getString(k.next())));

        } catch (JSONException e) {
            throw new ClassCastException("Could not cast String to TimeRange");
        }
    }

    public String getDay(Integer i) {
        return days[i];
    }

    public ArrayList<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public TimeRange getTimeRange(Integer index) {
        return timeRanges.get(index);
    }

    @Override
    public String toString() {

        ArrayList<String> strings = new ArrayList<>();
        JSONObject j = new JSONObject();
        try {
            for (int i = 0; i < this.timeRanges.size(); i++)
                j.put(String.valueOf(i), this.timeRanges.get(i));
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast Shift to String");
        }

        return j.toString();
    }
}
