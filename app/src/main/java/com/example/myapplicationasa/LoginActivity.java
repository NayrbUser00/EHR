package com.example.myapplicationasa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    EditText Email,Password;
    Button button;
    TextView register;
    FirebaseAuth mAuth;


    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         mAuth = FirebaseAuth.getInstance();

                Email = findViewById(R.id.Email);
                Password = findViewById(R.id.password);
                button = findViewById(R.id.button);
                register = findViewById(R.id.register);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = Email.getText().toString();
                        String password = Password.getText().toString();
                        //object for database
                        //Database db = new Database(getApplicationContext(), "EHR users", null,1);

                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(LoginActivity.this,"Please enter email", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(password)){
                            Toast.makeText(LoginActivity.this,"Please enter password", Toast.LENGTH_SHORT).show();
                            return;
                        }

               /*if(db.login(username,password)){
                   Toast.makeText(LoginActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
                   startActivity( new Intent(LoginActivity.this, home.class));
               }

               else {
                   Toast.makeText(LoginActivity.this,"Invalid login creds", Toast.LENGTH_SHORT).show();
               }*/

               //Firebase function login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(LoginActivity.this, home.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Login success.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Incorrect Credentials",
                                            Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

            }
        });





        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}