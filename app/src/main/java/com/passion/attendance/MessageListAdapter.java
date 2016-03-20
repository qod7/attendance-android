package com.passion.attendance;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.passion.attendance.Models.Message;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by Aayush on 1/2/2016.
 */
public class MessageListAdapter extends BaseAdapter {

    List<Message> mMessageList;
    private Context context;

    public MessageListAdapter(Context context, List<Message> mMessageList) {
        this.mMessageList = mMessageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        //TODO return Event id;
        return mMessageList.get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater l = LayoutInflater.from(context);
            view = l.inflate(R.layout.layout_message, null);
        }

        final DatabaseHandler db = new DatabaseHandler(context);

        TextView organization = (TextView) view.findViewById(R.id.message_organization);
        TextView title = (TextView) view.findViewById(R.id.message_title);
        TextView content = (TextView) view.findViewById(R.id.message_content);

        ImageView logo = (ImageView) view.findViewById(R.id.organization_image);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.content_delete_button);
        ImageButton replyButton = (ImageButton) view.findViewById(R.id.content_reply_button);

        TextView from = (TextView) view.findViewById(R.id.content_date);

        final Message m = mMessageList.get(i);

        String messageOrganization = db.retrieveStaff().getOrganization();
        String messageTitle = m.getTitle();
        String messageContent = m.getContent();
        LocalDate messageDate = m.getSentOn();

        // Populating simple fields
        organization.setText(messageOrganization);
        title.setText(messageTitle);
        content.setText(messageContent);
        from.setText(
                PassionAttendance.getDisplayedDate(messageDate)
        );

        // Populating the logo
        final TextDrawable logoDrawable = TextDrawable.builder()
                .buildRound(
                        String.valueOf(messageOrganization.charAt(0)),
                        Color.RED
                );
        logo.setImageDrawable(logoDrawable);

        // Attaching buttons with click listeners
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.deleteMessage(m.getId())) {
                    mMessageList.remove(i);
                    notifyDataSetChanged();
                }
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(context);

                LayoutInflater li = LayoutInflater.from(context);
                View dialogView = li.inflate(R.layout.layout_send_message, null);

                ImageView dialogLogo = (ImageView) dialogView.findViewById(R.id.organization_image);
                TextView dialogOrganization = (TextView) dialogView.findViewById(R.id.message_organization);
                final EditText dialogTitle = (EditText) dialogView.findViewById(R.id.message_title);
                final EditText dialogContent = (EditText) dialogView.findViewById(R.id.message_content);
                Button dialogSend = (Button) dialogView.findViewById(R.id.message_send);

                dialogLogo.setImageDrawable(logoDrawable);
                dialogOrganization.setText(db.retrieveStaff().getOrganization());

                dialogSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = dialogTitle.getText().toString(),
                                content = dialogContent.getText().toString();

                        if (content.isEmpty()){
                            dialogContent.setError("No message entered");
                            dialogContent.requestFocus();
                        } else {
                            sendMessage(title, content);
                        }
                    }
                });
                d.setContentView(dialogView);
                d.show();
            }
        });


        return view;
    }

    private void sendMessage(String title, String content) {
        // TODO Create an Async task to send message to server
    }
}
