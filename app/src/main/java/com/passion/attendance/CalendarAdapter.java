package com.passion.attendance;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Aayush on 11/27/2015.
 */
public class CalendarAdapter extends BaseAdapter {
    private LocalDate GregorianDate;

    private NepaliDate SelectedNepaliDate = null;

    private List<LocalDate> DateList;

    private Context context;

    private LocalDate mSelected;

    public CalendarAdapter(Context context, LocalDate SelectedDate) {
        this.context = context;

        this.DateList = new ArrayList<>();

        GregorianDate = DateTime.now().toLocalDate();

        try {
            SelectedNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(SelectedDate.toDate());
        } catch (NepaliDateException e) {
        }

        mSelected = SelectedDate;

        setCalendar(mSelected);
    }

    public CalendarAdapter(Context context) {
        this(context, DateTime.now().toLocalDate());
    }

    @Override
    public int getCount() {
        return DateList.size();
    }

    @Override
    public LocalDate getItem(int position) {
        return DateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(context);
            convertView = vi.inflate(R.layout.layout_calendar_cell, null);
        }

        TextView dateOfMonth = (TextView) convertView.findViewById(R.id.date_of_month);
        TextView altDateOfMonth = (TextView) convertView.findViewById(R.id.alternate_date_of_month);

        View calendarCell = convertView.findViewById(R.id.calendar_cell);

        dateOfMonth.setTextColor(Color.BLACK);

        View attendanceIndicator = convertView.findViewById(R.id.attendance_indicator),
                noticeIndicator = convertView.findViewById(R.id.notice_indicator),
                eventIndicator = convertView.findViewById(R.id.event_indicator),
                hudContainer = convertView.findViewById(R.id.hud_container);

        {
            Random rand = new Random();
            Boolean value1 = rand.nextBoolean(),
                    value2 = rand.nextBoolean(),
                    value3 = rand.nextBoolean();

            int VISIBLE = View.VISIBLE, GONE = View.GONE;

            if (value1) attendanceIndicator.setVisibility(VISIBLE);
            else attendanceIndicator.setVisibility(GONE);

            if (value2) noticeIndicator.setVisibility(VISIBLE);
            else noticeIndicator.setVisibility(GONE);

            if (value3) eventIndicator.setVisibility(VISIBLE);
            else eventIndicator.setVisibility(GONE);
        }

        int dayOfMonth = 0, altDayOfMonth;
        boolean currentMonth = false;

        try {
            NepaliDate currentDate = NepaliCalendar.convertGregorianToNepaliDate(DateList.get(position).toDate());

            dayOfMonth = currentDate.getDay();
            int month = currentDate.getMonthNumber();

            currentMonth = month == SelectedNepaliDate.getMonthNumber();
        } catch (NepaliDateException e) {
            e.printStackTrace();
        }

        DateTime cal = DateTime.now().withDate(DateList.get(position));

        altDayOfMonth = cal.getDayOfMonth();

        dateOfMonth.setText(String.valueOf(dayOfMonth));
        altDateOfMonth.setText(String.valueOf(altDayOfMonth));

        // Checking if current date is in current month
        if (currentMonth) {
            // If current date is in current month
            hudContainer.setVisibility(View.VISIBLE);

            dateOfMonth.setTextColor(ContextCompat.getColor(context, R.color.Black));
            altDateOfMonth.setTextColor(ContextCompat.getColor(context, R.color.Gray));
        } else {
            // If current date is not in current month
            hudContainer.setVisibility(View.INVISIBLE);

            dateOfMonth.setTextColor(ContextCompat.getColor(context, R.color.LightGrey));
            altDateOfMonth.setTextColor(ContextCompat.getColor(context, R.color.LightGrey));
        }
        // Checking if displayed date is today
        LocalDate today = DateTime.now().toLocalDate();
        if (today.equals(cal.toLocalDate())) {
            // If current date is today
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                calendarCell.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_cell_today_background));
            } else {
                calendarCell.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_cell_today_background));
            }
        } else {
            // If current date is not today
            // Checking if current date is selected
            if (mSelected.equals(cal.toLocalDate())) {
                // If current date is selected
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    calendarCell.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_cell_highlighted_background));
                } else {
                    calendarCell.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_cell_highlighted_background));
                }
            } else {
                // If current date is not selected
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    calendarCell.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_cell_background));
                } else {
                    calendarCell.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_cell_background));
                }
            }
        }
        return convertView;
    }

    public void setCalendar(LocalDate currentDate) {
        LocalDate tempGregorianDate = currentDate;
        NepaliDate tempNepaliDate;

        DateList.clear();

        try {
            tempNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(currentDate.toDate());
            SelectedNepaliDate = tempNepaliDate;

            tempNepaliDate = new NepaliDate(tempNepaliDate.getYear(), tempNepaliDate.getMonthNumber(), 1);

            tempGregorianDate = new LocalDate(NepaliCalendar.convertNepaliToGregorianDate(tempNepaliDate));

            int currentMonth = tempNepaliDate.getMonthNumber(),
                    currentYear = tempNepaliDate.getYear();

            tempGregorianDate = tempGregorianDate.minusDays(tempGregorianDate.getDayOfWeek());
            tempNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(tempGregorianDate.toDate());

            while ((currentYear == tempNepaliDate.getYear() && currentMonth >= tempNepaliDate.getMonthNumber()) ||
                    (currentYear > tempNepaliDate.getYear() && currentMonth == 1)) {
                DateList.add(tempGregorianDate);

                tempGregorianDate = tempGregorianDate.plusDays(1);
                tempNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(tempGregorianDate.toDate());

            }

            if (tempGregorianDate.getDayOfWeek() != 0)
                for (int i = tempGregorianDate.getDayOfWeek(); i <= 6; i++) {
                    DateList.add(tempGregorianDate);

                    tempGregorianDate = tempGregorianDate.plusDays(1);
                }

        } catch (NepaliDateException e) {
            e.printStackTrace();
        }
    }

    public void setCalendar(NepaliDate currentDate) {
        setCalendar(new LocalDate(NepaliCalendar.convertNepaliToGregorianDate(currentDate)));
    }

    public NepaliDate getSelectedNepaliDate() {
        return SelectedNepaliDate;
    }

    public void setSelected(LocalDate selected) {
        this.mSelected = selected;
    }
}