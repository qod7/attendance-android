package com.passion.attendance;

import android.os.Bundle;

import com.passion.attendance.Models.Message;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/20/2016.
 */
public class InboxFragment extends DetailFragment {

    private ArrayList<Message> mMessageList;

    public InboxFragment() {
    }

    public static InboxFragment newInstance() {
        Bundle args = new Bundle();

        InboxFragment fragment = new InboxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InboxFragment newInstance(Bundle args) {
        InboxFragment fragment = new InboxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setAdapter() {
        mMessageList = new ArrayList<>();
        mDetailListAdapter = new MessageListAdapter(mContext, mMessageList);
        mDetailListView.setAdapter(mDetailListAdapter);
    }

    @Override
    public void updateView(LocalDate selectedDate) {
        mMessageList.clear();
        mMessageList.addAll(mDatabaseHandler.retrieveMessages(selectedDate));
        mDetailListAdapter.notifyDataSetChanged();
    }
}
