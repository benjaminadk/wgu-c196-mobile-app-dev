package com.example.benjamin.assessment.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.activities.CourseDetailActivity;
import com.example.benjamin.assessment.models.Course;
import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private ArrayList<Course> courses;
    private String term_title;
    private int selectedPos = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewSubTitle;
        public ViewHolder(View v) {
            super(v);
            textViewTitle = (TextView) v.findViewById(R.id.course_title);
            textViewSubTitle = (TextView) v.findViewById(R.id.course_sub_title);
        }
    }

    public CourseListAdapter(ArrayList<Course> courses, String term_title) {
        this.courses = courses;
        this.term_title = term_title;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_course, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String course_title = courses.get(position).getTitle();
        holder.textViewTitle.setText(course_title);
        holder.textViewSubTitle.setText(term_title);
        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseDetailActivity.class);
                intent.putExtra("COURSE_ID", courses.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return courses.size();
    }
}
