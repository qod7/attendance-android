package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Created by Aayush on 3/14/2016.
 */
public class TimeRange implements Serializable {
    LocalTime from;
    LocalTime to;

    public TimeRange(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public TimeRange(String timeRangeString) throws InvalidParameterException {
        String[] stringArray = timeRangeString.split("\n");

        this.from = new LocalTime(stringArray[0]);
        this.to = new LocalTime(stringArray[1]);
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    @Override
    public String toString() {
        JSONObject j = new JSONObject();

        try {
            j.put(PassionAttendance.KEY_FROM, this.from.toString());
            j.put(PassionAttendance.KEY_TO, this.to.toString());
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast TimeRange to String");
        }

        return j.toString();
    }
}
