package com.example.universalyogaappadmin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YogaStudio";
    private SQLiteDatabase database;
    public static final int DATABASE_VERSION = 1;



    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE YogaStudio (" +
            " Course_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " COLUMN_NAME_DAY_OF_WEEK TEXT, " +
            " COLUMN_NAME_TIME INTEGER," +
            " COLUMN_NAME_CAPACITY INTEGER," +
            " COLUMN_NAME_DURATION INTEGER," +
            " COLUMN_NAME_PRICE INTEGER," +
            " COLUMN_NAME_CLASS_TYPE TEXT," +
            " COLUMN_NAME_DESCRIPTION TEXT),");


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_NAME);

        Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " - old data lost");

        onCreate(db);
    }

    public long insertDetails(String dayOfTheWeek, String ,)
}
