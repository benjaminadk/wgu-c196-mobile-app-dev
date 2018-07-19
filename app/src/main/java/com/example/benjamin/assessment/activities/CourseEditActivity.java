package com.example.benjamin.assessment.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.fragments.DatePickerFragment;
import com.example.benjamin.assessment.models.Course;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.Calendar;

public class CourseEditActivity extends AppCompatActivity
        implements  DatePickerFragment.DatePickerFragmentListener,
        AdapterView.OnItemSelectedListener{

    private DataSource dataSource;

    EditText editText;
    Button button1, button2, button3;
    long start, end, course_id;
    Spinner spinner;
    String title, status;
    FragmentManager fm = getSupportFragmentManager();
    int DATE_DIALOG = 0;

    Course course;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_create);

        // get term_id as extra on incoming intent
        course_id = getIntent().getLongExtra("COURSE_ID",0);

        // set actionbar and actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_edit_course);

        // open database collection
        dataSource = new DataSource(this);
        dataSource.open();

        course = dataSource.getCourseById(course_id);

        title = course.getTitle();
        start = course.getStart();
        end = course.getEnd();
        status = course.getStatus();

        editText = (EditText) findViewById(R.id.editText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button3.setText(R.string.course_edit_save);

        // implementation of spinner to help choose course status
        spinner = (Spinner) findViewById(R.id.create_course_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.create_course_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int pos = adapter.getPosition(status);
        spinner.setSelection(pos, true);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 1;
                openDialog();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 2;
                openDialog();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editText.getText().toString();
                boolean success = dataSource.updateCourse(course_id, title, start, end, status);
                if (success) {
                    Toast.makeText(getApplicationContext(), "Course Updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable To Update Course", Toast.LENGTH_LONG).show();
                }
            }
        });

        populateFields();
    }

    public void openDialog(){
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(fm, "datePicker");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(int year, int month, int day) {
        if(DATE_DIALOG ==1){
            button1.setText("Course Start: " + Integer.toString(month+1) + "/" +
                    Integer.toString(day)+ "/" +
                    Integer.toString(year));
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            start = c.getTime().getTime();
        }
        else if(DATE_DIALOG ==2){
            button2.setText("Course End Goal: " + Integer.toString(month+1) + "/" +
                    Integer.toString(day)+ "/" +
                    Integer.toString(year));
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            end = c.getTime().getTime();
        }
    }

    // set status value when spinner item is selected
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        status = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setDatePickerPopulate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        onDateSet(year, month, day);
    }

    public void populateFields() {
        editText.setText(title);

        DATE_DIALOG = 1;
        setDatePickerPopulate(start);

        DATE_DIALOG = 2;
        setDatePickerPopulate(end);

        DATE_DIALOG = 0;
    }
}
