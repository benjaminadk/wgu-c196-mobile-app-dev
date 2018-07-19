package com.example.benjamin.assessment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.models.Term;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.Calendar;

public class TermDetailActivity extends AppCompatActivity {

    long term_id, term_start, term_end;
    String term_title;

    Menu menu;

    TextView detail_term_title, detail_days_left, detail_term_start, detail_term_end;

    private DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // get term id from intent or shared preferences
        term_id = getIntent().getLongExtra("TERM_ID", readFromSavedPreferences());

        // database
        dataSource = new DataSource(this);
        dataSource.open();
        Term term = dataSource.getTermById(term_id);
        term_title = term.getTitle();
        term_start = term.getStart();
        term_end = term.getEnd();

        // cast text views by id
        detail_term_title = (TextView) findViewById(R.id.detail_term_title);
        detail_days_left = (TextView) findViewById(R.id.detail_days_left);
        detail_term_start = (TextView) findViewById(R.id.detail_term_start);
        detail_term_end = (TextView) findViewById(R.id.detail_term_end);

        // set values of text views
        detail_term_title.setText(term_title);
        detail_days_left.setText(getDaysUntilTermEnds(term_start, term_end));
        detail_term_start.append(getDateString(term_start));
        detail_term_end.append(getDateString(term_end));

        // assign title and enable action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");

        writeToSavedPreferences();
    }

    // create options menu specific to term detail view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_term, menu);
        return true;
    }

    // handles selections from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_course:
                openCourseCreateActivity();
                return true;
            case R.id.action_edit_term:
                openEditTermActivity();
                return true;
            case R.id.action_view_courses:
                openCourseListActivity();
                return true;
            case R.id.action_delete_term:
                handleDeleteTerm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // opens Create Course Activity passing term_id to be used as foreign key
    public void openCourseCreateActivity() {
        Intent intent = new Intent(this, CourseCreateActivity.class);
        intent.putExtra("TERM_ID", term_id);
        startActivity(intent);
    }

    // opens Edit Term Dialog
    public void openEditTermActivity() {
        Intent intent = new Intent(this, TermEditActivity.class);
        intent.putExtra("TERM_ID", term_id);
        startActivity(intent);
    }

    // opens Course List Activity
    public void openCourseListActivity() {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra("TERM_ID", term_id);
        intent.putExtra("TERM_TITLE", term_title);
        startActivity(intent);
    }

    // calls deleteTerm and displays toast to show user result
    public void handleDeleteTerm() {
        boolean isDeleted = dataSource.deleteTerm(term_id);
        if (isDeleted) {
            Toast.makeText(getApplicationContext(), "Term Deleted", Toast.LENGTH_LONG).show();
            menu.setGroupVisible(0,false);
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Delete Term Containing Courses", Toast.LENGTH_LONG).show();
        }
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

    // helper to find days until term start or days until term end depending if term is active
    public String getDaysUntilTermEnds(long start, long end) {
        long now = Calendar.getInstance().getTime().getTime();
        long millisecondsInADay = 24 * 60 * 60 * 1000;
        int days = 0;
        if (end < now) {
            return "term is over";
        } else if (start > now) {
            while(start > now) {
                now += millisecondsInADay;
                days += 1;
            }
            return days + " days until term starts";
        } else {
            while(end > now) {
                now += millisecondsInADay;
                days += 1;
            }
            return days + " days until term ends";
        }
    }

    public void writeToSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("TERM_ID", term_id);
        editor.commit();
    }

    public long readFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getLong("TERM_ID", 0);
    }
}
