package com.example.universalyogaappadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YogaStudio";
    private SQLiteDatabase database;
    public static final int DATABASE_VERSION = 14;

    /******************************************************************/
    //Course Properties
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
    public static final String INSTANCE_USER_ID = "userID";
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
                    "%s TEXT," +
                    "%s INTEGER," +
                    "FOREIGN KEY(%s) REFERENCES %s (%s) ON DELETE CASCADE)", INSTANCE_TABLE_NAME, INSTANCE_ID_COLUMN, INSTANCE_USER_ID, DATE_COLUMN, TEACHER_COLUMN, COMMENTS_COLUMN, COURSE_ID_COLUMN,COURSE_ID_COLUMN, COURSE_TABLE_NAME, COURSE_ID_COLUMN);


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

    public long insertCourseDetails(Integer courseId, String dayOfTheWeek, String time, String capacity, String price, String class_type, String description, Boolean update){
        ContentValues rowValues = new ContentValues();

        rowValues.put(DAY_OF_WEEK_COLUMN, dayOfTheWeek);
        rowValues.put(COLUMN_NAME_TIME, time);
        rowValues.put(CAPACITY_COLUMN, capacity);
        rowValues.put(PRICE_COLUMN, price);
        rowValues.put(CLASS_TYPE_COLUMN, class_type);
        rowValues.put(DESCRIPTION_COLUMN, description);
        if (update) {
            return database.update(COURSE_TABLE_NAME, rowValues, "courseID=?", new String[]{Integer.toString(courseId)});
        }
        else {
            return database.insertOrThrow(COURSE_TABLE_NAME, null, rowValues);
        }

    }

    public long deleteCourse(int courseId) {
        return database.delete(COURSE_TABLE_NAME, "courseID=?", new String[]{Integer.toString(courseId)});
    }

    public JSONObject getCourseById(int courseId) {
        Cursor results = database.query(COURSE_TABLE_NAME,
                new String[] {COURSE_ID_COLUMN, DAY_OF_WEEK_COLUMN, COLUMN_NAME_TIME, CAPACITY_COLUMN, PRICE_COLUMN, CLASS_TYPE_COLUMN, DESCRIPTION_COLUMN}, "courseID=?", new String[]{Integer.toString(courseId)}, null, null, DAY_OF_WEEK_COLUMN);
        results.moveToFirst();
        return getCourseObject(results);

    }

    public JSONArray getCourseDetails() {
        Cursor results = database.query(COURSE_TABLE_NAME,
                new String[] {COURSE_ID_COLUMN, DAY_OF_WEEK_COLUMN, COLUMN_NAME_TIME, CAPACITY_COLUMN, PRICE_COLUMN, CLASS_TYPE_COLUMN, DESCRIPTION_COLUMN}, null, null, null, null, DAY_OF_WEEK_COLUMN);

        JSONArray coursesArray = new JSONArray();

        results.moveToFirst();
        while (!results.isAfterLast()) {
            coursesArray.put(getCourseObject(results));
            results.moveToNext();
        }
        return coursesArray;
    }

    private JSONObject getCourseObject(Cursor cursor) {
        int id = cursor.getInt(0);
        String dayOfTheWeek = cursor.getString(1);
        String time = cursor.getString(2);
        String capacity = cursor.getString(3);
        String price = cursor.getString(4);
        String classType = cursor.getString(5);
        String description = cursor.getString(6);
        JSONObject courseObject = new JSONObject();
        try {
            courseObject.put("id", id);
            courseObject.put("dayOfWeek", dayOfTheWeek);
            courseObject.put("time", time);
            courseObject.put("capacity", capacity);
            courseObject.put("price", price);
            courseObject.put("classType", classType);
            courseObject.put("description", description);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return courseObject;
    }

    public long insertClassDetails(Integer classId, String userID, String classDate, String teacherName, String comments, Integer courseId, Boolean update){
        ContentValues rowValues = new ContentValues();

        rowValues.put(INSTANCE_USER_ID, userID);
        rowValues.put(DATE_COLUMN, classDate);
        rowValues.put(TEACHER_COLUMN, teacherName);
        rowValues.put(COMMENTS_COLUMN, comments);
        rowValues.put(COURSE_ID_COLUMN, courseId);
        if (update) {
            return database.update(INSTANCE_TABLE_NAME, rowValues, "instanceID=?", new String[]{Integer.toString(classId)});
        }
        else {
            return database.insertOrThrow(INSTANCE_TABLE_NAME, null, rowValues);
        }

    }

    public long deleteClass(int classId) {
        return database.delete(INSTANCE_TABLE_NAME, "instanceID=?", new String[]{Integer.toString(classId)});
    }

    public JSONObject getClassById(int classId) {
        Cursor results = database.query(INSTANCE_TABLE_NAME,
                new String[] {INSTANCE_ID_COLUMN, INSTANCE_USER_ID, DATE_COLUMN, TEACHER_COLUMN, COMMENTS_COLUMN, COURSE_ID_COLUMN}, "instanceID=?", new String[]{Integer.toString(classId)}, null, null, DATE_COLUMN);
        results.moveToFirst();
        return getClassObject(results);

    }

    public JSONArray getClassDetails(int courseId) {
        Cursor results = database.query(INSTANCE_TABLE_NAME,
                new String[] {INSTANCE_ID_COLUMN, INSTANCE_USER_ID, DATE_COLUMN, TEACHER_COLUMN, COMMENTS_COLUMN, COURSE_ID_COLUMN}, "courseId=?", new String[]{Integer.toString(courseId)}, null, null, DATE_COLUMN);

        JSONArray classArray = new JSONArray();

        results.moveToFirst();
        while (!results.isAfterLast()) {
            classArray.put(getClassObject(results));
            results.moveToNext();
        }
        return classArray;
    }

    private JSONObject getClassObject(Cursor cursor) {
        int id = cursor.getInt(0);
        String userID = cursor.getString(1);
        String date = cursor.getString(2);
        String teacherName = cursor.getString(3);
        String comments = cursor.getString(4);
        String courseId = cursor.getString(5);
        JSONObject classObject = new JSONObject();
        try {
            classObject.put("id", id);
            classObject.put("userID", userID);
            classObject.put("date", date);
            classObject.put("teacherName", teacherName);
            classObject.put("comments", comments);
            classObject.put("courseID", courseId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return classObject;
    }
}



