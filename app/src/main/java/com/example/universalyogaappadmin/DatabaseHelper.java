package com.example.universalyogaappadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YogaStudio";
    private SQLiteDatabase database;
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "courses";
    public static final String ID_COLUMN = "courseID";
    public static final String DAY_OF_WEEK_COLUMN = "day_of_week";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String CAPACITY_COLUMN = "capacity";
    public static final String COLUMN_NAME_DURATION = "duration";
    public static final String PRICE_COLUMN = "price";
    public static final String CLASS_TYPE_COLUMN = "class_type";
    public static final String DESCRIPTION_COLUMN = "description";

    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
            " %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " %s TEXT, " +
            " %s TEXT," +
            " %s TEXT," +
            " %s TEXT," +
            " %s TEXT)", TABLE_NAME, ID_COLUMN, DAY_OF_WEEK_COLUMN, CAPACITY_COLUMN, PRICE_COLUMN, CLASS_TYPE_COLUMN, DESCRIPTION_COLUMN);


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

    public long insertDetails(String dayOfTheWeek, String capacity, String price, String class_type, String description){
        ContentValues rowValues = new ContentValues();

        rowValues.put(DAY_OF_WEEK_COLUMN, dayOfTheWeek);
        //rowValues.put(COLUMN_NAME_TIME, time);
        rowValues.put(CAPACITY_COLUMN, capacity);
        //rowValues.put(COLUMN_NAME_DURATION, duration);
        rowValues.put(PRICE_COLUMN, price);
        rowValues.put(CLASS_TYPE_COLUMN, class_type);
        rowValues.put(DESCRIPTION_COLUMN, description);

        return database.insertOrThrow(DATABASE_NAME, null, rowValues);
    }

    public String getDetails(){
        Cursor results = database.query("details",
                new String[] {"dayOfTheWeek", " time", "capacity", "duration", "price", "class_type", "description"}, null,null,null,null,"dayOfTheWeek");

        String resultText = "";

        results.moveToFirst();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String dayOfTheWeek = results.getString(1);
            String time = results.getString(2);
            String capacity = results.getString(3);
            String duration = results.getString(4);
            String price = results.getString(5);
            String class_type = results.getString(6);
            String description = results.getString(7);

            resultText += id + " " + dayOfTheWeek + " " + time + " " + capacity + " " + duration + " " + price + " " + class_type + " " + description + "\n";

            results.moveToNext();
        }

        return  resultText;
    }
}

