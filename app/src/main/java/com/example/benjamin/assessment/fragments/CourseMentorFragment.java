package com.example.benjamin.assessment.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.adapters.MentorListAdapter;
import com.example.benjamin.assessment.models.Mentor;

import java.util.ArrayList;

public class CourseMentorFragment extends Fragment {

    String TAG = "CourseMentorFragment";
    ArrayList<Mentor> mentors = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_mentors, container, false);
        rootView.setTag(TAG);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.mentor_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }

    public void populateViews(ArrayList<Mentor> mentors) {
        this.mentors = mentors;
        adapter = new MentorListAdapter(mentors);
        recyclerView.setAdapter(adapter);
    }
}
