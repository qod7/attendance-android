package com.passion.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.passion.attendance.Models.Attendance;
import com.passion.attendance.Models.Event;
import com.passion.attendance.Models.Message;
import com.passion.attendance.Models.Staff;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Aayush on 3/10/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = PassionAttendance.PACKAGE_NAME;
    public static final Integer DATABASE_VERSION = 1;

    private Context context;

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String dbName = DATABASE_NAME + ".db";
        context.deleteDatabase(dbName);

        onCreate(db);
    }

    public void insertEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PassionAttendance.KEY_ID, event.getId());
        values.put(PassionAttendance.KEY_TITLE, event.getTitle());
        values.put(PassionAttendance.KEY_DESCRIPTION, event.getDescription());
        values.put(PassionAttendance.KEY_FROM, event.getFrom().toDate().getTime());
        values.put(PassionAttendance.KEY_TO, event.getTo().toDate().getTime());

        try {
            db.insertOrThrow("notices", null, values);
        } catch (SQLiteConstraintException e) {
            Log.e("Database Error", e.toString());
        }
        db.close();
    }

    public void insertEvent(ArrayList<Event> events) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        for (Event event : events) {
            values.put(PassionAttendance.KEY_ID, event.getId());
            values.put(PassionAttendance.KEY_TITLE, event.getTitle());
            values.put(PassionAttendance.KEY_DESCRIPTION, event.getDescription());
            values.put(PassionAttendance.KEY_FROM, event.getFrom().toDate().getTime());
            values.put(PassionAttendance.KEY_TO, event.getTo().toDate().getTime());

            try {
                db.insertOrThrow("events", null, values);
            } catch (SQLiteConstraintException e) {
                Log.e("Database Error", e.toString());
            }
        }
        db.close();
    }

    public void insertMessage(Message message) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PassionAttendance.KEY_ID, message.getId());
        values.put(PassionAttendance.KEY_TITLE, message.getTitle());
        values.put(PassionAttendance.KEY_CONTENT, message.getContent());
        values.put(PassionAttendance.KEY_TYPE, message.getType());
        values.put(PassionAttendance.KEY_SENT, message.getSentOn().toDate().getTime());

        try {
            db.insertOrThrow("notices", null, values);
        } catch (SQLiteConstraintException e) {
            Log.e("Database Error", e.toString());
        }
        db.close();

    }

    public void insertMessage(ArrayList<Message> messages) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        for (Message message : messages) {
            values.put(PassionAttendance.KEY_ID, message.getId());
            values.put(PassionAttendance.KEY_TITLE, message.getTitle());
            values.put(PassionAttendance.KEY_CONTENT, message.getContent());
            values.put(PassionAttendance.KEY_TYPE, message.getType());
            values.put(PassionAttendance.KEY_SENT, message.getSentOn().toDate().getTime());

            try {
                db.insertOrThrow("notices", null, values);
            } catch (SQLiteConstraintException e) {
                Log.e("Database Error", e.toString());
            }
        }
        db.close();
    }

    public void insertAttendance(Attendance attendance) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PassionAttendance.KEY_ID, attendance.getId());
        values.put(PassionAttendance.KEY_IS_PRESENT, attendance.getAttendance());
        values.put(PassionAttendance.KEY_DATE, attendance.getDate().toDate().getTime());

        try {
            db.insertOrThrow("notices", null, values);
        } catch (SQLiteConstraintException e) {
            Log.e("Database Error", e.toString());
        }
        db.close();

    }

    public void insertAttendance(ArrayList<Attendance> attendances) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        for (Attendance attendance : attendances) {
            values.put(PassionAttendance.KEY_ID, attendance.getId());
            values.put(PassionAttendance.KEY_IS_PRESENT, attendance.getAttendance());
            values.put(PassionAttendance.KEY_DATE, attendance.getDate().toDate().getTime());

            try {
                db.insertOrThrow("notices", null, values);
            } catch (SQLiteConstraintException e) {
                Log.e("Database Error", e.toString());
            }
        }
        db.close();

    }

    public void insertStaff(Staff staff){
        SharedPreferences sp = context.getSharedPreferences(PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE);

        sp.edit()
                .putInt(PassionAttendance.KEY_ID, staff.getId())
                .putString(PassionAttendance.KEY_NAME, staff.getName())
                .putString(PassionAttendance.KEY_ORGANIZATION, staff.getOrganization())
                .putString(PassionAttendance.KEY_IMAGE_URL, staff.getImageUrl())
                .putString(PassionAttendance.KEY_CONTACT_NUMBER, staff.getContactNumber())
                .putString(PassionAttendance.KEY_EXTRAS, PassionAttendance.getStringFromMap(staff.getExtras()))
                .putString(PassionAttendance.KEY_PREFERENCES, PassionAttendance.getStringFromMap(staff.getPreferences()))
                .apply();
    }

    public Staff retrieveStaff(){
        SharedPreferences sp;
        try {
            sp = context.getSharedPreferences(PassionAttendance.PREFERENCE_STAFF,
                    Context.MODE_PRIVATE);
        } catch (NullPointerException e) {
            return null;
        }

        Integer id = sp.getInt(PassionAttendance.KEY_ID, -1);
        String name = sp.getString(PassionAttendance.KEY_NAME, "");
        String organization = sp.getString(PassionAttendance.KEY_ORGANIZATION, "");
        String image = sp.getString(PassionAttendance.KEY_IMAGE_URL, "");
        String contact = sp.getString(PassionAttendance.KEY_CONTACT_NUMBER, "");

        HashMap<String, String> extras = PassionAttendance.getMapFromString(sp.getString(PassionAttendance.KEY_EXTRAS,
                ""));
        HashMap<String, String> preferences = PassionAttendance.getMapFromString(sp.getString(PassionAttendance.KEY_PREFERENCES,
                ""));

        return new Staff(id, name, organization, image, contact, extras, preferences);
    }

    public ArrayList<Attendance> retrieveAttendances() {
        SQLiteDatabase db = getReadableDatabase();
        String Query;
        Query = "SELECT * FROM attendances;";
        Cursor c = db.rawQuery(Query, null);

        ArrayList<Attendance> attendances = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                Boolean isPresent = c.getInt(c.getColumnIndex(PassionAttendance.KEY_IS_PRESENT)) != 0;
                LocalDate date = new LocalDate(c.getInt(c.getColumnIndex(PassionAttendance.KEY_DATE)));

                attendances.add(new Attendance(id, date, isPresent));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return attendances;
    }

    public ArrayList<Attendance> retrieveAttendances(Date d) {
        SQLiteDatabase db = getReadableDatabase();

        String Query;
        Query = "SELECT * FROM attendances WHERE " + PassionAttendance.KEY_DATE + " = \"" + d.getTime() + "\";";
        Cursor c = db.rawQuery(Query, null);

        ArrayList<Attendance> attendances = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                Boolean isPresent = c.getInt(c.getColumnIndex(PassionAttendance.KEY_IS_PRESENT)) != 0;
                LocalDate date = new LocalDate(c.getInt(c.getColumnIndex(PassionAttendance.KEY_DATE)));

                attendances.add(new Attendance(id, date, isPresent));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return attendances;
    }

    public ArrayList<Event> retrieveEvents() {
        SQLiteDatabase db = getReadableDatabase();
        String Query;
        Query = "SELECT * FROM events;";
        Cursor c = db.rawQuery(Query, null);

        ArrayList<Event> events = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String description = c.getString(c.getColumnIndex(PassionAttendance.KEY_DESCRIPTION));
                LocalDate from = new LocalDate(c.getInt(c.getColumnIndex(PassionAttendance.KEY_FROM)));
                LocalDate to = new LocalDate(c.getInt(c.getColumnIndex(PassionAttendance.KEY_TO)));

                events.add(new Event(id, title, description, from, to));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return events;
    }

    public ArrayList<Message> retrieveMessages() {
        SQLiteDatabase db = getReadableDatabase();
        String Query;
        Query = "SELECT * FROM messages;";
        Cursor c = db.rawQuery(Query, null);

        ArrayList<Message> messages = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String content = c.getString(c.getColumnIndex(PassionAttendance.KEY_CONTENT));
                LocalDate sent = new LocalDate(c.getInt(c.getColumnIndex(PassionAttendance.KEY_SENT)));
                Integer type = c.getInt(c.getColumnIndex(PassionAttendance.KEY_TYPE));

                messages.add(new Message(id, title, content, type, sent));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return messages;
    }

    public ArrayList<Message> retrieveMessages(Date d) {
        SQLiteDatabase db = getReadableDatabase();
        String Query;
        Query = String.format(
                "SELECT * FROM messages WHERE %s = \"%s\";",
                PassionAttendance.KEY_SENT,
                d.getTime()
        );
        Cursor c = db.rawQuery(Query, null);

        ArrayList<Message> messages = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String content = c.getString(c.getColumnIndex(PassionAttendance.KEY_CONTENT));
                LocalDate sent = new LocalDate(c.getInt(c.getColumnIndex(PassionAttendance.KEY_SENT)));
                Integer type = c.getInt(c.getColumnIndex(PassionAttendance.KEY_TYPE));

                messages.add(new Message(id, title, content, type, sent));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return messages;
    }
}
