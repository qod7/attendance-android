package com.passion.attendance.Models;

import java.util.Date;

/**
 * Created by Aayush on 3/14/2016.
 */
public class TimeTable {
    Date from;
    Date to;

    public TimeTable(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
