package com.example.myfirstapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuckyMobileDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BuckyMobile1.db";

    public BuckyMobileDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STEPS_ENTRIES);
        db.execSQL(SQL_CREATE_WATCH_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STEPS_ENTRIES);
        db.execSQL(SQL_DELETE_WATCH_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void deleteDatabase(Context mContext) { mContext.deleteDatabase(DATABASE_NAME);}

    /* BEGIN SQL Strings */
    // Steps Data
    private static final String SQL_CREATE_STEPS_ENTRIES =
            "CREATE TABLE " + StepsInstanceContract.StepsEntry.TABLE_NAME + " (" +
                    StepsInstanceContract.StepsEntry.COLUMN_NAME_DATE + " TEXT," +
                    StepsInstanceContract.StepsEntry.COLUMN_NAME_STEPS + " INTEGER," +
                    StepsInstanceContract.StepsEntry.COLUMN_NAME_HAS_POSTED + " INTEGER)";

    private static final String SQL_DELETE_STEPS_ENTRIES =
            "DROP TABLE IF EXISTS " + StepsInstanceContract.StepsEntry.TABLE_NAME;

    // TODO: Stopwatch Data
    private static final String SQL_CREATE_WATCH_ENTRIES =
            "CREATE TABLE " + WatchInstanceContract.WatchEntry.TABLE_NAME + " (" +
                    WatchInstanceContract.WatchEntry.COLUMN_NAME_DATE + " TEXT," +
                    WatchInstanceContract.WatchEntry.COLUMN_NAME_SECOND + " INTEGER," +
                    WatchInstanceContract.WatchEntry.COLUMN_NAME_HAS_POSTED + " INTEGER)";

    private static final String SQL_DELETE_WATCH_ENTRIES =
            "DROP TABLE IF EXISTS " + WatchInstanceContract.WatchEntry.TABLE_NAME;
}
