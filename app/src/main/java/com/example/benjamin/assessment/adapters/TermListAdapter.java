package com.example.benjamin.assessment.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.activities.TermDetailActivity;
import com.example.benjamin.assessment.models.Term;

import java.util.ArrayList;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ViewHolder> {

    private ArrayList<Term> terms;
    private int selectedPos = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.term_title);
        }
    }

    public TermListAdapter(ArrayList<Term> terms) {
        this.terms = terms;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TermListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_term, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String term_title = terms.get(position).getTitle();
        holder.textView.setText(term_title);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TermDetailActivity.class);
                intent.putExtra("TERM_ID", terms.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return terms.size();
    }
}

