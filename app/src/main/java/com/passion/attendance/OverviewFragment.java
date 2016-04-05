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
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.passion.attendance.Models.Shift;
import com.passion.attendance.Models.TimeRangeList;

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

    private EventFragment mEventFragment;
    private AttendanceFragment mAttendanceFragment;
    private InboxFragment mMessageFragment;
    private ImageView mShiftImage;
    private TextView mShiftDay;
    private View mView;

    public Integer mCurrentView;

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
        final View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        mContext = getContext();

        mTodayDetail = rootView.findViewById(R.id.detail_today_container);
        mSelectedDetail = rootView.findViewById(R.id.detail_selected_date_container);

        mDateTextView = (TextView) rootView.findViewById(R.id.selected_date);
        mSwitchViewButton = (ImageButton) rootView.findViewById(R.id.switch_mode_button);
        mShiftListView = (StaticListView) rootView.findViewById(R.id.shift_day_timetables);
        mTodayOverview = (TextView) rootView.findViewById(R.id.today_overview);

        mShiftDay = (TextView) rootView.findViewById(R.id.shift_day_name);
        mShiftImage = (ImageView) rootView.findViewById(R.id.shift_day_image);

        mEventFragment = EventFragment.newInstance();
        mAttendanceFragment = AttendanceFragment.newInstance();
        mMessageFragment = InboxFragment.newInstance();

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(mContext, getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Events"),
                mEventFragment.getClass(), new Bundle());
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Messages"),
                mMessageFragment.getClass(), new Bundle());
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Attendance"),
                mAttendanceFragment.getClass(), new Bundle());

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Bundle args = getArguments();

                int selectedYear = args.getInt(PassionAttendance.KEY_YEAR),
                        selectedMonth = args.getInt(PassionAttendance.KEY_MONTH),
                        selectedDay = args.getInt(PassionAttendance.KEY_DAY);

                Fragment parentFragment = OverviewFragment.this;
                Bundle fragmentArgs;


                switch (tabId) {
                    case "tab1":
                        EventFragment eventFragment = (EventFragment) parentFragment
                                .getChildFragmentManager()
                                .findFragmentByTag(tabId);

                        if (eventFragment != null) { // && !mFragment.isDetached()) {
                            parentFragment.getChildFragmentManager().beginTransaction()
                                    .detach(eventFragment)
                                    .commit();

                            fragmentArgs = eventFragment.getArguments();
                            fragmentArgs.putInt(PassionAttendance.KEY_YEAR, selectedYear);
                            fragmentArgs.putInt(PassionAttendance.KEY_MONTH, selectedMonth);
                            fragmentArgs.putInt(PassionAttendance.KEY_DAY, selectedDay);

                            parentFragment.getChildFragmentManager().beginTransaction()
                                    .attach(eventFragment)
                                    .commit();
                        }
                        break;
                    case "tab2":
                        InboxFragment messageFragment = (InboxFragment) parentFragment
                                .getChildFragmentManager()
                                .findFragmentByTag(tabId);

                        if (messageFragment != null) { // && !mFragment.isDetached()) {
                            parentFragment.getChildFragmentManager().beginTransaction()
                                    .detach(messageFragment)
                                    .commit();

                            fragmentArgs = messageFragment.getArguments();
                            fragmentArgs.putInt(PassionAttendance.KEY_YEAR, selectedYear);
                            fragmentArgs.putInt(PassionAttendance.KEY_MONTH, selectedMonth);
                            fragmentArgs.putInt(PassionAttendance.KEY_DAY, selectedDay);

                            parentFragment.getChildFragmentManager().beginTransaction()
                                    .attach(messageFragment)
                                    .commit();
                        }
                        break;
                    case "tab3":
                        AttendanceFragment attendanceFragment = (AttendanceFragment) parentFragment
                                .getChildFragmentManager()
                                .findFragmentByTag(tabId);

                        if (attendanceFragment != null) { // && !mFragment.isDetached()) {
                            parentFragment.getChildFragmentManager().beginTransaction()
                                    .detach(attendanceFragment)
                                    .commit();

                            fragmentArgs = attendanceFragment.getArguments();
                            fragmentArgs.putInt(PassionAttendance.KEY_YEAR, selectedYear);
                            fragmentArgs.putInt(PassionAttendance.KEY_MONTH, selectedMonth);
                            fragmentArgs.putInt(PassionAttendance.KEY_DAY, selectedDay);

                            parentFragment.getChildFragmentManager().beginTransaction()
                                    .attach(attendanceFragment)
                                    .commit();
                        }
                }
            }
        });

        Boolean todayOverview = getArguments().getBoolean(PassionAttendance.KEY_TODAY, true);

        setTodayVisible(todayOverview);

        mSwitchViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTodayVisible(true);
            }
        });

        mView = rootView;

        return rootView;
    }

    public void setTodayVisible(boolean visibility) {

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

    public void populateSelected() {

        Bundle args = getArguments();
        int selectedYear = args.getInt(PassionAttendance.KEY_YEAR),
                selectedMonth = args.getInt(PassionAttendance.KEY_MONTH),
                selectedDay = args.getInt(PassionAttendance.KEY_DAY);

        mDateTextView.setText(PassionAttendance.getDisplayedDate(new LocalDate(
                selectedYear,
                selectedMonth,
                selectedDay
        )));

        String currentTab = mTabHost.getCurrentTabTag();
        mTabHost.onTabChanged(currentTab);

//        mMessageFragment.updateView(selectedDate);
//        mEventFragment.updateView(selectedDate);
//        mAttendanceFragment.updateView(selectedDate);
    }

    private void populateToday() {

        DatabaseHandler db = new DatabaseHandler(mContext);

        // Displaying today's date
        LocalDate selectedDate = LocalDate.now();

        mDateTextView.setText(
                PassionAttendance.getDisplayedDate(selectedDate)
        );


        Integer msgCount = db.getMessagesCount(selectedDate),
                eventCount = db.getEventsCount(selectedDate);

//        Integer msgCount = db.getMessagesCount(),
//                eventCount = db.getEventsCount();

        setShiftListView();

        setOverviewMessage(msgCount, eventCount);
    }

    private void setShiftListView() {
        DatabaseHandler db = new DatabaseHandler(mContext);

        Shift shift = db.retrieveShift();

        int currentDay = LocalDate.now().getDayOfWeek() % 7;

        mShiftDay.setText(Shift.days[currentDay]);

        mShiftImage.setImageDrawable(
                TextDrawable.builder()
                        .buildRound(
                                String.valueOf(Shift.days[currentDay].charAt(0)),
                                ColorList.getRandomColor(mContext)
                        )
        );

        TimeRangeList trl = shift.getTimeRangeList(currentDay);

        TimeRangeAdapter timeRangeAdapter = new TimeRangeAdapter(mContext, trl);

        mShiftListView.setAdapter(timeRangeAdapter);

    }

    private void setOverviewMessage(Integer msgCount, Integer eventCount) {
        StringBuilder sb = new StringBuilder();
        String message = "";

        if (msgCount != 0) {
            message = "You have ";

            sb.append(msgCount)
                    .append(" message")
                    .append(msgCount > 1 ? "s" : "");

            message = message.concat(sb.toString());
        }
        sb = new StringBuilder(message);

        if (eventCount != 0) {
            if (message.isEmpty())
                sb.append("You have ");
            else
                sb.append(" and ");

            sb.append(eventCount)
                    .append(" event")
                    .append(eventCount > 1 ? "s" : "");

            message = sb.toString();
        }

//        Snackbar.make(mView, message, Snackbar.LENGTH_SHORT).show();
        mTodayOverview.setText(message);
    }

}
