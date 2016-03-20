package com.passion.attendance.Models;

import org.joda.time.LocalDate;

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
