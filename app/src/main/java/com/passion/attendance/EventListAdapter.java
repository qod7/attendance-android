package com.passion.attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.passion.attendance.Models.Event;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Aayush on 1/2/2016.
 */
public class EventListAdapter extends BaseAdapter {

    ArrayList<Event> mEventList;
    private Context context;

    LocalBroadcastManager mBroadcastManager;

    public EventListAdapter(Context context, ArrayList<Event> mEventList) {
        this.mEventList = mEventList;
        this.context = context;

        mBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    @Override
    public Object getItem(int i) {
        return mEventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mEventList.get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater l = LayoutInflater.from(context);
            view = l.inflate(R.layout.layout_event, null);
        }

        final DatabaseHandler db = new DatabaseHandler(context);

        TextView organization = (TextView) view.findViewById(R.id.event_organization);
        TextView title = (TextView) view.findViewById(R.id.event_title);
        TextView description = (TextView) view.findViewById(R.id.event_description);

        ImageView logo = (ImageView) view.findViewById(R.id.organization_image);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.content_delete_button);

        View fromContainer = view.findViewById(R.id.event_from_desc);
        TextView fromLabel = (TextView) view.findViewById(R.id.event_from_label);
        TextView from = (TextView) view.findViewById(R.id.content_from_date);

        View toContainer = view.findViewById(R.id.event_to_desc);
        TextView toLabel = (TextView) view.findViewById(R.id.event_to_label);
        TextView to = (TextView) view.findViewById(R.id.content_to_date);

        final Event e = mEventList.get(i);

        String eventOrganization = db.retrieveStaff().getOrganization();
        String eventTitle = e.getTitle();
        String eventDescription = e.getDescription();
        LocalDate eventFrom = e.getFrom();
        LocalDate eventTo = e.getTo();

        // Populating simple fields
        organization.setText(eventOrganization);
        title.setText(eventTitle);
        description.setText(eventDescription);

        // Populating the logo
        logo.setImageDrawable(
                TextDrawable.builder()
                        .buildRound(
                                String.valueOf(eventOrganization.charAt(0)),
                                Color.RED
                        )
        );

        // Populating the date fields
        if (eventFrom.equals(eventTo)) {
            toContainer.setVisibility(View.GONE);

            fromLabel.setText("At:");

            from.setText(
                    PassionAttendance.getDisplayedShortDate(eventTo)
            );
        } else {
            toContainer.setVisibility(View.VISIBLE);

            fromLabel.setText("From:");

            from.setText(
                    PassionAttendance.getDisplayedShortDate(eventFrom)
            );

            to.setText(
                    PassionAttendance.getDisplayedShortDate(eventTo)
            );
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.deleteEvent(e.getId())) {
                    mEventList.remove(i);
                    notifyDataSetChanged();

                    Intent i = new Intent();
                    i.setAction(PassionAttendance.ACTION_REMOVE_ITEM);
                    mBroadcastManager.sendBroadcast(i);
                }
            }
        });

        return view;
    }
}
