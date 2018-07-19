package com.example.benjamin.assessment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.activities.AssessmentDetailActivity;
import com.example.benjamin.assessment.models.Assessment;

import java.util.ArrayList;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.ViewHolder> {

    private ArrayList<Assessment> assessments;
    private String assessment_title;
    private int selectedPos = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewSubTitle;
        public ViewHolder(View v) {
            super(v);
            textViewTitle = (TextView) v.findViewById(R.id.assessment_title);
            textViewSubTitle = (TextView) v.findViewById(R.id.assessment_sub_title);
        }
    }

    public AssessmentListAdapter(ArrayList<Assessment> assessments, String assessment_title) {
        this.assessments = assessments;
        this.assessment_title = assessment_title;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_assessment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String course_title = assessments.get(position).getTitle();
        holder.textViewTitle.setText(course_title);
        holder.textViewSubTitle.setText(assessment_title);
        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AssessmentDetailActivity.class);
                intent.putExtra("ASSESSMENT_ID", assessments.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return assessments.size();
    }
}
