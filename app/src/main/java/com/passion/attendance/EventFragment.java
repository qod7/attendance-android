package com.passion.attendance;

import android.os.Bundle;

import com.passion.attendance.Models.Event;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/20/2016.
 */
public class EventFragment extends DetailFragment {

    ArrayList<Event> mEventList;

    public EventFragment() {
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

    @Override
    public void setAdapter() {
        mEventList = new ArrayList<>();
        mDetailListAdapter = new EventListAdapter(mContext, mEventList);
        mDetailListView.setAdapter(mDetailListAdapter);
    }

    @Override
    public void updateView(LocalDate selectedDate) {
        mEventList.clear();
        mEventList.addAll(mDatabaseHandler.retrieveEvents(selectedDate));
        mDetailListAdapter.notifyDataSetChanged();
    }
}
