package com.example.myapplicationasa;

import static android.widget.Toast.LENGTH_SHORT;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Addnotes extends AppCompatActivity {
    Button button;
    EditText PatientID, physcian,testdone,testdate,testresult,Recommendation;
    Spinner spinner1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addnotes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button = findViewById(R.id.btnSave);
        PatientID = findViewById(R.id.editpatientid);
        physcian = findViewById(R.id.editphysician);
        testdone= findViewById(R.id.testdone);
        testdate = findViewById(R.id.editTextDate);
        testresult = findViewById(R.id.editTresult);
        Recommendation = findViewById(R.id.editTextTextMultiLine);
        spinner1 = findViewById(R.id.spinner1);
        ArrayList<String> admissionTypes = new ArrayList<>();
        admissionTypes.add("Medical Checkup");
        admissionTypes.add("Emergency Admission");
        admissionTypes.add("Elective Admission");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, admissionTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String email = getEmail(currentUser);
                String patientIdStr = PatientID.getText().toString();
                String physician = physcian.getText().toString();
                String testDone = testdone.getText().toString();
                String testResult = testresult.getText().toString();
                String recommendation = Recommendation.getText().toString();
                String admissionType = spinner1.getSelectedItem().toString();
                String testDateStr = testdate.getText().toString();
                Integer testDate = null;  // Default value is null

                // Convert test date if provided
                if (!testDateStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                    try {
                        Date date = sdf.parse(testDateStr);
                        if (date != null) {
                            testDate = (int) (date.getTime() / 1000);  // Timestamp in seconds
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // Allow patient ID to be blank; parse only if not empty
                Integer patientID = null;
                if (!patientIdStr.isEmpty()) {
                    try {
                        patientID = Integer.parseInt(patientIdStr);
                        if (patientID < 0) {
                            Toast.makeText(Addnotes.this, "Patient ID cannot be negative", Toast.LENGTH_SHORT).show();
                            patientID = null;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(Addnotes.this, "Invalid Patient ID", Toast.LENGTH_SHORT).show();
                        patientID = null;
                    }
                }

                // Save the data even if patientID is null
                boolean isSaved = saveData(patientID, physician, admissionType, testDone, testDate, testResult, recommendation, email);
                if (isSaved) {
                    Toast.makeText(Addnotes.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Addnotes.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean saveData(Integer patientID, String physician, String admissionType, String testDone,
                             Integer testDate, String testResult, String recommendation, String email) {

        if (physician != null && admissionType != null) {
            // Create an instance of your database helper
            diagnosis db = new diagnosis(Addnotes.this);

            // Call the method to add the record to the database
            return db.addmedicnotes(patientID, physician, admissionType, testDone, testDate, testResult, recommendation, email);
        } else {
            Toast.makeText(Addnotes.this, "Unable to save data: Missing required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private String getEmail(FirebaseUser currentUser) {
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            Toast.makeText(this, "User not logged in", LENGTH_SHORT).show();
            return null;
        }
    }

}