package com.example.myapplicationasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MedicalNotes extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<Medical_notes> notesList;  // Change to Medical_notes type
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_notes);

        notesList = new ArrayList<>();  // Use Medical_notes list

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // NoteAdapter should now take Medical_notes objects
        noteAdapter = new NoteAdapter(this, notesList);
        recyclerView.setAdapter(noteAdapter);

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            buttonDialog();
        });

        String email = null;
        if (user != null) {
            email = user.getEmail();  // Now email is initialized before use.
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }

        if (email != null) {
            diagnosis db = new diagnosis(this);
            List<Medical_notes> notes = db.getmedicalnotes(email); // Get medical notes
            if (notes != null && !notes.isEmpty()) {
                notesList.addAll(notes); // Add notes to the list
                noteAdapter.notifyDataSetChanged(); // Notify adapter to update the RecyclerView
            } else {
                Toast.makeText(this, "No medical notes found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buttonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Action");

        builder.setPositiveButton("Save new record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MedicalNotes.this, Addnotes.class);
                startActivity(intent);
                finish();
                Toast.makeText(MedicalNotes.this, "Save clicked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MedicalNotes.this, "Delete clicked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
