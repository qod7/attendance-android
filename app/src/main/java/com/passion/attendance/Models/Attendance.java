package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aayush on 3/8/2016.
 */
public class Attendance {

    Integer id;
    LocalDate date;
    LocalTime from;
    LocalTime to;
    Boolean presence;

    public Attendance(Integer id, LocalDate date, LocalTime from, LocalTime to, Boolean presence) {
        this.id = id;
        this.date = date;
        this.from = from;
        this.to = to;
        this.presence = presence;
    }

    public Attendance(String attendance) {
        try {
            JSONObject j = new JSONObject(attendance);
            this.id = j.getInt(PassionAttendance.KEY_ID);
            this.presence = j.getBoolean(PassionAttendance.KEY_IS_PRESENT);
            try {
                this.date = new LocalDate(j.getInt(PassionAttendance.KEY_DATE));
            } catch (NumberFormatException e) {
                this.date = new LocalDate(j.getString(PassionAttendance.KEY_DATE));
            }
            try {
                this.from = new LocalTime(
                        j.getInt(PassionAttendance.KEY_FROM) + PassionAttendance.TIME_OFFSET,
                        DateTimeZone.UTC
                );
            } catch (NumberFormatException e) {
                this.from = new LocalTime(j.getString(PassionAttendance.KEY_FROM));
            }
            try {
                this.to = new LocalTime(
                        j.getInt(PassionAttendance.KEY_TO) + PassionAttendance.TIME_OFFSET,
                        DateTimeZone.UTC
                );
            } catch (NumberFormatException e) {
                this.to = new LocalTime(j.getString(PassionAttendance.KEY_TO));
            }
        } catch (JSONException e)

        {
            throw new ClassCastException("Could not cast String to Attendance");
        }

    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getFrom() {
        return from;
    }

    public LocalTime getTo() {
        return to;
    }

    public Boolean getIsPresenct() {
        return presence;
    }

    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        try {
            j.put(PassionAttendance.KEY_ID, this.id);
            j.put(PassionAttendance.KEY_DATE, this.date.toDate().getTime());
            j.put(PassionAttendance.KEY_FROM, this.from.getMillisOfDay());
            j.put(PassionAttendance.KEY_TO, this.to.getMillisOfDay());
            j.put(PassionAttendance.KEY_IS_PRESENT, this.presence);
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast Attendance to String");
        }
        return j.toString();
    }

    public static Attendance getDummyAttendance(){
        return new Attendance(
                1,
                LocalDate.now(),
                LocalTime.now().minusHours(2),
                LocalTime.now().minusHours(1),
                true
        );
    }
}
