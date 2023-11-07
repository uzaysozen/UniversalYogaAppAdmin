package com.example.universalyogaappadmin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Admin_Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YogaStudio.db";
    public static final int DATABASE_VERSION = 1;
    public Admin_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.CourseEntry.TABLE_NAME + " (" +
                    DatabaseContract.CourseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_DAY_OF_WEEK + " TEXT," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_TIME + " TEXT," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_CAPACITY + " INTEGER," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_DURATION + " INTEGER," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_PRICE + " REAL," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_CLASS_TYPE + " TEXT," +
                    DatabaseContract.CourseEntry.COLUMN_NAME_DESCRIPTION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.CourseEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
