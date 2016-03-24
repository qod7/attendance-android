package com.passion.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passion.attendance.Models.Message;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/20/2016.
 */
public class InboxFragment extends Fragment {

    private final Context mContext;
    private StaticListView mMessageListView;
    private DatabaseHandler mDatabaseHandler;
    private MessageListAdapter mMessageListAdapter;
    private ArrayList<Message> mMessageList;
    private TextView mFragmentTitle;

    public InboxFragment() {
        mContext = getContext();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mMessageList = new ArrayList<>();
        mDatabaseHandler = new DatabaseHandler(mContext);
        mFragmentTitle = (TextView) rootView.findViewById(R.id.detail_title);
        mMessageListView = (StaticListView) rootView.findViewById(R.id.detail_list);
        mMessageListAdapter = new MessageListAdapter(mContext, mMessageList);

        mMessageListView.setAdapter(mMessageListAdapter);

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
        mFragmentTitle.setText("Messages");

        mMessageList = mDatabaseHandler.retrieveMessages(selectedDate);
        mMessageListAdapter.notifyDataSetChanged();
    }
}
