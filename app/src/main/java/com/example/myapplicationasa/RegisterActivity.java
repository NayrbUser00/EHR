package com.example.myapplicationasa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.regex.*;

public class RegisterActivity extends AppCompatActivity {


    EditText Password, Confirmpassword ,Email;
    Button signup;
    TextView textView2;

    private FirebaseAuth mAuth;

    public static boolean passwordValidation(String password) {
        // Check if the password length is less than 8
        if (password.length() < 8) {
            return false;
        }

        // Define the regular expression for a strong password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        // Return true if the password matches the regex pattern
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);



        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.editEmail);
        Password = findViewById(R.id.editPassword);
        Confirmpassword = findViewById(R.id.editConfirmPassword);
        signup = findViewById(R.id.editSignup);
        textView2 = findViewById(R.id.textView2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String confirmPassword = Confirmpassword.getText().toString();

                // Check if email or password fields are empty
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please complete details", Toast.LENGTH_SHORT).show();
                }
                // Check if password meets the required validation
                else if (passwordValidation(password)) {
                    Toast.makeText(getApplicationContext(), "Minimum of 8 characters required", Toast.LENGTH_LONG).show();
                }
                // Check if password and confirm password match
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                }
                // Proceed with user registration if all conditions are met
                else {
                    Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registration successful, get the current user
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Add additional logic here, such as navigating to the next screen
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // Registration failed, display a message to the user
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });




        //database register function
                   /* db.registrar(firstname,lastname,username,email,password);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    */
                }
            }




