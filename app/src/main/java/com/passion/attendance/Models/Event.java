package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aayush on 1/2/2016.
 */
public class Event {

    Integer id;
    String title;
    String description;
    LocalDate from;
    LocalDate to;

    public Event(Integer id, String title, String description, LocalDate from) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = from;
    }

    public Event(Integer id, String title, String description, LocalDate from, LocalDate to) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = to;
    }

    public Event(String event) {
        try {
            JSONObject j = new JSONObject(event);

            this.id = j.getInt(PassionAttendance.KEY_ID);
            this.title = j.getString(PassionAttendance.KEY_TITLE);
            this.description = j.getString(PassionAttendance.KEY_DESCRIPTION);
            this.from = new LocalDate(j.getString(PassionAttendance.KEY_FROM));
            this.to = new LocalDate(j.getString(PassionAttendance.KEY_TO));
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast String to Event");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }
}
