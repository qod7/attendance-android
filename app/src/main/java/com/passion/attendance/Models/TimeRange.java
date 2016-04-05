package com.passion.attendance.Models;

import android.util.Log;

import com.passion.attendance.PassionAttendance;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

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

    public TimeRange(String timeRangeString) {
        try {
            JSONObject t = new JSONObject(timeRangeString);

            try {
                this.from = new LocalTime(
                        t.getInt(PassionAttendance.KEY_FROM) + PassionAttendance.TIME_OFFSET,
                        DateTimeZone.UTC
                );
            } catch (JSONException e) {
                this.from = new LocalTime(t.getString(PassionAttendance.KEY_FROM));
            }

            try {
                this.to = new LocalTime(
                        t.getInt(PassionAttendance.KEY_TO) + PassionAttendance.TIME_OFFSET,
                        DateTimeZone.UTC
                );
            } catch (JSONException e) {
                this.to = new LocalTime(t.getString(PassionAttendance.KEY_TO));
            }
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast String to TimeRange: " + timeRangeString);
        }
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
