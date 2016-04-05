package com.passion.attendance;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.passion.attendance.Models.Attendance;

import java.util.ArrayList;

/**
 * Created by Aayush on 3/21/2016.
 */
public class AttendanceAdapter extends BaseAdapter {

    Context context;
    ArrayList<Attendance> attendances;

    public AttendanceAdapter(Context context, ArrayList<Attendance> attendances) {
        this.context = context;
        this.attendances = attendances;
    }

    @Override
    public int getCount() {
        return attendances.size();
    }

    @Override
    public Attendance getItem(int position) {
        return attendances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.layout_attendance, parent, false);
        }

        TextView fromTextView = (TextView) convertView.findViewById(R.id.attendance_from_time);
        TextView toTextView = (TextView) convertView.findViewById(R.id.attendance_to_time);
        View attendanceIndicator = convertView.findViewById(R.id.attendance_indicator);

        Attendance a = getItem(position);

        fromTextView.setText(a.getFrom().toString("h:mm a"));
        toTextView.setText(a.getTo().toString("h:mm a"));

        if (a.getIsPresenct())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                attendanceIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.presence_indicator));
            else
                attendanceIndicator.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.presence_indicator));
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                attendanceIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.absence_indicator));
            else
                attendanceIndicator.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.absence_indicator));

        return convertView;
    }
}
