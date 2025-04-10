package com.example.myapplicationasa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Medical_notes> notesList;

    public NoteAdapter(Context context, List<Medical_notes> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each list item
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Medical_notes currentNote = notesList.get(position);

        // Set the data to the UI components (e.g., TextViews)
        holder.patientIdText.setText("Patient ID: " + currentNote.getPatientId());
        holder.physicianText.setText("Physician: " + currentNote.getPhysician());
        holder.testResultText.setText("Test Result: " + currentNote.getTestResult());

        // Handle the click on an item
        holder.itemView.setOnClickListener(v -> {
            // Intent to start the detailed activity
            Intent intent = new Intent(context, NoteDetailActivity.class);
            // Pass the medical note object to the next activity
            intent.putExtra("noteDetails", currentNote);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView patientIdText, physicianText, testResultText;

        public NoteViewHolder(View itemView) {
            super(itemView);
            patientIdText = itemView.findViewById(R.id.text_patient_id);
            physicianText = itemView.findViewById(R.id.text_physician);
            testResultText = itemView.findViewById(R.id.text_test_result);
        }
    }
}
