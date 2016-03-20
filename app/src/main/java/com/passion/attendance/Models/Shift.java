package com.passion.attendance.Models;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/14/2016.
 */
public class Shift {
    ArrayList<TimeRange> timeRanges;

    public final String[] days = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    public Shift(ArrayList<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public String getDay(Integer i) {
        return days[i];
    }

    public ArrayList<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(ArrayList<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public void setTimeTable(Integer index, TimeRange timeRange){
        this.timeRanges.add(index, timeRange);
    }

    public TimeRange getTimeTable(Integer index){
        return this.timeRanges.get(index);
    }
}
