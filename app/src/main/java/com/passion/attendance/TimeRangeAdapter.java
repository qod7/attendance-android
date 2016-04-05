package com.passion.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.passion.attendance.Models.TimeRange;
import com.passion.attendance.Models.TimeRangeList;

/**
 * Created by Aayush on 3/30/2016.
 */
public class TimeRangeAdapter extends BaseAdapter {
    
    Context context;
    TimeRangeList timeRanges;

    public TimeRangeAdapter(Context context, TimeRangeList timeRanges) {
        this.context = context;
        this.timeRanges = timeRanges;
    }

    @Override
    public int getCount() {
        return timeRanges.size();
    }

    @Override
    public TimeRange getItem(int position) {
        return timeRanges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.layout_shift_day, parent, false);
        }

        TextView fromTextView = (TextView) convertView.findViewById(R.id.content_from_date);
        TextView toTextView = (TextView) convertView.findViewById(R.id.content_to_date);

        TimeRange t = getItem(position);

        fromTextView.setText(t.getFrom().toString("h:mm a"));
        toTextView.setText(t.getTo().toString("h:mm a"));

        return convertView;
    }
}
