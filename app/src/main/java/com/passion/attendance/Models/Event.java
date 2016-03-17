package com.passion.attendance.Models;

import java.util.Date;

/**
 * Created by Aayush on 1/2/2016.
 */
public class Event {

    Integer id;
    String title;
    String description;
    Date from;
    Date to;

    public Event(Integer id, String title, String description, Date from) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = from;
    }

    public Event(Integer id, String title, String description, Date from, Date to) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = to;
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

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }
}
