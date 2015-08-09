package com.upac.upacapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RaffleSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "raffle_entries";
    private static final String EVENT_ID = "event_id";
    private static final String TICKET_ID = "ticket_id";
    private static final String[] COLUMNS = {EVENT_ID, TICKET_ID};

    public RaffleSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        final String CREATE_RAFFLE_TABLE = "CREATE TABLE raffle_entries ( " +
                "event_id DOUBLE PRIMARY KEY UNIQUE, " +
                "ticket_id STRING)";

        db.execSQL(CREATE_RAFFLE_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS raffle_entries");

        this.onCreate(db);
    }

    public String getEntries(final String eventId) {
        final SQLiteDatabase db = this.getReadableDatabase();
        String ticketID = null;

        try {
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

            ticketID = cursor.getString(cursor.getColumnIndex(TICKET_ID));
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("There are no entries");
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return ticketID;
    }

    public void addEntry(final String eventId, final String entryId) {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(EVENT_ID, eventId);
        values.put(TICKET_ID, entryId);

        db.insert(DATABASE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        db.close();
    }
}