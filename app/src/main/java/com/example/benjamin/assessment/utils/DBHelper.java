package com.example.benjamin.assessment.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // database constants
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    // constants for term
    public static final String TABLE_TERMS = "terms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_START = "starts";
    public static final String COLUMN_END = "ends";

    // constants for course
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TERM_ID = "termId";

    // constants for notes
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_COURSE_ID = "courseId";

    // constants for mentor
    public static final String TABLE_MENTORS = "mentors";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    // constants for assessment
    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DUE = "due";

    // SQL to create term table
    private static final String TERM_DATABASE_CREATE = "create table " + TABLE_TERMS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_START + " integer not null, "
            + COLUMN_END + " integer not null);";

    // SQL to create course table
    private static final String COURSE_DATABASE_CREATE = "create table " + TABLE_COURSES + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_START + " integer not null, "
            + COLUMN_END + " integer not null, "
            + COLUMN_STATUS + " text not null, "
            + COLUMN_TERM_ID + " integer not null, "
            + "foreign key(" + COLUMN_TERM_ID + ") references " + TABLE_TERMS + "(" + COLUMN_ID + ") "
            + "on delete cascade on update cascade);";

    // SQL to create note table
    private static final String NOTE_DATABASE_CREATE = "create table " + TABLE_NOTES + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TEXT + " text not null, "
            + COLUMN_COURSE_ID + " integer not null, "
            + "foreign key(" + COLUMN_COURSE_ID + ") references " + TABLE_COURSES + "(" + COLUMN_ID + ") "
            + "on delete cascade on update cascade);";

    // SQL to create mentor table
    private static final String MENTOR_DATABASE_CREATE = "create table " + TABLE_MENTORS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PHONE + " text not null, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_COURSE_ID + " integer not null, "
            + "foreign key(" + COLUMN_COURSE_ID + ") references " + TABLE_COURSES + "(" + COLUMN_ID + ") "
            + "on delete cascade on update cascade);";

    // SQL to create assessment table
    private static final String ASSESSMENT_DATABASE_CREATE = "create table " + TABLE_ASSESSMENTS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_DUE + " integer not null, "
            + COLUMN_COURSE_ID + " integer not null, "
            + "foreign key(" + COLUMN_COURSE_ID + ") references " + TABLE_COURSES + "(" + COLUMN_ID + ") "
            + "on delete cascade on update cascade);";

    // constructor for database helper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // builds tables when database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERM_DATABASE_CREATE);
        db.execSQL(COURSE_DATABASE_CREATE);
        db.execSQL(NOTE_DATABASE_CREATE);
        db.execSQL(MENTOR_DATABASE_CREATE);
        db.execSQL(ASSESSMENT_DATABASE_CREATE);
    }

    // handle upgrading to new version of sqlite
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        onCreate(db);
    }
}
