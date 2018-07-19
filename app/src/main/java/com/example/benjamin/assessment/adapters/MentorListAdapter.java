package com.example.benjamin.assessment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.activities.MentorActivity;
import com.example.benjamin.assessment.models.Mentor;
import java.util.ArrayList;

public class MentorListAdapter extends RecyclerView.Adapter<MentorListAdapter.ViewHolder> {

    private ArrayList<Mentor> mentors;
    private int selectedPos = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPhone;
        public TextView textViewEmail;
        public ViewHolder(View v) {
            super(v);
            textViewName = (TextView) v.findViewById(R.id.mentor_name);
            textViewPhone = (TextView) v.findViewById(R.id.mentor_phone);
            textViewEmail = (TextView) v.findViewById(R.id.mentor_email);
        }
    }

    public MentorListAdapter(ArrayList<Mentor> mentors) {
        this.mentors = mentors;
    }

    @NonNull
    @Override
    public MentorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_mentor, parent, false);
        MentorListAdapter.ViewHolder vh = new MentorListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String mentor_name = mentors.get(position).getName();
        final String mentor_phone = mentors.get(position).getPhone();
        final String mentor_email = mentors.get(position).getEmail();
        holder.textViewName.setText(mentor_name);
        holder.textViewPhone.setText(mentor_phone);
        holder.textViewEmail.setText(mentor_email);

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MentorActivity.class);
                intent.putExtra("MENTOR_ID", mentors.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }
}
