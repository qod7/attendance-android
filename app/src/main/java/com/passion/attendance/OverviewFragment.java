package com.passion.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by Aayush on 3/20/2016.
 */
public class OverviewFragment extends Fragment {

    private FragmentTabHost mTabHost;
    private Context mContext;

    private View mTodayDetail;
    private View mSelectedDetail;
    private TextView mDateTextView;
    private ImageButton mSwitchViewButton;
    private StaticListView mShiftListView;
    private TextView mTodayOverview;

    public OverviewFragment() {
        mContext = getContext();
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static OverviewFragment newInstance() {

        Bundle args = new Bundle();

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OverviewFragment newInstance(Bundle args) {

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_day_overview, container, false);

        mTodayDetail = rootView.findViewById(R.id.detail_today_container);
        mSelectedDetail = rootView.findViewById(R.id.detail_selected_date_container);
        mDateTextView = (TextView) rootView.findViewById(R.id.selected_date);
        mSwitchViewButton = (ImageButton) rootView.findViewById(R.id.switch_mode_button);
        mShiftListView = (StaticListView) rootView.findViewById(R.id.shift_day_timetables);
        mTodayOverview = (TextView) rootView.findViewById(R.id.today_overview);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(mContext, getFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Events"),
                EventsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Messages"),
                InboxFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Attendance"),
                AttendanceFragment.class, null);

        setTodayVisible(true);

        mSwitchViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTodayVisible(true);
            }
        });

        return rootView;
    }

    private void setDisplayedDate(LocalDate localDate) {
        String dateString = "";
        try {
            LocalDate now = DateTime.now().toLocalDate();
            NepaliDate nowNepali = NepaliCalendar.convertGregorianToNepaliDate(now.toDate());

            dateString = String.format(
                    "%s, %s %d, %d",
                    now.toString("EE"),
                    nowNepali.getMonthName(),
                    nowNepali.getDay(),
                    nowNepali.getYear()
            );

        } catch (NepaliDateException e) {
            e.printStackTrace();
        }
        mDateTextView.setText(dateString);
    }

    private void setTodayVisible(boolean visibility) {

        if (visibility) {
            mTodayDetail.setVisibility(View.VISIBLE);
            mSelectedDetail.setVisibility(View.GONE);
            mSwitchViewButton.setVisibility(View.GONE);

            populateToday();
        } else {
            mTodayDetail.setVisibility(View.GONE);
            mSelectedDetail.setVisibility(View.VISIBLE);
            mSwitchViewButton.setVisibility(View.VISIBLE);

            populateSelected();
        }
    }

    private void populateSelected() {

        Bundle args = getArguments();

        // Displaying selected date
        LocalDate selectedDate = new LocalDate(
                args.getInt(PassionAttendance.KEY_YEAR),
                args.getInt(PassionAttendance.KEY_MONTH),
                args.getInt(PassionAttendance.KEY_DAY)
        );

        setDisplayedDate(selectedDate);
    }

    private void populateToday() {

        DatabaseHandler db = new DatabaseHandler(mContext);

        // Displaying today's date
        setDisplayedDate(DateTime.now().toLocalDate());

        StringBuilder sb = new StringBuilder();



    }

}
