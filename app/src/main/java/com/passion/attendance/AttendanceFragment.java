package com.passion.attendance;

import android.os.Bundle;

import com.passion.attendance.Models.Attendance;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/20/2016.
 */
public class AttendanceFragment extends DetailFragment {

    private ArrayList<Attendance> mAttendanceList;

    public AttendanceFragment() {

    }

    public static AttendanceFragment newInstance() {
        Bundle args = new Bundle();

        AttendanceFragment fragment = new AttendanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AttendanceFragment newInstance(Bundle args) {
        AttendanceFragment fragment = new AttendanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setAdapter() {
        mAttendanceList = new ArrayList<>();
        mDetailListAdapter = new AttendanceAdapter(mContext, mAttendanceList);
        mDetailListView.setAdapter(mDetailListAdapter);
    }

    @Override
    public void updateView(LocalDate selectedDate) {
        mAttendanceList.clear();
        mAttendanceList.addAll(mDatabaseHandler.retrieveAttendance(selectedDate));
        mDetailListAdapter.notifyDataSetChanged();
    }
}
