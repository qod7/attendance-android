package com.passion.attendance.Models;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/14/2016.
 */
public class Shift {
    ArrayList<TimeTable> timeTables;

    public final String[] days = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    public Shift(ArrayList<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public String getDay(Integer i) {
        return days[i];
    }

    public ArrayList<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(ArrayList<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public void setTimeTable(Integer index, TimeTable timeTable){
        this.timeTables.add(index, timeTable);
    }

    public TimeTable getTimeTable(Integer index){
        return this.timeTables.get(index);
    }
}
