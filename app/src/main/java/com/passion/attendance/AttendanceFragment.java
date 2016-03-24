package com.passion.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passion.attendance.Models.Attendance;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/20/2016.
 */
public class AttendanceFragment extends Fragment {

    private final Context mContext;
    private StaticListView mAttendanceListView;
    private DatabaseHandler mDatabaseHandler;
    private AttendanceAdapter mAttendanceAdapter;
    private ArrayList<Attendance> mAttendanceList;
    private TextView mFragmentTitle;

    public AttendanceFragment() {
        mContext = getContext();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mAttendanceList = new ArrayList<Attendance>();
        mDatabaseHandler = new DatabaseHandler(mContext);
        mFragmentTitle = (TextView) rootView.findViewById(R.id.detail_title);
        mAttendanceListView = (StaticListView) rootView.findViewById(R.id.detail_list);

        if (getArguments() == null){
            loadView(LocalDate.now());
        } else {
            LocalDate localDate = new LocalDate(
                    getArguments().getInt(PassionAttendance.KEY_YEAR),
                    getArguments().getInt(PassionAttendance.KEY_MONTH),
                    getArguments().getInt(PassionAttendance.KEY_DAY)
            );

            loadView(localDate);
        }
        return rootView;
    }

    public void loadView(LocalDate selectedDate) {
        mFragmentTitle.setText("Events");

        mAttendanceList = mDatabaseHandler.retrieveAttendance(selectedDate);

        mAttendanceAdapter = new AttendanceAdapter(mContext, mAttendanceList);
        mAttendanceListView.setAdapter(mAttendanceAdapter);
    }
}
