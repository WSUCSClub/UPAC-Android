package com.upac.upacapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CKosidowski11 on 2/12/2015.
 */
public class NotifySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notify_entries";
    private static final String EVENT_ID = "event_id";
    private static final String[] COLUMNS = {EVENT_ID};

    public NotifySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create notify table
        String CREATE_NOTIFY_TABLE = "CREATE TABLE " + DATABASE_NAME + " ( " +
                "event_id DOUBLE PRIMARY KEY UNIQUE)";

        // create notify table
        db.execSQL(CREATE_NOTIFY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS raffle_entries");

        this.onCreate(db);
    }

    public String getEntries(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String eventID = null;

        Cursor cursor =
                db.query(DATABASE_NAME, // a. table
                        COLUMNS, // b. column names
                        " event_id = ?", // c. selections
                        new String[]{String.valueOf(eventId)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        try {
            eventID = cursor.getString(cursor.getColumnIndex(EVENT_ID));
        } catch (CursorIndexOutOfBoundsException e) {

        }

        db.close();

        return eventID;
    }

    public void addEntry(String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EVENT_ID, eventId); // get title

        db.insert(DATABASE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        db.close();
    }
}