package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/14/2016.
 */
public class Shift {

    public static final String[] days = {
            "SUNDAY",
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY"
    };

    ArrayList<TimeRangeList> timeRangeLists;

    public Shift(ArrayList<TimeRangeList> timeRangeLists) {
        this.timeRangeLists = timeRangeLists;
    }

    public ArrayList<TimeRangeList> getTimeRangeLists() {
        return timeRangeLists;
    }

    public TimeRangeList getTimeRangeList(int day){
        return timeRangeLists.get(day);
    }

    public Shift(String shift) {
        try {
            JSONObject j = new JSONObject(shift);

            JSONArray jsonArray = j.getJSONArray(PassionAttendance.KEY_SHIFT);

            ArrayList<TimeRangeList> t = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                t.add(new TimeRangeList((String) jsonArray.get(i)));

            this.timeRangeLists = t;

        } catch (JSONException e) {
            throw new ClassCastException("Could not cast String to Shift");
        }
    }

    @Override
    public String toString() {
        JSONArray jsonArray = new JSONArray();
        for (TimeRangeList t : timeRangeLists)
            jsonArray.put(t.toString());

        JSONObject j = new JSONObject();
        try {
            j.put(PassionAttendance.KEY_SHIFT, jsonArray);
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast Shift to String");
        }

        return j.toString();
    }

    public static Shift getDummyShift() {

        ArrayList<TimeRangeList> T = new ArrayList<>();
        TimeRangeList t = new TimeRangeList();

        t.add(new TimeRange(LocalTime.now(), LocalTime.now().plusHours(1)));
        T.add((TimeRangeList) t.clone());

        t.add(new TimeRange(LocalTime.now().plusHours(2), LocalTime.now().plusHours(3)));
        T.add((TimeRangeList) t.clone());

        t.add(new TimeRange(LocalTime.now().plusHours(4), LocalTime.now().plusHours(5)));
        T.add((TimeRangeList) t.clone());

        t.add(new TimeRange(LocalTime.now().plusHours(6), LocalTime.now().plusHours(7)));
        T.add((TimeRangeList) t.clone());

        t.add(new TimeRange(LocalTime.now().plusHours(7).plusMinutes(30), LocalTime.now().plusHours(8)));
        T.add((TimeRangeList) t.clone());

        t.add(new TimeRange(LocalTime.now().plusHours(1).plusMinutes(30), LocalTime.now().plusHours(2)));
        T.add((TimeRangeList) t.clone());

        t.add(new TimeRange(LocalTime.now().plusHours(2).plusMinutes(30), LocalTime.now().plusHours(3)));
        T.add((TimeRangeList) t.clone());

        return new Shift(T);
    }


}
