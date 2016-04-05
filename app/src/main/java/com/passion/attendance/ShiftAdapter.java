package com.passion.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.passion.attendance.Models.Shift;
import com.passion.attendance.Models.TimeRangeList;

/**
 * Created by Aayush on 3/31/2016.
 */
public class ShiftAdapter extends BaseAdapter {

    Context context;
    Shift shift;

    public ShiftAdapter(Context context, Shift shift) {
        this.context = context;
        this.shift = shift;
    }

    @Override
    public int getCount() {
        return shift.getTimeRangeLists().size();
    }

    @Override
    public TimeRangeList getItem(int position) {
        return shift.getTimeRangeList(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.layout_shift, parent, false);
        }

        ImageView dayImage = (ImageView) convertView.findViewById(R.id.day_image);
        TextView dayName = (TextView) convertView.findViewById(R.id.day_name);
        StaticListView dayShift = (StaticListView) convertView.findViewById(R.id.day_shift);

        String day = Shift.days[position];

        dayImage.setImageDrawable(
                TextDrawable.builder()
                        .buildRound(String.valueOf(day.charAt(0)), ColorList.getRandomColor(context))
        );

        dayName.setText(day);

        TimeRangeAdapter tr = new TimeRangeAdapter(context, getItem(position));

        dayShift.setAdapter(tr);

        return convertView;
    }
}
