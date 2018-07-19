package com.example.benjamin.assessment.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.adapters.TermListAdapter;
import com.example.benjamin.assessment.models.Term;
import com.example.benjamin.assessment.utils.DataSource;
import java.util.ArrayList;

public class TermListActivity extends AppCompatActivity {

    // pieces of RecyclerView - a customizable list
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        // load xml for RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.term_recycler_view);

        // set actionbar title and sub title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.menu_terms);
        actionbar.setSubtitle(R.string.sub_title_terms);

        // open database and get all terms
        dataSource = new DataSource(this);
        dataSource.open();
        ArrayList<Term> terms = dataSource.getAllTerms();

        // wire up layout manager and adapter to recyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TermListAdapter(terms);
        recyclerView.setAdapter(adapter);
    }
}
