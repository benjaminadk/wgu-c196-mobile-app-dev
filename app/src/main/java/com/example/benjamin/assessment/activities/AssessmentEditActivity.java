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
import com.example.benjamin.assessment.models.Assessment;
import com.example.benjamin.assessment.utils.DataSource;
import java.util.Calendar;

public class AssessmentEditActivity extends AppCompatActivity
        implements  DatePickerFragment.DatePickerFragmentListener {

    Assessment assessment;

    long assessment_id, due;
    String title, type;

    private DataSource dataSource;

    EditText editText;
    boolean etHasValue;

    RadioGroup radioGroup;
    Button button1, button2;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_create);

        assessment_id = getIntent().getLongExtra("ASSESSMENT_ID", 0);

        // enable actionbar and set actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.action_edit_assessment);

        // open database
        dataSource = new DataSource(this);
        dataSource.open();

        assessment = dataSource.getAssessmentById(assessment_id);

        title = assessment.getTitle();
        type = assessment.getType();
        due = assessment.getDue();

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
        editText.setText(title);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        setDatePickerPopulate(due);

        button2.setText(R.string.assessment_edit_save);

        int check;
        if (type.equals("performance")) {
            check = R.id.performance;
        } else {
            check = R.id.objective;
        }
        radioGroup.check(check);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etHasValue || due == 0) {
                    Toast.makeText(getApplicationContext(), "Complete All Fields", Toast.LENGTH_LONG).show();
                } else {
                    title = editText.getText().toString();
                    boolean success = dataSource.updateAssessment(assessment_id, title, type, due);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Assessment Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable To Update Assessment", Toast.LENGTH_LONG).show();
                    }
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

    public void setDatePickerPopulate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        onDateSet(year, month, day);
    }
}
