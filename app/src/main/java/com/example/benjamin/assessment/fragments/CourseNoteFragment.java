package com.example.benjamin.assessment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.benjamin.assessment.R;
import com.example.benjamin.assessment.models.Note;

public class CourseNoteFragment extends Fragment {

    long note_id;
    String note_text;
    long course_id;
    String course_title;

    Button detail_note_share;
    TextView detail_course_note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_note, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        detail_course_note = (TextView) view.findViewById(R.id.detail_course_note);
        detail_note_share = (Button) view.findViewById(R.id.detail_note_share);

        detail_note_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, course_title);
                intent.putExtra(Intent.EXTRA_TEXT, note_text);
                startActivity(Intent.createChooser(intent, "Share Notes"));
            }
        });
    }

    public void populateViews(Note note, String course_title) {
        note_id = note.getId();
        note_text = note.getText();
        course_id = note.getCourseId();
        this.course_title = course_title;
        detail_course_note.setText(note_text);
        detail_note_share.setVisibility(View.VISIBLE);
    }
}
