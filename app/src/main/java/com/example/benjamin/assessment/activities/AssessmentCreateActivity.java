package com.example.benjamin.assessment.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.fragments.DatePickerFragment;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.Calendar;

public class AssessmentCreateActivity extends AppCompatActivity
            implements  DatePickerFragment.DatePickerFragmentListener{

    private DataSource dataSource;

    EditText editText;
    boolean etHasValue;

    Button button1, button2;
    RadioGroup radioGroup;
    FragmentManager fm = getSupportFragmentManager();

    String title, type;
    long due, course_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_create);

        // get course_id as extra on incoming intent
        course_id = getIntent().getLongExtra("COURSE_ID",0);

        // set actionbar and actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_create_assessment);

        // open database collection
        dataSource = new DataSource(this);
        dataSource.open();

        editText = (EditText) findViewById(R.id.editText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        radioGroup = (RadioGroup) findViewById(R.id.type);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    etHasValue = false;
                } else {
                    etHasValue = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        radioGroup.check(R.id.performance);
        type = "performance";

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etHasValue || due == 0) {
                    System.out.println("DUE  " + due);
                    Toast.makeText(getApplicationContext(), "Complete All Fields", Toast.LENGTH_LONG).show();
                } else {
                    title = editText.getText().toString();
                    dataSource.createAssessment(title, type, due, course_id);
                    resetValues();
                    Toast.makeText(getApplicationContext(), "Assessment Created", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openDialog(){
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(fm, "datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int day) {
            button1.setText("Due Date: " + Integer.toString(month+1) + "/" +
                    Integer.toString(day)+ "/" +
                    Integer.toString(year));
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            due = c.getTime().getTime();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.performance:
                if (checked)
                   type = "performance";
                    break;
            case R.id.objective:
                if (checked)
                   type = "objective";
                    break;
        }
    }

    public void resetValues() {
        editText.setText("");
        button1.setText(R.string.assessment_due);
        radioGroup.check(R.id.performance);
        title = "";
        type = "";
        due = 0;
    }
}
