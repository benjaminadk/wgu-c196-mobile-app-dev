package com.example.benjamin.assessment.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.assessment.models.Assessment;
import com.example.benjamin.assessment.models.Course;
import com.example.benjamin.assessment.models.Mentor;
import com.example.benjamin.assessment.models.Note;
import com.example.benjamin.assessment.models.Term;

import java.util.ArrayList;

public class DataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    // terms columns
    private String[] allColumnsTerms = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_START,
            DBHelper.COLUMN_END
    };

    // courses columns
    private String[] allColumnsCourses = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_START,
            DBHelper.COLUMN_END,
            DBHelper.COLUMN_STATUS,
            DBHelper.COLUMN_TERM_ID
    };

    // notes columns
    private String[] allColumnsNotes = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TEXT,
            DBHelper.COLUMN_COURSE_ID
    };

    // mentor columns
    private String[] allColumnsMentor = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_NAME,
            DBHelper.COLUMN_PHONE,
            DBHelper.COLUMN_EMAIL,
            DBHelper.COLUMN_COURSE_ID
    };

    // assessment columns
    private String[] allColumnsAssessment = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_TYPE,
            DBHelper.COLUMN_DUE,
            DBHelper.COLUMN_COURSE_ID,
    };

    // constructor
    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    // opens sqlite database
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // closes database
    public void close() {
        dbHelper.close();
    }

    // term logic
    // creates a new term
    public Term createTerm(String title, long start, long end) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_START, start);
        values.put(DBHelper.COLUMN_END, end);
        long insertId = database.insert(DBHelper.TABLE_TERMS, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_TERMS, allColumnsTerms, DBHelper.COLUMN_ID
                + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        Term newTerm = cursorToTerm(cursor);
        cursor.close();
        return newTerm;
    }

    // update an existing term
    public boolean updateTerm(long id, String title, long start, long end) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_START, start);
        values.put(DBHelper.COLUMN_END, end);
        String whereClause = DBHelper.COLUMN_ID + " = " + id;
        int rows = database.update(DBHelper.TABLE_TERMS, values, whereClause, null);
        return rows == 1;
    }

    // fetches and returns all terms
    public ArrayList<Term> getAllTerms() {
        ArrayList<Term> terms = new ArrayList<Term>();
        Cursor cursor = database.query(DBHelper.TABLE_TERMS, allColumnsTerms, null,
                null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Term term = cursorToTerm(cursor);
            terms.add(term);
            cursor.moveToNext();
        }
        cursor.close();
        return terms;
    }

    // fetches term by id
    public Term getTermById(long id) {
        String selection = DBHelper.COLUMN_ID + " = " + id;
        Cursor cursor = database.query(DBHelper.TABLE_TERMS, allColumnsTerms, selection,
                null, null, null, null);
        cursor.moveToFirst();
        Term term = cursorToTerm(cursor);
        cursor.close();
        return term;
    }

    // delete term
    public boolean deleteTerm(long id) {
        boolean success;
        String selection = "termId = " + id;
        String whereClause = "_id = " + id;
        Cursor cursor = database.query(DBHelper.TABLE_COURSES, allColumnsCourses, selection,
                    null,null,null,null);
        if (cursor.getCount() > 0) {
            success = false;
        } else {
            database.delete(DBHelper.TABLE_TERMS, whereClause,null);
            success = true;
        }
        cursor.close();
        return success;
    }

    // helper method to create Term instances
    private Term cursorToTerm(Cursor cursor) {
        Term term = new Term();
        term.setId(cursor.getLong(0));
        term.setTitle(cursor.getString(1));
        term.setStart(cursor.getLong(2));
        term.setEnd(cursor.getLong(3));
        return term;
    }

    // course logic
    // creates new course
    public Course createCourse(String title, long start, long end, String status, long termId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_START, start);
        values.put(DBHelper.COLUMN_END, end);
        values.put(DBHelper.COLUMN_STATUS, status);
        values.put(DBHelper.COLUMN_TERM_ID, termId);
        long insertId = database.insert(DBHelper.TABLE_COURSES, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_COURSES, allColumnsCourses, DBHelper.COLUMN_ID
                + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        Course newCourse = cursorToCourse(cursor);
        cursor.close();
        return newCourse;
    }

    // update course
    public boolean updateCourse(long id, String title, long start, long end, String status) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_START, start);
        values.put(DBHelper.COLUMN_END, end);
        values.put(DBHelper.COLUMN_STATUS, status);
        String whereClause = DBHelper.COLUMN_ID + " = " + id;
        int rows = database.update(DBHelper.TABLE_COURSES, values, whereClause, null);
        return rows == 1;
    }

    // fetch and return courses for a given term
    public ArrayList<Course> getCoursesForTerm(long termId) {
        ArrayList<Course> courses = new ArrayList<Course>();
        String selection = "termId = " + termId;
        Cursor cursor = database.query(DBHelper.TABLE_COURSES, allColumnsCourses, selection,
                null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Course course = cursorToCourse(cursor);
            courses.add(course);
            cursor.moveToNext();
        }
        cursor.close();
        return courses;
    }

    // fetch course by id
    public Course getCourseById(long id) {
        String selection = DBHelper.COLUMN_ID + " = " + id;
        Cursor cursor = database.query(DBHelper.TABLE_COURSES, allColumnsCourses, selection,
                null,null,null,null);
        cursor.moveToFirst();
        Course course = cursorToCourse(cursor);
        cursor.close();
        return course;
    }

    // delete course
    public boolean deleteCourse(long id) {
        String whereClause = "_id = " + id;
        database.delete(DBHelper.TABLE_COURSES, whereClause,null);
        return true;
    }

    // helper method to create Course instances
    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setId(cursor.getLong(0));
        course.setTitle(cursor.getString(1));
        course.setStart(cursor.getLong(2));
        course.setEnd(cursor.getLong(3));
        course.setStatus(cursor.getString(4));
        course.setTermId(cursor.getLong(5));
        return course;
    }

    // note logic
    // create note or overwrite note
    public Note createNote(String text, long courseId) {
        ContentValues values = new ContentValues();
        Cursor cursor;
        String selection = DBHelper.COLUMN_COURSE_ID + " = " + courseId;
        Cursor noteCursor = database.query(DBHelper.TABLE_NOTES, allColumnsNotes, selection,
                null, null, null, null);
        if (noteCursor.getCount() == 0) {
            noteCursor.close();
            values.put(DBHelper.COLUMN_TEXT, text);
            values.put(DBHelper.COLUMN_COURSE_ID, courseId);
            long insertId = database.insert(DBHelper.TABLE_NOTES, null, values);
            cursor = database.query(DBHelper.TABLE_NOTES, allColumnsNotes, DBHelper.COLUMN_ID
                    + " = " + insertId, null,null,null,null);
            cursor.moveToFirst();
            Note newNote = cursorToNote(cursor);
            cursor.close();
            return newNote;
        } else {
            noteCursor.moveToFirst();
            long noteId = noteCursor.getLong(0);
            String whereClause = DBHelper.COLUMN_ID + " = " + noteId;
            values.put(DBHelper.COLUMN_TEXT, text);
            values.put(DBHelper.COLUMN_COURSE_ID, courseId);
            database.update(DBHelper.TABLE_NOTES, values, whereClause, null);
            cursor = database.query(DBHelper.TABLE_NOTES, allColumnsNotes, whereClause,
                    null, null, null, null);
            cursor.moveToFirst();
            Note updatedNote = cursorToNote(cursor);
            cursor.close();
            return updatedNote;
        }
    }

    // fetch note for course
    public Note getNoteForCourse(long courseId) {
        String selection = DBHelper.COLUMN_COURSE_ID + " = " + courseId;
        Cursor cursor = database.query(DBHelper.TABLE_NOTES, allColumnsNotes, selection,
                null,null,null,null);
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        Note note = cursorToNote(cursor);
        cursor.close();
        return note;
    }

    // helper method to create Note instances
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setText(cursor.getString(1));
        note.setCourseId(cursor.getLong(2));
        return note;
    }

    // mentor logic
    // create mentor
    public Mentor createMentor(String name, String phone, String email, long courseId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_PHONE, phone);
        values.put(DBHelper.COLUMN_EMAIL, email);
        values.put(DBHelper.COLUMN_COURSE_ID, courseId);
        long insertId = database.insert(DBHelper.TABLE_MENTORS, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_MENTORS, allColumnsMentor, DBHelper.COLUMN_ID
                + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        Mentor newMentor = cursorToMentor(cursor);
        cursor.close();
        return newMentor;
    }

    // fetch mentors for given course
    public ArrayList<Mentor> getMentorsForCourse(long courseId) {
        ArrayList<Mentor> mentors = new ArrayList<Mentor>();
        String selection = DBHelper.COLUMN_COURSE_ID + " = " + courseId;
        Cursor cursor = database.query(DBHelper.TABLE_MENTORS, allColumnsMentor, selection,
                null,null,null,null);
        if (cursor.getCount() == 0) return mentors;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Mentor mentor = cursorToMentor(cursor);
            mentors.add(mentor);
            cursor.moveToNext();
        }
        cursor.close();
        return mentors;
    }

    // fetch mentor by id
    public Mentor getMentorById(long id) {
        String selection = DBHelper.COLUMN_ID + " = " + id;
        Cursor cursor = database.query(DBHelper.TABLE_MENTORS, allColumnsMentor, selection,
                null,null,null,null);
        cursor.moveToFirst();
        Mentor mentor = cursorToMentor(cursor);
        cursor.close();
        return mentor;
    }

    // update mentor
    public boolean updateMentor(long id, String name, String phone, String email) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_PHONE, phone);
        values.put(DBHelper.COLUMN_EMAIL, email);
        String whereClause = DBHelper.COLUMN_ID + " = " + id;
        int rows = database.update(DBHelper.TABLE_MENTORS, values, whereClause, null);
        return rows == 1;
    }

    // delete mentor
    public boolean deleteMentor(long id) {
        String whereClause = "_id = " + id;
        database.delete(DBHelper.TABLE_MENTORS, whereClause,null);
        return true;
    }

    // helper to create Mentor instances
    private Mentor cursorToMentor(Cursor cursor) {
        Mentor mentor = new Mentor();
        mentor.setId(cursor.getLong(0));
        mentor.setName(cursor.getString(1));
        mentor.setPhone(cursor.getString(2));
        mentor.setEmail(cursor.getString(3));
        mentor.setCourseId(cursor.getLong(4));
        return mentor;
    }

    // assessment logic
    // create assessment
    public Assessment createAssessment(String title, String type, long due, long courseId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_TYPE, type);
        values.put(DBHelper.COLUMN_DUE, due);
        values.put(DBHelper.COLUMN_COURSE_ID, courseId);
        long insertId = database.insert(DBHelper.TABLE_ASSESSMENTS, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_ASSESSMENTS, allColumnsAssessment, DBHelper.COLUMN_ID
                + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        Assessment newAssessment = cursorToAssessment(cursor);
        cursor.close();
        return newAssessment;
    }

    // update assessment
    public boolean updateAssessment(long id, String title, String type, long due) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_TYPE, type);
        values.put(DBHelper.COLUMN_DUE, due);
        String whereClause = DBHelper.COLUMN_ID + " = " + id;
        int rows = database.update(DBHelper.TABLE_ASSESSMENTS, values, whereClause, null);
        return rows == 1;
    }

    // fetches assessments for given course
    public ArrayList<Assessment> getAssessmentsForCourse(long courseId) {
        ArrayList<Assessment> assessments = new ArrayList<Assessment>();
        String selection = DBHelper.COLUMN_COURSE_ID + " = " + courseId;
        Cursor cursor = database.query(DBHelper.TABLE_ASSESSMENTS, allColumnsAssessment, selection,
                null,null,null,null);
        if (cursor.getCount() == 0) return assessments;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Assessment assessment = cursorToAssessment(cursor);
            assessments.add(assessment);
            cursor.moveToNext();
        }
        cursor.close();
        return assessments;
    }

    // fetches assessment by id
    public Assessment getAssessmentById(long id) {
        String selection = DBHelper.COLUMN_ID + " = " + id;
        Cursor cursor = database.query(DBHelper.TABLE_ASSESSMENTS, allColumnsAssessment, selection,
                null,null,null,null);
        cursor.moveToFirst();
        Assessment assessment = cursorToAssessment(cursor);
        cursor.close();
        return assessment;
    }

    // delete assessment
    public boolean deleteAssessment(long id) {
        String whereClause = "_id = " + id;
        database.delete(DBHelper.TABLE_ASSESSMENTS, whereClause,null);
        return true;
    }

    // helper to create Assessment instances
    private Assessment cursorToAssessment(Cursor cursor) {
        Assessment assessment = new Assessment();
        assessment.setId(cursor.getLong(0));
        assessment.setTitle(cursor.getString(1));
        assessment.setType(cursor.getString(2));
        assessment.setDue(cursor.getLong(3));
        assessment.setCourseId(cursor.getLong(4));
        return assessment;
    }
}
