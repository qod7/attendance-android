package com.passion.attendance.Models;

import com.passion.attendance.PassionAttendance;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aayush on 3/8/2016.
 */
public class Message {

    public static final int SENT = 0, RECEIVED = 1;

    Integer id;
    String title;
    String content;
    Integer type;
    LocalDate sentOn;

    public Message(Integer id, String title, String content, LocalDate sentOn) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = RECEIVED;
        this.sentOn = sentOn;
    }

    public Message(Integer id, String title, String content, Integer type, LocalDate sentOn) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.sentOn = sentOn;
    }

    public Message(String message) {
        try {
            JSONObject m = new JSONObject(message);

            this.id = m.getInt(PassionAttendance.KEY_ID);
            this.title = m.getString(PassionAttendance.KEY_TITLE);
            this.content = m.getString(PassionAttendance.KEY_CONTENT);
            this.type = m.getInt(PassionAttendance.KEY_TYPE);
            this.sentOn = new LocalDate(m.getString(PassionAttendance.KEY_SENT));
        } catch (JSONException e) {
            throw new ClassCastException("Could not cast String to Message");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getType() {
        return type;
    }

    public LocalDate getSentOn() {
        return sentOn;
    }
}
