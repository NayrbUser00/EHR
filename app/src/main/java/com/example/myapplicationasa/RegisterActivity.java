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


import java.util.regex.*;

public class RegisterActivity extends AppCompatActivity {

    EditText Firstname,Lastname, Username,Password, Confirmpassword ,Email;
    Button signup;
    TextView textView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);



        Firstname = findViewById(R.id.editFirstname);
        Lastname = findViewById(R.id.editLastname);
        Email = findViewById(R.id.editEmail);
        Username = findViewById(R.id.editUsername);
        Password = findViewById(R.id.editPassword);
        Confirmpassword = findViewById(R.id.editConfirmPassword);
        signup = findViewById(R.id.editSignup);
        textView2 = findViewById(R.id.textView2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = Firstname.getText().toString();
                String lastname = Lastname.getText().toString();
                String username= Username.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String confirmpassword = Confirmpassword.getText().toString();

                //object for database
                //Database db = new Database(getApplicationContext(), "EHR users", null,1);


                if (username.length() ==0 || Password.length() == 0 || firstname.length() ==0 || lastname.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please complete details ", Toast.LENGTH_SHORT).show();
                }
                else if (passwordvalidation(password)){
                    Toast.makeText(getApplicationContext(), "Minimum of 8 character length", Toast.LENGTH_LONG).show();
                }

                else if (!password.equals(confirmpassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match!!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(getApplicationContext(),"Register Successful ", Toast.LENGTH_SHORT).show();
                    //database register function
                   /* db.registrar(firstname,lastname,username,email,password);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    */
                }
            }


    });


}

    public static boolean passwordvalidation(String password) {

        if (password.length() < 8) {
            return false;
        }


        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

}



