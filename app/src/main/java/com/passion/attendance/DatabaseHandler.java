package com.passion.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.passion.attendance.Models.Attendance;
import com.passion.attendance.Models.Event;
import com.passion.attendance.Models.Message;
import com.passion.attendance.Models.Shift;
import com.passion.attendance.Models.Staff;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
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
        this.context = context;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query;

        query = String.format(
                "CREATE TABLE attendances (%s INTEGER PRIMARY KEY, " +
                "%s STRING, %s STRING, %s STRING, %s INTEGER);",
                PassionAttendance.KEY_ID,
                PassionAttendance.KEY_DATE,
                PassionAttendance.KEY_FROM,
                PassionAttendance.KEY_TO,
                PassionAttendance.KEY_IS_PRESENT
        );
        db.execSQL(query);
        Log.i("Query", query);

        query = String.format(
                "CREATE TABLE events (%s INTEGER PRIMARY KEY, " +
                        "%s STRING, %s STRING, %s STRING, %s STRING);",
                PassionAttendance.KEY_ID,
                PassionAttendance.KEY_TITLE,
                PassionAttendance.KEY_DESCRIPTION,
                PassionAttendance.KEY_FROM,
                PassionAttendance.KEY_TO
        );
        db.execSQL(query);
        Log.i("Query", query);

        query = String.format(
                "CREATE TABLE messages (%s INTEGER PRIMARY KEY, " +
                        "%s STRING, %s STRING, %s INTEGER, %s STRING);",
                PassionAttendance.KEY_ID,
                PassionAttendance.KEY_TITLE,
                PassionAttendance.KEY_CONTENT,
                PassionAttendance.KEY_TYPE,
                PassionAttendance.KEY_SENT
        );
        db.execSQL(query);
        Log.i("Query", query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String dbName = DATABASE_NAME + ".db";
        context.deleteDatabase(dbName);

        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();

        String Query;

        Query = "DELETE FROM attendances;";
        db.execSQL(Query);

        Query = "DELETE FROM messages;";
        db.execSQL(Query);

        Query = "DELETE FROM events;";
        db.execSQL(Query);

        db.close();

        SharedPreferences sp = context.getSharedPreferences(
                PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE
        );

        sp.edit()
                .remove(PassionAttendance.KEY_STAFF)
                .remove(PassionAttendance.KEY_SHIFT)
                .apply();
    }

    public void insertEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PassionAttendance.KEY_ID, event.getId());
        values.put(PassionAttendance.KEY_TITLE, event.getTitle());
        values.put(PassionAttendance.KEY_DESCRIPTION, event.getDescription());
        values.put(PassionAttendance.KEY_FROM, event.getFrom().toString());
        values.put(PassionAttendance.KEY_TO, event.getTo().toString());

        try {
            db.insertOrThrow("events", null, values);
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
            values.put(PassionAttendance.KEY_FROM, event.getFrom().toString());
            values.put(PassionAttendance.KEY_TO, event.getTo().toString());

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
        values.put(PassionAttendance.KEY_SENT, message.getSentOn().toString());

        try {
            db.insertOrThrow("messages", null, values);
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
            values.put(PassionAttendance.KEY_SENT, message.getSentOn().toString());

            try {
                db.insertOrThrow("messages", null, values);
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
        values.put(PassionAttendance.KEY_DATE, attendance.getDate().toString());
        values.put(PassionAttendance.KEY_FROM, attendance.getFrom().toString());
        values.put(PassionAttendance.KEY_TO, attendance.getTo().toString());
        values.put(PassionAttendance.KEY_IS_PRESENT, attendance.getIsPresenct());

        try {
            db.insertOrThrow("attendances", null, values);
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
            values.put(PassionAttendance.KEY_DATE, attendance.getDate().toString());
            values.put(PassionAttendance.KEY_FROM, attendance.getFrom().toString());
            values.put(PassionAttendance.KEY_TO, attendance.getTo().toString());
            values.put(PassionAttendance.KEY_IS_PRESENT, attendance.getIsPresenct());

            try {
                db.insertOrThrow("attendances", null, values);
            } catch (SQLiteConstraintException e) {
                Log.e("Database Error", e.toString());
            }
        }
        db.close();

    }

    public void insertStaff(Staff staff) {
        SharedPreferences sp = context.getSharedPreferences(PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE);

        sp.edit()
                .putInt(PassionAttendance.KEY_ID, staff.getId())
                .putString(PassionAttendance.KEY_NAME, staff.getName())
                .putString(PassionAttendance.KEY_ORGANIZATION, staff.getOrganization())
                .putString(PassionAttendance.KEY_IMAGE_URL, staff.getImageUrl())
                .putString(PassionAttendance.KEY_CONTACT_NUMBER, staff.getContactNumber())
                .putString(
                        PassionAttendance.KEY_EXTRAS,
                        PassionAttendance.getStringFromStringMap(staff.getExtras())
                )
                .putString(
                        PassionAttendance.KEY_PREFERENCES,
                        PassionAttendance.getStringFromStringMap(staff.getPreferences())
                )
                .apply();
    }

    public void insertShift(Shift shift) {
        SharedPreferences sp = context.getSharedPreferences(PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE);

        sp.edit().putString(PassionAttendance.KEY_SHIFT, shift.toString()).apply();
    }

    public Staff retrieveStaff() {
        SharedPreferences sp;
        try {
            sp = context.getSharedPreferences(
                    PassionAttendance.PREFERENCE_STAFF,
                    Context.MODE_PRIVATE
            );
        } catch (NullPointerException e) {
            return null;
        }

        Integer id = sp.getInt(PassionAttendance.KEY_ID, -1);
        String name = sp.getString(PassionAttendance.KEY_NAME, "");
        String organization = sp.getString(PassionAttendance.KEY_ORGANIZATION, "");
        String image = sp.getString(PassionAttendance.KEY_IMAGE_URL, "");
        String contact = sp.getString(PassionAttendance.KEY_CONTACT_NUMBER, "");

        HashMap<String, String> extras = PassionAttendance.getStringMapFromString(
                sp.getString(PassionAttendance.KEY_EXTRAS, "")
        );
        HashMap<String, String> preferences = PassionAttendance.getStringMapFromString(
                sp.getString(PassionAttendance.KEY_PREFERENCES, "")
        );

        return new Staff(id, name, organization, image, contact, extras, preferences);
    }

    public Shift retrieveShift() {
        SharedPreferences sp = context.getSharedPreferences(
                PassionAttendance.PREFERENCE_STAFF,
                Context.MODE_PRIVATE
        );

        return new Shift(sp.getString(PassionAttendance.KEY_SHIFT, ""));
    }

    public ArrayList<Attendance> retrieveAttendances() {
        SQLiteDatabase db = getReadableDatabase();
        String Query = "SELECT * FROM attendances;";
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Attendance> attendances = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                LocalDate date = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_DATE)));
                LocalTime from = new LocalTime(c.getString(c.getColumnIndex(PassionAttendance.KEY_FROM)));
                LocalTime to = new LocalTime(c.getString(c.getColumnIndex(PassionAttendance.KEY_TO)));
                Boolean isPresent = c.getInt(c.getColumnIndex(PassionAttendance.KEY_IS_PRESENT)) != 0;

                attendances.add(new Attendance(id, date, from, to, isPresent));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return attendances;
    }

    public ArrayList<Attendance> retrieveAttendance(LocalDate d) {
        SQLiteDatabase db = getReadableDatabase();

        String Query = String.format("SELECT * FROM attendances WHERE %s = \"%s\";",
                PassionAttendance.KEY_DATE,
                d
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Attendance> attendances = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                LocalDate date = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_DATE)));
                LocalTime from = new LocalTime(c.getString(c.getColumnIndex(PassionAttendance.KEY_FROM)));
                LocalTime to = new LocalTime(c.getString(c.getColumnIndex(PassionAttendance.KEY_TO)));
                Boolean isPresent = c.getInt(c.getColumnIndex(PassionAttendance.KEY_IS_PRESENT)) != 0;

                attendances.add(new Attendance(id, date, from, to, isPresent));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return attendances;
    }

    public ArrayList<Event> retrieveEvents() {
        SQLiteDatabase db = getReadableDatabase();
        String Query = "SELECT * FROM events;";
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Event> events = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String description = c.getString(c.getColumnIndex(PassionAttendance.KEY_DESCRIPTION));
                LocalDate from = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_FROM)));
                LocalDate to = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_TO)));

                events.add(new Event(id, title, description, from, to));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return events;
    }

    public ArrayList<Event> retrieveEvents(LocalDate d) {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT * FROM events WHERE %s <= \"%s\" AND %s >= \"%s\";",
                PassionAttendance.KEY_FROM,
                d.toString(),
                PassionAttendance.KEY_TO,
                d.toString()
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Event> events = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String description = c.getString(c.getColumnIndex(PassionAttendance.KEY_DESCRIPTION));
                LocalDate from = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_FROM)));
                LocalDate to = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_TO)));

                events.add(new Event(id, title, description, from, to));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return events;
    }

    public ArrayList<Event> retrieveEvents(LocalDate startDate, LocalDate endDate) {
        SQLiteDatabase db = getReadableDatabase();

        String Query = String.format(
                "SELECT * FROM events WHERE " +
                        "(%s >= \"%s\" AND %s <= \"%s\") OR " +
                        "(%s >= \"%s\" AND %s <= \"%s\");",
                PassionAttendance.KEY_FROM,
                startDate.toString(),
                PassionAttendance.KEY_FROM,
                endDate.toString(),
                PassionAttendance.KEY_TO,
                startDate.toString(),
                PassionAttendance.KEY_TO,
                endDate.toString()
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Event> events = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String description = c.getString(c.getColumnIndex(PassionAttendance.KEY_DESCRIPTION));
                LocalDate from = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_FROM)));
                LocalDate to = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_TO)));

                events.add(new Event(id, title, description, from, to));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return events;
    }


    public ArrayList<Message> retrieveMessages() {
        SQLiteDatabase db = getReadableDatabase();
        String Query = "SELECT * FROM messages;";
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Message> messages = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String content = c.getString(c.getColumnIndex(PassionAttendance.KEY_CONTENT));
                LocalDate sent = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_SENT)));
                Integer type = c.getInt(c.getColumnIndex(PassionAttendance.KEY_TYPE));

                messages.add(new Message(id, title, content, type, sent));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return messages;
    }

    public ArrayList<Message> retrieveMessages(LocalDate d) {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT * FROM messages WHERE %s = \"%s\";",
                PassionAttendance.KEY_SENT,
                d.toString()
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Message> messages = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String content = c.getString(c.getColumnIndex(PassionAttendance.KEY_CONTENT));
                LocalDate sent = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_SENT)));
                Integer type = c.getInt(c.getColumnIndex(PassionAttendance.KEY_TYPE));

                messages.add(new Message(id, title, content, type, sent));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return messages;
    }

    public ArrayList<Message> retrieveMessages(LocalDate startDate, LocalDate endDate) {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT * FROM messages WHERE %s >= \"%s\" AND %s <= \"%s\";",
                PassionAttendance.KEY_SENT,
                startDate.toString(),
                PassionAttendance.KEY_SENT,
                endDate.toString()
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        ArrayList<Message> messages = new ArrayList<>();

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(PassionAttendance.KEY_ID));
                String title = c.getString(c.getColumnIndex(PassionAttendance.KEY_TITLE));
                String content = c.getString(c.getColumnIndex(PassionAttendance.KEY_CONTENT));
                LocalDate sent = new LocalDate(c.getString(c.getColumnIndex(PassionAttendance.KEY_SENT)));
                Integer type = c.getInt(c.getColumnIndex(PassionAttendance.KEY_TYPE));

                messages.add(new Message(id, title, content, type, sent));

                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return messages;
    }


    public Integer getMessagesCount(LocalDate d) {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT COUNT(*) AS %s FROM messages WHERE %s = \"%s\";",
                PassionAttendance.KEY_COUNT,
                PassionAttendance.KEY_SENT,
                d.toString()
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        try {
            c.moveToFirst();
            Integer count = c.getInt(c.getColumnIndex(PassionAttendance.KEY_COUNT));
            c.close();
            db.close();
            Log.i("Messages Count", count.toString());
            return count;
        } catch (CursorIndexOutOfBoundsException e) {
            c.close();
            db.close();
            return 0;
        }
    }

    public ArrayList<Integer> getMessagesCount(LocalDate startDate, LocalDate endDate) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> messageCount = new ArrayList<>();
        String Query = String.format(
                "SELECT %s, COUNT(*) AS %s FROM messages WHERE %s BETWEEN \"%s\" AND \"%s\" GROUP BY %s;",
                PassionAttendance.KEY_SENT,
                PassionAttendance.KEY_COUNT,
                PassionAttendance.KEY_SENT,
                startDate.toString(),
                endDate.toString(),
                PassionAttendance.KEY_SENT
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer count = c.getInt(c.getColumnIndex(PassionAttendance.KEY_COUNT));
                messageCount.add(count);
                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return messageCount;
    }

    public Integer getEventsCount() {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT COUNT(*) AS %s FROM events;",
                PassionAttendance.KEY_COUNT
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        try {
            c.moveToFirst();
            Integer count = c.getInt(c.getColumnIndex(PassionAttendance.KEY_COUNT));
            c.close();
            db.close();
            Log.i("Events Count", count.toString());
            return count;
        } catch (CursorIndexOutOfBoundsException e) {
            c.close();
            db.close();
            return 0;
        }
    }

    public Integer getMessagesCount() {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT COUNT(*) AS %s FROM messages;",
                PassionAttendance.KEY_COUNT
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        try {
            c.moveToFirst();
            Integer count = c.getInt(c.getColumnIndex(PassionAttendance.KEY_COUNT));
            c.close();
            db.close();
            Log.i("Messages Count", count.toString());
            return count;
        } catch (CursorIndexOutOfBoundsException e) {
            c.close();
            db.close();
            return 0;
        }
    }

    public Integer getEventsCount(LocalDate d) {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format(
                "SELECT COUNT(*) AS %s FROM events WHERE %s <= \"%s\" AND %s >= \"%s\";",
                PassionAttendance.KEY_COUNT,
                PassionAttendance.KEY_FROM,
                d.toString(),
                PassionAttendance.KEY_TO,
                d.toString()
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        try {
            c.moveToFirst();
            Integer count = c.getInt(c.getColumnIndex(PassionAttendance.KEY_COUNT));
            c.close();
            db.close();
            Log.i("Events Count", count.toString());
            return count;
        } catch (CursorIndexOutOfBoundsException e) {
            c.close();
            db.close();
            return 0;
        }
    }

    public ArrayList<Integer> getEventsCount(LocalDate startDate, LocalDate endDate) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> eventCount = new ArrayList<>();
        String Query = String.format(
                "SELECT %s, %s, COUNT(*) AS %s FROM messages WHERE " +
                        "(%s BETWEEN \"%s\" AND \"%s\") OR" +
                        "(%s BETWEEN \"%s\" AND \"%s\") GROUP BY %s;",
                PassionAttendance.KEY_FROM,
                PassionAttendance.KEY_TO,
                PassionAttendance.KEY_COUNT,
                PassionAttendance.KEY_FROM,
                startDate.toString(),
                endDate.toString(),
                PassionAttendance.KEY_TO,
                startDate.toString(),
                endDate.toString(),
                PassionAttendance.KEY_FROM
        );
        Log.i("Query", Query);

        Cursor c = db.rawQuery(Query, null);

        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Integer count = c.getInt(c.getColumnIndex(PassionAttendance.KEY_COUNT));
                eventCount.add(count);
                c.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return eventCount;
    }
    public boolean deleteEvent(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String Query = String.format(
                "DELETE FROM events WHERE %s = \"%d\";",
                PassionAttendance.KEY_ID,
                id
        );
        Log.i("Query", Query);

        try{
            db.execSQL(Query);
            db.close();
            return true;
        } catch (Exception e){
            db.close();
            return false;
        }
    }

    public boolean deleteMessage(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String Query = String.format(
                "DELETE FROM messages WHERE %s = \"%d\";",
                PassionAttendance.KEY_ID,
                id
        );
        Log.i("Query", Query);

        try{
            db.execSQL(Query);
            db.close();
            return true;
        } catch (Exception e){
            db.close();
            return false;
        }
    }
}
