package com.example.benjamin.assessment.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.models.Course;

import java.util.Calendar;

public class CourseDetailFragment extends Fragment {

    long course_id;
    String course_title;
    long course_start;
    long course_end;
    String course_status;

    TextView detail_course_title;
    TextView detail_course_start;
    TextView detail_course_end;
    TextView detail_course_status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // cast text views by id
        detail_course_title = (TextView) view.findViewById(R.id.detail_course_title);
        detail_course_start = (TextView) view.findViewById(R.id.detail_course_start);
        detail_course_end = (TextView) view.findViewById(R.id.detail_course_end);
        detail_course_status = (TextView) view.findViewById(R.id.detail_course_status);
    }

    public void populateViews(Course course) {
        course_id = course.getId();
        course_title = course.getTitle();
        course_start = course.getStart();
        course_end = course.getEnd();
        course_status = course.getStatus();

        detail_course_title.setText(course_title);
        detail_course_start.append("  " + getDateString(course_start));
        detail_course_end.append("  " + getDateString(course_end));
        detail_course_status.append("  " + course_status);
    }

    // helper to transform long ms from db into string mm/dd/yyyy
    public String getDateString(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        return "  " + month + "/" + day + "/" + year;
    }
}
