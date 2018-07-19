package com.example.benjamin.assessment.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.adapters.AssessmentListAdapter;
import com.example.benjamin.assessment.models.Assessment;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.ArrayList;

public class AssessmentListActivity extends AppCompatActivity {

    long course_id;
    String course_title;

    // pieces of RecyclerView - a customizable list
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        // intent extras reflect selected term
        course_id = getIntent().getLongExtra("COURSE_ID", readTermIdFromSavedPreferences());
        if (getIntent().getStringExtra("COURSE_TITLE") == null) {
            course_title = readTermTitleFromSavedPreferences();
        } else {
            course_title = getIntent().getStringExtra("COURSE_TITLE");
        }

        // load xml for RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.assessment_recycler_view);

        // set actionbar title and sub title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_assessments);
        actionbar.setSubtitle(R.string.sub_title_assessments);

        // open database and get all terms
        dataSource = new DataSource(this);
        dataSource.open();
        ArrayList<Assessment> assessments = dataSource.getAssessmentsForCourse(course_id);

        // wire up layout manager and adapter to recyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AssessmentListAdapter(assessments, course_title);
        recyclerView.setAdapter(adapter);

        writeToSavedPreferences();
    }

    public void writeToSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("COURSE_ID", course_id);
        editor.putString("COURSE_TITLE", course_title);
        editor.commit();
    }

    public long readTermIdFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getLong("COURSE_ID", 0);
    }

    public String readTermTitleFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("COURSE_TITLE", "");
    }
}
