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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Aayush on 11/27/2015.
 */
public class CalendarAdapter extends BaseAdapter {
    private Calendar GregorianCalendar;

    private NepaliDate SelectedNepaliDate = null;
    private NepaliDate SelectedDate = null;

    private List<Date> DateList;

    private Context context;

    private Date mSelected;

    public CalendarAdapter(Context context, Date SelectedDate) {
        this.context = context;

        this.DateList = new ArrayList<>();

        GregorianCalendar = Calendar.getInstance();

        try {
            SelectedNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(SelectedDate);
        } catch (NepaliDateException e) {
        }

        mSelected = SelectedDate;

        setCalendar(mSelected);
    }

    public CalendarAdapter(Context context) {
        this(context, Calendar.getInstance().getTime());
    }

    @Override
    public int getCount() {
        return DateList.size();
    }

    @Override
    public Date getItem(int position) {
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

        TextView DateOfMonth = (TextView) convertView.findViewById(R.id.date_of_month);
        TextView AltDateOfMonth = (TextView) convertView.findViewById(R.id.alternate_date_of_month);

        DateOfMonth.setTextColor(Color.BLACK);

        View AttendanceIndicator = convertView.findViewById(R.id.attendance_indicator),
                NoticeIndicator = convertView.findViewById(R.id.notice_indicator),
                EventIndicator = convertView.findViewById(R.id.event_indicator);

        {
            Random rand = new Random();
            Boolean value1 = rand.nextBoolean(),
                    value2 = rand.nextBoolean(),
                    value3 = rand.nextBoolean();

            int VISIBLE = View.VISIBLE, GONE = View.GONE;

            if (value1) AttendanceIndicator.setVisibility(VISIBLE);
            else AttendanceIndicator.setVisibility(GONE);

            if (value2) NoticeIndicator.setVisibility(VISIBLE);
            else NoticeIndicator.setVisibility(GONE);

            if (value3)
                EventIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.Transparent));
            else EventIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.Orange));
        }

        int dayOfMonth = 0, altdayOfMonth = 0;
        boolean currentMonth = false;

        try {
            NepaliDate currentDate = NepaliCalendar.convertGregorianToNepaliDate(DateList.get(position));

            dayOfMonth = currentDate.getDay();
            int month = currentDate.getMonthNumber();

            currentMonth = month == SelectedNepaliDate.getMonthNumber();
        } catch (NepaliDateException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateList.get(position));

        altdayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        DateOfMonth.setText(String.valueOf(dayOfMonth));
        AltDateOfMonth.setText(String.valueOf(altdayOfMonth));

        View Background = convertView.findViewById(R.id.calendar_cell_background);
        if (currentMonth) {
            Calendar today = Calendar.getInstance();
            if (today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                    && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_cell_today_background));
                } else {
                    convertView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_cell_today_background));
                }
            } else {
                Calendar selected = Calendar.getInstance();
                selected.setTime(mSelected);
                if (!(selected.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                        && selected.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                        && selected.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_cell_background));
                    } else {
                        convertView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_cell_background));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_cell_highlighted_background));
                    } else {
                        convertView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_cell_highlighted_background));
                    }
                }
            }
            EventIndicator.setVisibility(View.VISIBLE);
        } else {
            Background.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray));
            EventIndicator.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setCalendar(Date currentDate) {
        Calendar tempGregorianCalendar = GregorianCalendar;
        tempGregorianCalendar.setTime(currentDate);

        NepaliDate tempNepaliDate;

        DateList.clear();

        try {
            tempNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(tempGregorianCalendar.getTime());
            SelectedNepaliDate = tempNepaliDate;

            tempNepaliDate = new NepaliDate(tempNepaliDate.getYear(), tempNepaliDate.getMonthNumber(), 1);

            tempGregorianCalendar.setTime(NepaliCalendar.convertNepaliToGregorianDate(tempNepaliDate));

            int currentMonth = tempNepaliDate.getMonthNumber(),
                    currentYear = tempNepaliDate.getYear();

            tempGregorianCalendar.add(Calendar.DATE, -GregorianCalendar.get(Calendar.DAY_OF_WEEK) + 1);
            tempNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(tempGregorianCalendar);

            while ((currentYear == tempNepaliDate.getYear() && currentMonth >= tempNepaliDate.getMonthNumber()) ||
                    (currentYear > tempNepaliDate.getYear() && currentMonth == 1)) {
                DateList.add(tempGregorianCalendar.getTime());

                tempGregorianCalendar.add(Calendar.DATE, 1);
                tempNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(tempGregorianCalendar);

            }

            if (tempGregorianCalendar.get(Calendar.DAY_OF_WEEK) != 1)
                for (int i = tempGregorianCalendar.get(Calendar.DAY_OF_WEEK); i <= 7; i++) {
                    DateList.add(tempGregorianCalendar.getTime());

                    tempGregorianCalendar.add(Calendar.DATE, 1);
                }

        } catch (NepaliDateException e) {
            e.printStackTrace();
        }
    }

    public void setCalendar(NepaliDate currentDate) {
        setCalendar(NepaliCalendar.convertNepaliToGregorianDate(currentDate));
    }

    public NepaliDate getSelectedNepaliDate() {
        return SelectedNepaliDate;
    }

    public void setSelected(Date selected) {
        this.mSelected = selected;
    }
}