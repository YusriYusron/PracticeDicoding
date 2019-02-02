package com.yusriyusron.practice.sqllite.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NOTE = "note";

    public static final class NoteColums implements BaseColumns{
        // Note Title
        public static String TITLE = "title";
        // Note Description
        public static String DESCRIPTION = "description";
        // Note Date
        public static String DATE = "date";
    }
}
