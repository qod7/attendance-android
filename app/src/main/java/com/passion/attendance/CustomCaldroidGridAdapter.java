package com.passion.attendance;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;
import java.util.Random;

import hirondelle.date4j.DateTime;

public class CustomCaldroidGridAdapter extends CaldroidGridAdapter {

    public CustomCaldroidGridAdapter(Context context, int month, int year,
                                     HashMap<String, Object> caldroidData,
                                     HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        View convertView = convertView;

        // For reuse
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_calendar_cell, null);
        }

        int topPadding = convertView.getPaddingTop();
        int leftPadding = convertView.getPaddingLeft();
        int bottomPadding = convertView.getPaddingBottom();
        int rightPadding = convertView.getPaddingRight();

        TextView DateOfMonth = (TextView) convertView.findViewById(R.id.date_of_month);
        TextView AltDateOfMonth = (TextView) convertView.findViewById(R.id.alternate_date_of_month);

        DateOfMonth.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            DateOfMonth.setTextColor(Color.GRAY);
        }

        {
            Random rand = new Random();
            Boolean value1 = rand.nextBoolean(),
                    value2 = rand.nextBoolean(),
                    value3 = rand.nextBoolean();

            View AttendanceIndicator = convertView.findViewById(R.id.attendance_indicator),
                    NoticeIndicator = convertView.findViewById(R.id.notice_indicator),
                    EventIndicator = convertView.findViewById(R.id.event_indicator);

            int VISIBLE = View.VISIBLE, GONE = View.GONE;

            if (value1) AttendanceIndicator.setVisibility(VISIBLE);
            else AttendanceIndicator.setVisibility(GONE);

            if (value2) NoticeIndicator.setVisibility(VISIBLE);
            else NoticeIndicator.setVisibility(GONE);

            if (value3)
                EventIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.Transparent));
            else EventIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.Orange));
        }

        boolean shouldResetDisabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            DateOfMonth.setTextColor(Color.GRAY);
            convertView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
        } else {
            shouldResetDisabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            convertView.setBackgroundColor(resources
                    .getColor(R.color.LightBlue));

            DateOfMonth.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDisabledView && shouldResetSelectedView) {
            // Customize for today
            convertView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
        }

        DateOfMonth.setText(dateTime.getDay().toString());
        AltDateOfMonth.setText(dateTime.getDay().toString());
        //AbsentIndicator.setColorFilter(cellView.getResources().getColor(R.color.Orange));

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        convertView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
//        setCustomResources(dateTime, convertView, DateOfMonth);

        return convertView;
    }
}
