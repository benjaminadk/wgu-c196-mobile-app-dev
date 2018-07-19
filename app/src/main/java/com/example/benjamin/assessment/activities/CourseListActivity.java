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
import com.example.benjamin.assessment.adapters.CourseListAdapter;
import com.example.benjamin.assessment.models.Course;
import com.example.benjamin.assessment.utils.DataSource;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {

    long term_id;
    String term_title;

    // pieces of RecyclerView - a customizable list
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // intent extras reflect selected term
        term_id = getIntent().getLongExtra("TERM_ID", readTermIdFromSavedPreferences());
        if (getIntent().getStringExtra("TERM_TITLE") == null) {
            term_title = readTermTitleFromSavedPreferences();
        } else {
            term_title = getIntent().getStringExtra("TERM_TITLE");
        }

        // load xml for RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.course_recycler_view);

        // set actionbar title and sub title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_courses);
        actionbar.setSubtitle(R.string.sub_title_courses);

        // open database and get all terms
        dataSource = new DataSource(this);
        dataSource.open();
        ArrayList<Course> courses = dataSource.getCoursesForTerm(term_id);

        // wire up layout manager and adapter to recyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CourseListAdapter(courses, term_title);
        recyclerView.setAdapter(adapter);

        writeToSavedPreferences();
    }

    public void writeToSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("TERM_ID", term_id);
        editor.putString("TERM_TITLE", term_title);
        editor.commit();
    }

    public long readTermIdFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            return sharedPref.getLong("TERM_ID", 0);
    }

    public String readTermTitleFromSavedPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            return sharedPref.getString("TERM_TITLE", "");
        }
}
