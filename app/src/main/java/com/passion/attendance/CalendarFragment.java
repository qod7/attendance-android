package com.passion.attendance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aayush on 11/27/2015.
 */
public class CalendarFragment extends Fragment {


    public static final String START_DAY = "day",
            START_MONTH = "month",
            START_YEAR = "year",
            START_WITH_NEPALI_DATE = "start_nepali_date";
    private int mCurrentMonth;
    private CalendarListener mCalendarListener;
    private AdapterView.OnItemClickListener mItemClickListener;
    private CalendarAdapter mCalendarAdapter;
    private StaticGridView mCalendarGridView;
    private ImageButton mNextMonthButton;
    private ImageButton mPreviousMonthButton;
    private TextView mCalendarMonthName;

    private Date mSelected;

    public CalendarFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_calendar, container,
                false);

        Calendar cal = loadArguments();

        mCalendarGridView = (StaticGridView) rootView.findViewById(R.id.calendar_gridview);
        mNextMonthButton = (ImageButton) rootView.findViewById(R.id.calendar_right_arrow);
        mPreviousMonthButton = (ImageButton) rootView.findViewById(R.id.calendar_left_arrow);
        mCalendarMonthName = (TextView) rootView.findViewById(R.id.calendar_month);

        mCalendarAdapter = new CalendarAdapter(getActivity(), mSelected);
        mCalendarGridView.setAdapter(mCalendarAdapter);

        mCalendarMonthName.setText(mCalendarAdapter.getSelectedNepaliDate().getMonthName());

        mCalendarGridView.setOnItemClickListener(getItemClickListener());

        View.OnClickListener MonthChangeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NepaliDate currentNepaliDate = mCalendarAdapter.getSelectedNepaliDate();

                int currentMonth = currentNepaliDate.getMonthNumber(),
                        currentYear = currentNepaliDate.getYear();

                int viewID = view.getId();
                switch (viewID) {
                    case R.id.calendar_right_arrow:
                        if (currentMonth == 12) {
                            currentMonth = 1;
                            currentYear++;
                        } else {
                            currentMonth++;
                        }
                        break;
                    case R.id.calendar_left_arrow:
                        if (currentMonth == 1) {
                            currentMonth = 12;
                            currentYear--;
                        } else {
                            currentMonth--;
                        }
                        break;
                }
                try {
                    mCalendarAdapter.setCalendar(new NepaliDate(currentYear, currentMonth, 1));
                    mCalendarAdapter.notifyDataSetChanged();
                    mCalendarMonthName.setText(mCalendarAdapter.getSelectedNepaliDate().getMonthName());
                    if (mCalendarListener != null)
                        mCalendarListener.onChangeMonth(currentMonth, currentYear);
                    Log.i("Blank", "Empty log for reviewing variables above");
                } catch (NepaliDateException e) {
                    e.printStackTrace();
                }
            }
        };

        mNextMonthButton.setOnClickListener(MonthChangeListener);
        mPreviousMonthButton.setOnClickListener(MonthChangeListener);

        if (mCalendarListener != null) mCalendarListener.onViewCreated();

        return rootView;
    }

    private Calendar loadArguments() {
        Bundle CalendarData = getArguments();

        if (CalendarData == null) CalendarData = new Bundle();

        try {
            Calendar cal = Calendar.getInstance();
            NepaliDate currentNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(cal);

            boolean nepaliDate = CalendarData.getBoolean(START_WITH_NEPALI_DATE, false);

            int month, day, year;
            if (nepaliDate) {
                month = CalendarData.getInt(START_MONTH, currentNepaliDate.getMonthNumber());
                day = CalendarData.getInt(START_DAY, currentNepaliDate.getDay());
                year = CalendarData.getInt(START_MONTH, currentNepaliDate.getYear());

                Date currentDate = NepaliCalendar.convertNepaliToGregorianDate(currentNepaliDate);
                cal.setTime(currentDate);
            } else {
                month = CalendarData.getInt(START_MONTH, cal.get(Calendar.MONTH));
                day = CalendarData.getInt(START_DAY, cal.get(Calendar.DAY_OF_MONTH));
                year = CalendarData.getInt(START_MONTH, cal.get(Calendar.YEAR));

                cal.set(year, month, day);
            }

            mSelected = cal.getTime();

            return cal;
        } catch (NepaliDateException e){
            return null;
        }
    }



    public AdapterView.OnItemClickListener getItemClickListener() {
        if (this.mItemClickListener == null) {
            setmItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSelected = mCalendarAdapter.getItem(position);
                    mCalendarAdapter.setSelected(mSelected);
                    mCalendarAdapter.notifyDataSetChanged();

                    if (mCalendarListener != null) {
                        mCalendarListener.onSelectDate(mCalendarAdapter.getItem(position), view);
                    }
                }
            });
        }
        return this.mItemClickListener;
    }

    public void setmItemClickListener(AdapterView.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public StaticGridView getmCalendarGridView() {
        return mCalendarGridView;
    }

    public void setmCalendarGridView(StaticGridView mCalendarGridView) {
        this.mCalendarGridView = mCalendarGridView;
    }

    public CalendarAdapter getCalendarAdapter() {
        return mCalendarAdapter;
    }

    public void setCalendarAdapter(CalendarAdapter calendarAdapter) {
        this.mCalendarAdapter = calendarAdapter;
    }

    public CalendarListener getCalendarListener() {
        return mCalendarListener;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.mCalendarListener = calendarListener;
    }
}
