package com.example.myapplicationasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MedicalNotes extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<File> notesFiles;
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;
    private static final int PICK_PDF_REQUEST = 1;

    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_notes);
        notesFiles = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter(this, notesFiles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            buttonDialog();
        });

    }
    private  void buttonDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Action");

        builder.setPositiveButton("Save new  record", new DialogInterface.OnClickListener() {
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