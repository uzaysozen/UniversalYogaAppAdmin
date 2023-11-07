package com.example.universalyogaappadmin;

import android.provider.BaseColumns;
public final class DatabaseContract {

    private DatabaseContract(){}
    public static class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CAPACITY = "capacity";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_CLASS_TYPE = "class_type";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

    }
}
