package com.example.benjamin.assessment.activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.fragments.CourseDetailFragment;
import com.example.benjamin.assessment.fragments.CourseMentorFragment;
import com.example.benjamin.assessment.fragments.CourseNoteFragment;
import com.example.benjamin.assessment.fragments.MentorDialogFragment;
import com.example.benjamin.assessment.fragments.NoteDialogFragment;
import com.example.benjamin.assessment.models.Course;
import com.example.benjamin.assessment.models.Mentor;
import com.example.benjamin.assessment.models.Note;
import com.example.benjamin.assessment.utils.DataSource;
import com.example.benjamin.assessment.utils.MyReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class CourseDetailActivity extends AppCompatActivity
                implements NoteDialogFragment.NoteDialogListener,
                           MentorDialogFragment.MentorDialogListener{

    long course_id;
    Note note;
    Mentor mentor;
    ArrayList<Mentor> mentors;
    Course course;

    private DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // get course id
        course_id = getIntent().getLongExtra("COURSE_ID", readFromSavedPreferences());

        // database
        dataSource = new DataSource(this);
        dataSource.open();
        course = dataSource.getCourseById(course_id);
        mentors = dataSource.getMentorsForCourse(course_id);

        // populate text fields in fragment
        populateDetails();

        // populate mentors if there are any
        if (mentors != null) {
            populateMentors();
        }

        // populate note fragment if there is a note
        if (course_id != 0) {
            note = dataSource.getNoteForCourse(course_id);
            writeToSavedPreferences();
            if(note != null) {
                populateNote();
            }
        }

        // assign title and enable action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_assessment:
                openAssessmentCreate();
                return true;
            case R.id.action_add_notes:
                openNoteDialog();
                return true;
            case R.id.action_add_mentor:
                openMentorDialog();
                return true;
            case R.id.action_edit_course:
                openEditCourseActivity();
                return true;
            case R.id.action_delete_course:
                handleDeleteCourse();
                return true;
            case R.id.action_view_assessments:
                openAssessmentList();
                return true;
            case R.id.action_start_alert:
                createStartAlert();
                return true;
            case R.id.action_end_alert:
                createEndAlert();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // open assessment create activity
    public void openAssessmentCreate() {
        Intent intent = new Intent(this, AssessmentCreateActivity.class);
        intent.putExtra("COURSE_ID", course_id);
        startActivity(intent);
    }

    // opens the dialog to add new note
    public void openNoteDialog() {
        DialogFragment dialog = new NoteDialogFragment();
        dialog.show(getFragmentManager(), "NoteDialogFragment");
    }

    // opens the dialog to add new mentor
    public void openMentorDialog() {
        DialogFragment dialog = new MentorDialogFragment();
        dialog.show(getFragmentManager(), "MentorDialogFragment");
    }

    // opens edit course activity
    public void openEditCourseActivity() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        intent.putExtra("COURSE_ID", course_id);
        startActivity(intent);
    }

    // deletes the current course
    public void handleDeleteCourse() {
        boolean success = dataSource.deleteCourse(course_id);
        if (success) {
            Toast.makeText(getApplicationContext(), "Course Deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Delete Course", Toast.LENGTH_LONG).show();
        }
    }

    // open assessment list activity
    public void openAssessmentList() {
        Intent intent = new Intent(this, AssessmentListActivity.class);
        intent.putExtra("COURSE_ID", course_id);
        intent.putExtra("COURSE_TITLE", course.getTitle());
        startActivity(intent);
    }

    // add Calendar Alert for start course
    public void createStartAlert() {
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.setTimeInMillis(course.getStart());
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
//                .putExtra(CalendarContract.Events.TITLE, course.getTitle() + " Starts")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "First day of class")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "WGU Online")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//        startActivity(intent);

        // new code to use Alarm Service instead of calendar
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("TITLE", "Course Start Reminder");
        intent.putExtra("TEXT", course.getTitle() + " starts today");
        intent.setAction(course.getTitle() + " starts today");
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent sender = PendingIntent.getBroadcast(this, uniqueInt ,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,course.getStart() + (1000 * 60 * 60 * 7), sender);
    }

    // add Calendar Alert for end of course
    public void createEndAlert() {
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.setTimeInMillis(course.getEnd());
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
//                .putExtra(CalendarContract.Events.TITLE, course.getTitle() + " End Goal")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "Goal date to finish class")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "WGU Online")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//        startActivity(intent);

        // new code to use Alarm Service instead of calendar
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("TITLE", "Course End Reminder");
        intent.putExtra("TEXT", course.getTitle() + " ends today");
        intent.setAction(course.getTitle() + " starts today");
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent sender = PendingIntent.getBroadcast(this, uniqueInt ,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,course.getEnd() + (1000 * 60 * 60 * 7), sender);
    }

    // handles the save click in the note dialog fragment
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText note_input = (EditText) dialogView.findViewById(R.id.note_input);
        String note_text = note_input.getText().toString();
        if (note_text != "") {
            note = dataSource.createNote(note_text, course_id);
            populateNote();
        }
    }

    @Override
    public void onMentorDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText mentor_name_input = (EditText) dialogView.findViewById(R.id.mentor_name);
        EditText mentor_phone_input = (EditText) dialogView.findViewById(R.id.mentor_phone);
        EditText mentor_email_input = (EditText) dialogView.findViewById(R.id.mentor_email);
        String mentor_name = mentor_name_input.getText().toString();
        String mentor_phone = mentor_phone_input.getText().toString();
        String mentor_email = mentor_email_input.getText().toString();
        if (mentor_name != "" && mentor_phone != "" && mentor_email != "") {
            mentor = dataSource.createMentor(mentor_name, mentor_phone, mentor_email, course_id);
            System.out.println("mentor " + mentor);
            mentors.add(mentor);
            populateMentors();
        }
    }

    // populate details
    public void populateDetails() {
        CourseDetailFragment fragment = (CourseDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.course_detail);
        fragment.populateViews(course);
    }

    // populates the course mentors
    public void populateMentors() {
        CourseMentorFragment fragment = (CourseMentorFragment)
                getFragmentManager().findFragmentById(R.id.mentors);
        fragment.populateViews(mentors);
    }

    // populates the course note in the course note fragment
    public void populateNote() {
        CourseNoteFragment fragment = (CourseNoteFragment)
                getSupportFragmentManager().findFragmentById(R.id.notes);
        fragment.populateViews(note, course.getTitle());
    }

    // persist course id to populate activity when back is pressed from child activities
    public void writeToSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("COURSE_ID", course_id);
        editor.commit();
    }

    // reads from shared prefs if course id is not passed in with intent
    public long readFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getLong("COURSE_ID", 0);
    }
}
