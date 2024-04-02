package com.example.ccr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookings.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_BOOKINGS = "Filed_Booking";
    private static final String COLUMN_ID = "Student_id";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TIME_SLOT = "Time_slot";
    private static final String createTableQuery = "CREATE TABLE " + TABLE_BOOKINGS + " (" + COLUMN_ID + " VARCHAR(250), " + COLUMN_DATE + " TEXT, " + COLUMN_TIME_SLOT + " TEXT)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BOOKINGS;
    private static final String SELTECT_ALL = "SELECT * FROM " + TABLE_BOOKINGS;
    Context context;


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context, "onCreate method is called", Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(createTableQuery);
        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            Toast.makeText(context, "onUpgrade method is called", Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e, Toast.LENGTH_LONG).show();
        }
    }

    public long insertData(String id, String date, String timeSlot) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME_SLOT, timeSlot);

        long rowID = sqLiteDatabase.insert(TABLE_BOOKINGS, null, contentValues);
        return rowID;

    }

    public Cursor displayAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELTECT_ALL, null);
        return cursor;
    }


}