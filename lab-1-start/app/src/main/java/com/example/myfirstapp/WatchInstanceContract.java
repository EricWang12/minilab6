package com.example.myfirstapp;

import android.provider.BaseColumns;

public class WatchInstanceContract {
    public WatchInstanceContract() {
        // blank constructor
    }
    public static class WatchEntry implements BaseColumns {
        public static final String TABLE_NAME = "stopwatchInstances";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_SECOND = "second";
        public static final String COLUMN_NAME_HAS_POSTED = "hasPosted";
    }
}
