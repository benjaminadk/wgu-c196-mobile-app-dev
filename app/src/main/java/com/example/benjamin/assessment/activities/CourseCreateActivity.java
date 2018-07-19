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

public class CourseCreateActivity extends AppCompatActivity
        implements  DatePickerFragment.DatePickerFragmentListener,
                    AdapterView.OnItemSelectedListener{

    private DataSource dataSource;

    EditText editText;
    Button button1, button2, button3;
    long start, end, term_id;
    Spinner spinner;
    String status;
    FragmentManager fm = getSupportFragmentManager();
    int DATE_DIALOG = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_create);

        // get term_id as extra on incoming intent
        term_id = getIntent().getLongExtra("TERM_ID",0);

        // set actionbar and actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_create_course);

        // open database collection
        dataSource = new DataSource(this);
        dataSource.open();

        editText = (EditText) findViewById(R.id.editText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        // implementation of spinner to help choose course status
        spinner = (Spinner) findViewById(R.id.create_course_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.create_course_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
                dataSource.createCourse(editText.getText().toString(), start, end,
                        status, term_id);
                resetValues();
                Toast.makeText(getApplicationContext(), "Course Created", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openDialog(){
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(fm, "datePicker");
    }

    public void resetValues() {
        editText.setText("");
        button1.setText(R.string.set_course_start);
        button2.setText(R.string.set_course_end);
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
}
