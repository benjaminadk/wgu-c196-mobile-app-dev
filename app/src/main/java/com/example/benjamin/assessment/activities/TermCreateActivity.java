package com.example.benjamin.assessment.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.fragments.DatePickerFragment;
import com.example.benjamin.assessment.models.Term;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.Calendar;

public class TermCreateActivity extends AppCompatActivity
        implements  DatePickerFragment.DatePickerFragmentListener{

    private DataSource dataSource;

    EditText editText;
    Button button1, button2, button3;
    long start, end;
    FragmentManager fm = getSupportFragmentManager();
    int DATE_DIALOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_create);

        // enable actionbar and set actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_create_term);

        // open database
        dataSource = new DataSource(this);
        dataSource.open();

        editText = (EditText) findViewById(R.id.editText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

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
                dataSource.createTerm(editText.getText().toString(), start, end);
                resetValues();
                Toast.makeText(getApplicationContext(), "New Term Created", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openDialog(){
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(fm, "datePicker");
    }

    public void resetValues() {
        editText.setText("");
        button1.setText(R.string.set_term_start);
        button2.setText(R.string.set_term_end);
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
}
