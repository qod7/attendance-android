package com.passion.attendance;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.passion.attendance.Models.Event;
import com.passion.attendance.Models.Message;

import java.util.ArrayList;
import java.util.List;

public class GcmMessageReceiver extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String gcmMessage = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + gcmMessage);

        String type = "";

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setAutoCancel(true)
                .setSound(defaultRingtoneUri);

        List<String> details = new ArrayList<>();

        PendingIntent pendingIntent;
        TaskStackBuilder stackBuilder;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        switch (type) {
            case "EVENT":
                Event event = Event.getDummyEvent();

                // Adding event information to notification
                notificationBuilder.setContentTitle(event.getTitle());
                notificationBuilder.setContentText(event.getDescription());

                // Setting up action for notification
                Intent eventIntent = new Intent(this, EventsActivity.class);
                eventIntent.putExtra(
                        PassionAttendance.KEY_SOURCE,
                        PassionAttendance.KEY_NOTIFICATION
                );
                eventIntent.putExtra(
                        PassionAttendance.KEY_EVENT,
                        event.toString()
                );

                // Setting up stack for notification action
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(EventsActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(eventIntent);

                pendingIntent = stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                notificationBuilder.setContentIntent(pendingIntent);

                // Adding details to notification
                inboxStyle.setBigContentTitle("Event details");
                details.add("From: " + event.getFrom().toString());
                details.add("To: " + event.getTo().toString());

                for (String detail : details) inboxStyle.addLine(detail);

                notificationBuilder.setStyle(inboxStyle);
                break;
            case "MESSAGE":
                Message message = Message.getDummyMessage();

                //Adding message information to notification
                notificationBuilder.setContentTitle(message.getTitle());
                notificationBuilder.setContentText(message.getContent());

                // Setting up action for notification
                Intent messageIntent = new Intent(this, MessagesActivity.class);
                messageIntent.putExtra(
                        PassionAttendance.KEY_SOURCE,
                        PassionAttendance.KEY_NOTIFICATION
                );
                messageIntent.putExtra(
                        PassionAttendance.KEY_MESSAGE,
                        message.toString()
                );

                // Setting up stack for notification action
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MessagesActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(messageIntent);

                pendingIntent = stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                notificationBuilder.setContentIntent(pendingIntent);

                // Adding details to message
                inboxStyle.setBigContentTitle("Message details");
                details.add("Sent: " + message.getSentOn().toString());

                for (String detail : details) inboxStyle.addLine(detail);

                notificationBuilder.setStyle(inboxStyle);
                break;
        }

        nm.notify("NOTIFICATION", 0, notificationBuilder.build());

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        // [END_EXCLUDE]
    }
    // [END receive_message]
}