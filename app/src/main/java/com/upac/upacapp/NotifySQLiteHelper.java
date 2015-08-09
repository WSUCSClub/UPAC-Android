package com.upac.upacapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotifySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notify_entries";
    private static final String EVENT_ID = "event_id";
    private static final String[] COLUMNS = {EVENT_ID};

    public NotifySQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        // SQL statement to create notify table
        final String CREATE_NOTIFY_TABLE = "CREATE TABLE " + DATABASE_NAME + " ( " + "event_id DOUBLE PRIMARY KEY UNIQUE)";

        // create notify table
        db.execSQL(CREATE_NOTIFY_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS raffle_entries");

        this.onCreate(db);
    }

    public String getEntries(final String eventId) {
        SQLiteDatabase db = null;
        String eventID = null;

        try {
            db = this.getReadableDatabase();
            final Cursor cursor =
                    db.query(DATABASE_NAME, // a. table
                            COLUMNS, // b. column names
                            " event_id = ?", // c. selections
                            new String[]{String.valueOf(eventId)}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

            if (cursor != null) {
                cursor.moveToFirst();
            }

            eventID = cursor.getString(cursor.getColumnIndex(EVENT_ID));
        } catch (final CursorIndexOutOfBoundsException e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return eventID;
    }

    public void addEntry(final String eventId) {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(EVENT_ID, eventId); // get title

        db.insert(DATABASE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        db.close();
    }
}