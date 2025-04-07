package com.example.myapplicationasa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    EditText editEmailAddress;
    Button sendemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);

        editEmailAddress = findViewById(R.id.forgotemail);
        sendemail = findViewById(R.id.sendemail);
        sendemail.setOnClickListener(v -> sendresetemail());

    }


    private void sendresetemail(){
        String email = editEmailAddress.getText().toString().trim();

        if (email.isEmpty()) {
            editEmailAddress.setError("Email is required");
            editEmailAddress.requestFocus();
            return;
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Forgotpassword.this, "Please check your email", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Forgotpassword.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Forgotpassword.this, "Error: " , Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
