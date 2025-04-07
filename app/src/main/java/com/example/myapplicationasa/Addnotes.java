package com.example.myapplicationasa;

import static android.widget.Toast.LENGTH_SHORT;

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

import java.util.ArrayList;

public class Addnotes extends AppCompatActivity {
    Button button;
    EditText PatientID, physcian,testdone,testdate,testresult,Recommendation;
    Spinner spinner1;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            }
        });
    }
    private boolean saveData(int patientID, String physician, String admissionType, String testDone,
                             int testDate, String testResult, String recommendation, String email) {
        // Check if physician or any required field is null (you can customize validation as needed)
        if (physician != null && admissionType != null  ) {
            // Create an instance of your database helper
            diagnosis db = new diagnosis(Addnotes.this);


            // Call the method to add the record to the database
            return db.addmedicnotes(patientID, physician, admissionType, testDone, testDate, testResult, recommendation,email);
        } else {
            // Show a toast message if some required field is missing
            Toast.makeText(Addnotes.this, "Unable to save data: Missing fields", Toast.LENGTH_SHORT).show();
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