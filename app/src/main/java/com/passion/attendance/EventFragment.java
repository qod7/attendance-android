package com.passion.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passion.attendance.Models.Event;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/20/2016.
 */
public class EventFragment extends Fragment {

    private final Context mContext;
    private StaticListView mEventListView;
    private DatabaseHandler mDatabaseHandler;
    private EventListAdapter mEventListAdapter;
    private ArrayList<Event> mEventList;
    private TextView mFragmentTitle;

    public EventFragment() {
        mContext = getContext();
    }

    public static EventFragment newInstance() {
        Bundle args = new Bundle();

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static EventFragment newInstance(Bundle args) {
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mEventList = new ArrayList<>();
        mDatabaseHandler = new DatabaseHandler(mContext);
        mFragmentTitle = (TextView) rootView.findViewById(R.id.detail_title);
        mEventListView = (StaticListView) rootView.findViewById(R.id.detail_list);
        mEventListAdapter = new EventListAdapter(mContext, mEventList);

        mEventListView.setAdapter(mEventListAdapter);

        if (getArguments() == null) {
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

        mEventList = mDatabaseHandler.retrieveEvents(selectedDate);
        mEventListAdapter.notifyDataSetChanged();
    }
}
