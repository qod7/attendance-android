package com.passion.attendance;

import android.view.View;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by Aayush on 1/10/2016.
 */
public abstract class CalendarListener {
    public abstract void onSelectDate(LocalDate date, View view);


    /**
     * Inform client user has long clicked on a date
     *
     * @param date
     * @param view
     */
    public void onLongClickDate(Date date, View view) {
        // Do nothing
    }


    /**
     * Inform client that calendar has changed month
     *
     * @param month
     * @param year
     */
    public void onChangeMonth(int month, int year) {
        // Do nothing
    }


    /**
     * Inform client that CaldroidFragment view has been created and views are
     * no longer null. Useful for customization of button and text views
     */
    public void onViewCreated() {
        // Do nothing
    }
}
