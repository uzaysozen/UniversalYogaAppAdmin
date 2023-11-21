package com.example.universalyogaappadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YogaStudio";
    private SQLiteDatabase database;
    public static final int DATABASE_VERSION = 9;

    public static final String COURSE_TABLE_NAME = "courses";
    public static final String COURSE_ID_COLUMN = "courseID";
    public static final String DAY_OF_WEEK_COLUMN = "dayOfWeek";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String CAPACITY_COLUMN = "capacity";
    public static final String PRICE_COLUMN = "price";
    public static final String CLASS_TYPE_COLUMN = "classType";
    public static final String DESCRIPTION_COLUMN = "description";

    /******************************************************************/
    //Instance Properties
    public static final String INSTANCE_TABLE_NAME = "classInstance";
    public static final String INSTANCE_ID_COLUMN = "instanceID";
    public static final String DATE_COLUMN = "date";
    public static final String TEACHER_COLUMN = "teacherName";
    public static final String COMMENTS_COLUMN = "comments";

    private static final String CREATE_COURSE_TABLE = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT," +
            "%s TEXT," +
            "%s TEXT," +
            "%s TEXT," +
            "%s TEXT," +
            " %s TEXT)", COURSE_TABLE_NAME, COURSE_ID_COLUMN, DAY_OF_WEEK_COLUMN, COLUMN_NAME_TIME, CAPACITY_COLUMN, PRICE_COLUMN, CLASS_TYPE_COLUMN, DESCRIPTION_COLUMN);

    private static final String INSTANCE_TABLE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s INTEGER," +
                    "FOREIGN KEY(%s) REFERENCES %s (%s))", INSTANCE_TABLE_NAME, INSTANCE_ID_COLUMN, DATE_COLUMN, TEACHER_COLUMN, COMMENTS_COLUMN, COURSE_ID_COLUMN,COURSE_ID_COLUMN, COURSE_TABLE_NAME, COURSE_ID_COLUMN);


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(INSTANCE_TABLE_CREATE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        //super.onConfigure(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + INSTANCE_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + COURSE_TABLE_NAME);

        Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " - old data lost");

        onCreate(db);
    }

    public long insertDetails(String dayOfTheWeek, String time, String capacity, String price, String class_type, String description){
        ContentValues rowValues = new ContentValues();

        rowValues.put(DAY_OF_WEEK_COLUMN, dayOfTheWeek);
        rowValues.put(COLUMN_NAME_TIME, time);
        rowValues.put(CAPACITY_COLUMN, capacity);
        rowValues.put(PRICE_COLUMN, price);
        rowValues.put(CLASS_TYPE_COLUMN, class_type);
        rowValues.put(DESCRIPTION_COLUMN, description);

        return database.insertOrThrow(COURSE_TABLE_NAME, null, rowValues);
    }

    /*public String getCourseDetails(){
        Cursor results = database.query(COURSE_TABLE_NAME,
                new String[] {COURSE_ID_COLUMN, DAY_OF_WEEK_COLUMN, COLUMN_NAME_TIME, CAPACITY_COLUMN, PRICE_COLUMN, CLASS_TYPE_COLUMN, DESCRIPTION_COLUMN}, null,null,null,null,DAY_OF_WEEK_COLUMN);

        String resultText = "";

        results.moveToFirst();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String dayOfTheWeek = results.getString(1);
            String time = results.getString(2);
            String capacity = results.getString(3);
            String price = results.getString(4);
            String class_type = results.getString(5);
            String description = results.getString(6);

            resultText += id + " " + dayOfTheWeek + " " + time + " " + capacity + " " + price + " " + class_type + " " + description + "\n";

            results.moveToNext();
        }

        return resultText;
    }*/

    public String getCourseDetails() {
        Cursor results = database.query(COURSE_TABLE_NAME,
                new String[] {COURSE_ID_COLUMN, DAY_OF_WEEK_COLUMN, COLUMN_NAME_TIME, CAPACITY_COLUMN, PRICE_COLUMN, CLASS_TYPE_COLUMN, DESCRIPTION_COLUMN}, null, null, null, null, DAY_OF_WEEK_COLUMN);

        JSONArray coursesArray = new JSONArray();

        results.moveToFirst();
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String dayOfTheWeek = results.getString(1);
            String time = results.getString(2);
            String capacity = results.getString(3);
            String price = results.getString(4);
            String classType = results.getString(5);
            String description = results.getString(6);

            JSONObject courseObject = new JSONObject();
            try {
                courseObject.put("id", id);
                courseObject.put("dayOfTheWeek", dayOfTheWeek);
                courseObject.put("time", time);
                courseObject.put("capacity", capacity);
                courseObject.put("price", price);
                courseObject.put("classType", classType);
                courseObject.put("description", description);
            } catch (Exception e) {
                throw new RuntimeException();
            }
            coursesArray.put(courseObject);

            results.moveToNext();
        }

        return coursesArray.toString();
    }
}

