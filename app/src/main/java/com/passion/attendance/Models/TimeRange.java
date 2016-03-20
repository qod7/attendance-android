package com.passion.attendance.Models;

import org.joda.time.LocalTime;

/**
 * Created by Aayush on 3/14/2016.
 */
public class TimeRange {
    LocalTime from;
    LocalTime to;

    public TimeRange(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
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
}
