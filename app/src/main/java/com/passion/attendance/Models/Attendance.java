package com.passion.attendance.Models;

import java.util.Date;

/**
 * Created by Aayush on 3/8/2016.
 */
public class Attendance {

    Integer id;
    Date date;
    Boolean isPresent;

    public Attendance(Integer id, Date date, Boolean isPresent) {
        this.id = id;
        this.date = date;
        this.isPresent = isPresent;
    }

    public Attendance(Integer id, Date date) {
        this.id = id;
        this.date = date;
        this.isPresent = null;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Boolean getAttendance() {
        return isPresent;
    }
}
