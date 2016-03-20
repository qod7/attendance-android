package com.passion.attendance.Models;

import org.joda.time.LocalDate;

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
