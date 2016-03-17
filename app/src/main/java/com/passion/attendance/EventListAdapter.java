package com.passion.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.passion.attendance.Models.Event;

import java.util.List;

/**
 * Created by Aayush on 1/2/2016.
 */
public class EventListAdapter extends BaseAdapter {

    List<Event> EventList;
    private Context context;

    public EventListAdapter(Context context, List<Event> eventList) {
        EventList = eventList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return EventList.size();
    }

    @Override
    public Object getItem(int i) {
        return EventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        //TODO return Event id;
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater l = LayoutInflater.from(context);
            view = l.inflate(R.layout.layout_item_container, null);
        }

        View RootView = view.findViewById(R.id.content_layout);

        RootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Item " + i + " pressed", Toast.LENGTH_SHORT).show();
            }
        });

        TextView Title = (TextView) view.findViewById(R.id.content_title);
        String t = "Title " + i;
        Title.setText(t);
        return view;
    }
}
