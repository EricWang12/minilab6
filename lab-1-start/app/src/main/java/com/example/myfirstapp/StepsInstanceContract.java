package com.example.myfirstapp;
import android.provider.BaseColumns;
public class StepsInstanceContract {
    public StepsInstanceContract() {
// blank constructor
    }
    public static class StepsEntry implements BaseColumns {
        public static final String TABLE_NAME = "stepsinstances";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_STEPS = "steps";
        public static final String COLUMN_NAME_HAS_POSTED = "hasPosted";
    }
}