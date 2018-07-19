package com.example.benjamin.assessment.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.models.Assessment;
import com.example.benjamin.assessment.utils.DataSource;
import com.example.benjamin.assessment.utils.MyReceiver;

import java.util.Calendar;

public class AssessmentDetailActivity extends AppCompatActivity {

    Assessment assessment;

    long assessment_id, assessment_due;
    String assessment_title, assessment_type;

    TextView detail_assessment_title, detail_assessment_type, detail_assessment_due;

    private DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        // get assessment id from intent or shared preferences
        assessment_id = getIntent().getLongExtra("ASSESSMENT_ID", readFromSavedPreferences());

        // database
        dataSource = new DataSource(this);
        dataSource.open();
        assessment = dataSource.getAssessmentById(assessment_id);
        assessment_title = assessment.getTitle();
        assessment_type = assessment.getType();
        assessment_due = assessment.getDue();

        // cast text views
        detail_assessment_title = (TextView) findViewById(R.id.detail_assessment_title);
        detail_assessment_type = (TextView) findViewById(R.id.detail_assessment_type);
        detail_assessment_due = (TextView) findViewById(R.id.detail_assessment_due);

        detail_assessment_title.setText(assessment_title);
        detail_assessment_type.append(" " + assessment_type);
        detail_assessment_due.append(getDateString(assessment_due));

        // assign title and enable action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");

        writeToSavedPreferences();
    }

    // create options menu specific to assessment detail view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment, menu);
        return true;
    }

    // handles selections in the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit_assessment:
                openEditAssessmentActivity();
                return true;
            case R.id.action_delete_assessment:
                handleDeleteAssessment();
                return true;
            case R.id.action_alert_assessment:
                createDueAlert();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openEditAssessmentActivity() {
        Intent intent = new Intent(this, AssessmentEditActivity.class);
        intent.putExtra("ASSESSMENT_ID", assessment_id);
        startActivity(intent);
    }

    // deletes the current assessment
    public void handleDeleteAssessment() {
        boolean success = dataSource.deleteAssessment(assessment_id);
        if (success) {
            Toast.makeText(getApplicationContext(), "Assessment Deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Delete Assessment", Toast.LENGTH_LONG).show();
        }
    }

    // add Calendar Alert for assessment due
    public void createDueAlert() {
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.setTimeInMillis(assessment.getDue());
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
//                .putExtra(CalendarContract.Events.TITLE, assessment.getTitle() + " Due")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "Assessment Due Today")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "WGU Online")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//        startActivity(intent);

        // new code to use Alarm Service instead of calendar
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("TITLE", "Assessment Due Reminder");
        intent.putExtra("TEXT", assessment.getTitle() + " due today");
        intent.setAction(assessment.getTitle() + " is due today");
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent sender = PendingIntent.getBroadcast(this, uniqueInt ,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,assessment.getDue() + (1000 * 60 * 60 * 7), sender);
    }

    // helper to transform long ms from db into string mm/dd/yyyy
    public String getDateString(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        return "  " + month + "/" + day + "/" + year;
    }

    public void writeToSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("ASSESSMENT_ID", assessment_id);
        editor.commit();
    }

    public long readFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getLong("ASSESSMENT_ID", 0);
    }
}
