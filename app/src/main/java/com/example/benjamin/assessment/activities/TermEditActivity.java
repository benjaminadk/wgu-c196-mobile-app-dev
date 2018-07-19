package com.example.benjamin.assessment.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.fragments.DatePickerFragment;
import com.example.benjamin.assessment.models.Term;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.Calendar;

public class TermEditActivity extends AppCompatActivity
        implements  DatePickerFragment.DatePickerFragmentListener {

    Term term;

    long term_id, start, end;
    String title;

    private DataSource dataSource;

    EditText editText;
    Button button1, button2, button3;

    FragmentManager fm = getSupportFragmentManager();
    int DATE_DIALOG = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_create);

        term_id = getIntent().getLongExtra("TERM_ID", 0);

        // enable actionbar and set actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.action_edit_term);

        // open database
        dataSource = new DataSource(this);
        dataSource.open();

        term = dataSource.getTermById(term_id);

        title = term.getTitle();
        start = term.getStart();
        end = term.getEnd();

        editText = (EditText) findViewById(R.id.editText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button3.setText(R.string.term_edit_save);

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
                boolean success = dataSource.updateTerm(term_id, title, start, end);
                if (success) {
                    Toast.makeText(getApplicationContext(), "Term Updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable To Update Term", Toast.LENGTH_LONG).show();
                }
            }
        });

        populateFields();
    }

    public void openDialog(){
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(fm, "datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        if(DATE_DIALOG ==1){
            button1.setText("Term Start: " + Integer.toString(month+1) + "/" +
                    Integer.toString(day)+ "/" +
                    Integer.toString(year));
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            start = c.getTime().getTime();
        }
        else if(DATE_DIALOG ==2){
            button2.setText("Term End: " + Integer.toString(month+1) + "/" +
                    Integer.toString(day)+ "/" +
                    Integer.toString(year));
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            end = c.getTime().getTime();
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

    public void populateFields() {
        editText.setText(title);

        DATE_DIALOG = 1;
        setDatePickerPopulate(start);

        DATE_DIALOG = 2;
        setDatePickerPopulate(end);

        DATE_DIALOG = 0;
    }
}
