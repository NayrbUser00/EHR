package com.example.myapplicationasa;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class documents extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<File> pdfFiles;
    private pdfadapter adapter;
    private RecyclerView recyclerView;
    private static final int PICK_PDF_REQUEST = 1;


    ImageButton upload,delete,open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_documents);
        pdfFiles = new ArrayList<>();


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new pdfadapter(this, pdfFiles);
        recyclerView.setAdapter(adapter);

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(v -> openFileChooser());
        open = findViewById(R.id.open);
        open.setOnClickListener(v -> {
            File selectedFile = adapter.getSelectedPdfFile();
            if (selectedFile != null) {
                openPdf(selectedFile);
            } else {
                Toast.makeText(this, "No file selected to open", Toast.LENGTH_SHORT).show();
            }
        });


        delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            // Get the selected file from the adapter
            File selectedFile = adapter.getSelectedPdfFile();

            // Use the context from the view parameter
            Context context = v.getContext();

            if (selectedFile != null) {
                // Show a confirmation dialog before deleting
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this file?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            deletePDF(selectedFile); // Call deletePDF method to delete the file
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                // If no file is selected, show a toast message
                Toast.makeText(context, "No file selected for deletion", Toast.LENGTH_SHORT).show();
            }
        });






        String email = null;
        if (user != null) {
            email = user.getEmail();
            loadPDFFiles(email);

        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }



    }



    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri pdfUri = data.getData();
            if (user != null) {
                savePDFToInternalStorage(pdfUri, user.getEmail());
            }
        }
    }

    private void savePDFToInternalStorage(Uri uri, String userEmail) {
        try {
            String sanitizedEmail = sanitizeEmail(userEmail);
            File userDirectory = new File(getFilesDir(), sanitizedEmail);

            // Ensure the user's directory exists
            if (!userDirectory.exists()) {
                userDirectory.mkdirs();
            }

            // Create a default file name for the PDF
            String defaultFileName = "uploaded_pdf_" + System.currentTimeMillis() + ".pdf";

            // Ask the user whether they want to rename the file before saving
            new AlertDialog.Builder(this)
                    .setTitle("Rename PDF")
                    .setMessage("Do you want to rename the file before saving?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User chose to rename, prompt for a new name
                        promptForNewFileName(userDirectory, defaultFileName, uri, userEmail);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // User chose not to rename, proceed with the default name
                        proceedWithSavingFile(userDirectory, defaultFileName, uri);
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing the PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void promptForNewFileName(File userDirectory, String defaultFileName, Uri uri, String userEmail) {
        EditText input = new EditText(this);
        input.setHint("Enter new file name");

        new AlertDialog.Builder(this)
                .setTitle("Enter New File Name")
                .setView(input)
                .setPositiveButton("Rename", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        // If user provided a name, proceed with saving the file
                        saveFileToPath(userDirectory, newName + ".pdf", uri, userEmail);
                    } else {
                        Toast.makeText(this, "Invalid file name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
        
    }

    private void saveFileToPath(File userDirectory, String fileName, Uri uri, String userEmail) {
        try {
            File internalFile = new File(userDirectory, fileName);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(internalFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();

            // Refresh the list after saving the new PDF
            if (userEmail != null) {
                loadPDFFiles(userEmail); // Reload files specific to the user
            }

            Toast.makeText(this, "PDF saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void proceedWithSavingFile(File userDirectory, String fileName, Uri uri) {
        // Proceed with saving the file with the default name
        saveFileToPath(userDirectory, fileName, uri, null);
    }
    private void loadPDFFiles(String userEmail) {
        String sanitizedEmail = sanitizeEmail(userEmail);
        File userDirectory = new File(getFilesDir(), sanitizedEmail);
        File[] files = userDirectory.listFiles((dir, name) -> name.endsWith(".pdf"));

        if (files != null && files.length > 0) {
            pdfFiles.clear();
            pdfFiles.addAll(Arrays.asList(files));
            adapter.updateList(pdfFiles);
        }
    }

    public static String sanitizeEmail(String email) {
        return email.replaceAll("[^a-zA-Z0-9]", "_");
    }



    private void deletePDF(File pdfFile) {
        if (pdfFile.exists()) {
            boolean deleted = pdfFile.delete();
            if (deleted) {
                Toast.makeText(this, "PDF deleted successfully", Toast.LENGTH_SHORT).show();
                loadPDFFiles(user.getEmail()); // Reload the PDF list after deletion
            } else {
                Toast.makeText(this, "Failed to delete PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void openPdf(File pdfFile) {
        try {
            Uri uri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName() + ".provider",
                    pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "No application available to open PDF", Toast.LENGTH_SHORT).show();
        }
    }

}



