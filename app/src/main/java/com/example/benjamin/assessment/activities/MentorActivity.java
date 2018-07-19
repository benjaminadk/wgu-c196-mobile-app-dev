package com.example.benjamin.assessment.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.models.Mentor;
import com.example.benjamin.assessment.utils.DataSource;

public class MentorActivity extends AppCompatActivity {

    long mentor_id;
    String name, phone, email;
    Mentor mentor;

    EditText mentor_name, mentor_phone, mentor_email;
    Button button1;

    private DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        // set actionbar and actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_edit_mentor);

        // get mentor id
        mentor_id = getIntent().getLongExtra("MENTOR_ID", readFromSavedPreferences());

        // database
        dataSource = new DataSource(this);
        dataSource.open();
        mentor = dataSource.getMentorById(mentor_id);

        name = mentor.getName();
        phone = mentor.getPhone();
        email = mentor.getEmail();

        mentor_name = (EditText) findViewById(R.id.mentor_name);
        mentor_phone = (EditText) findViewById(R.id.mentor_phone);
        mentor_email = (EditText) findViewById(R.id.mentor_email);
        button1 = (Button) findViewById(R.id.mentor_button);

        mentor_name.setText(name);
        mentor_phone.setText(phone);
        mentor_email.setText(email);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_input = mentor_name.getText().toString();
                String phone_input = mentor_phone.getText().toString();
                String email_input = mentor_email.getText().toString();
                if(name_input != "" && phone_input != "" && email_input != "") {
                    boolean success = dataSource.updateMentor(mentor_id, name_input, phone_input, email_input);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Mentor Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error Updating Mentor", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        writeToSavedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mentor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete_mentor:
                handleDeleteMentor();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void handleDeleteMentor() {
        boolean success = dataSource.deleteMentor(mentor_id);
        if (success) {
            resetValues();
            Toast.makeText(getApplicationContext(), "Mentor Deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Delete Mentor", Toast.LENGTH_LONG).show();
        }
    }

    public void resetValues() {
        mentor_name.setText("");
        mentor_phone.setText("");
        mentor_email.setText("");
    }

    // persist course id to populate activity when back is pressed from child activities
    public void writeToSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("MENTOR_ID", mentor_id);
        editor.commit();
    }

    // reads from shared prefs if course id is not passed in with intent
    public long readFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getLong("MENTOR_ID", 0);
    }
}
