package com.passion.attendance.Models;

import org.joda.time.LocalDate;

/**
 * Created by Aayush on 3/8/2016.
 */
public class Attendance {

    Integer id;
    LocalDate date;
    Boolean isPresent;

    public Attendance(Integer id, LocalDate date, Boolean isPresent) {
        this.id = id;
        this.date = date;
        this.isPresent = isPresent;
    }

    public Attendance(Integer id, LocalDate date) {
        this.id = id;
        this.date = date;
        this.isPresent = null;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Boolean getAttendance() {
        return isPresent;
    }
}
