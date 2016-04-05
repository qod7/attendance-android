package com.passion.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/30/2016.
 */
public abstract class DetailFragment extends Fragment {
    protected Context mContext;
    protected ArrayList<Object> mDetailList;
    protected DatabaseHandler mDatabaseHandler;
    protected StaticListView mDetailListView;
    protected BaseAdapter mDetailListAdapter;
    protected boolean isLoaded = false;

    private OnFragmentRefresh mOnFragmentRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mContext = getContext();

        mDetailList = new ArrayList<>();
        mDatabaseHandler = new DatabaseHandler(mContext);
        mDetailListView = (StaticListView) rootView.findViewById(R.id.detail_list);

        updateView();

        isLoaded = true;

        if (mOnFragmentRefresh != null)
            mOnFragmentRefresh.onFragmentRefresh();

        return rootView;
    }

    public void setOnFragmentRefresh(OnFragmentRefresh mOnFragmentRefresh) {
        this.mOnFragmentRefresh = mOnFragmentRefresh;
    }

    public void updateView() {
        setAdapter();

        updateView(getSelectedDate());

    }

    private LocalDate getSelectedDate() {
        Bundle args = getArguments();

        LocalDate selectedDate;

        try {
            int selectedYear = args.getInt(PassionAttendance.KEY_YEAR),
                    selectedMonth = args.getInt(PassionAttendance.KEY_MONTH),
                    selectedDay = args.getInt(PassionAttendance.KEY_DAY);

            selectedDate = new LocalDate(
                    selectedYear,
                    selectedMonth,
                    selectedDay
            );
        } catch (NullPointerException|IllegalFieldValueException e) {
            args = getParentFragment().getArguments();
            try {
                int selectedYear = args.getInt(PassionAttendance.KEY_YEAR),
                        selectedMonth = args.getInt(PassionAttendance.KEY_MONTH),
                        selectedDay = args.getInt(PassionAttendance.KEY_DAY);

                selectedDate = new LocalDate(
                        selectedYear,
                        selectedMonth,
                        selectedDay
                );
            } catch (NullPointerException|IllegalFieldValueException e1){
                selectedDate = LocalDate.now();
            }
        }

//        Toast.makeText(mContext, selectedDate.toString(), Toast.LENGTH_SHORT).show();

        return selectedDate;
    }

    public abstract void setAdapter();

    public abstract void updateView(LocalDate selectedDate);

    public interface OnFragmentRefresh {
        void onFragmentRefresh();
    }
}
