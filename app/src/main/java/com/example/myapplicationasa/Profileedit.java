package com.example.myapplicationasa;

import static android.widget.Toast.LENGTH_SHORT;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class Profileedit extends AppCompatActivity {

    EditText Firstname, Lastname, age, weight, height, address;
    Button button, addphoto;



    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profileedit);

        Firstname = findViewById(R.id.Firstname);
        Lastname = findViewById(R.id.Lastname);
        age = findViewById(R.id.dateofbirth);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        address = findViewById(R.id.address);
        button = findViewById(R.id.savebutton);
        addphoto = findViewById(R.id.addprofile);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To get the email parameter
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String email = getEmail(currentUser);


                if (!Enterall()) {
                    Toast.makeText(Profileedit.this, "Please ensure fields have data", LENGTH_SHORT).show();
                    return;
                }
                String firstname = Firstname.getText().toString();
                String lastname = Lastname.getText().toString();
                String dob = age.getText().toString();
                String weightText = weight.getText().toString();
                String heightText = height.getText().toString();
                String addressText = address.getText().toString();

                int weight = 0;
                int height = 0;
                try {
                    weight = Integer.parseInt(weightText);
                    height = Integer.parseInt(heightText);
                } catch (NumberFormatException e) {
                    // You could handle the error (e.g., show a toast or log the issue)
                    Log.e("Input Error", "Invalid input for weight or height");
                    return; // Don't proceed if input is invalid
                }

                //save data
                if (saveUser(firstname, lastname, weight, height, dob, addressText, email)) {
                    Toast.makeText(Profileedit.this, "Success", LENGTH_SHORT).show();
                    Intent intent = new Intent(Profileedit.this, home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Profileedit.this, "Failed", LENGTH_SHORT).show();
                }


            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    //to check if input was blank
    private boolean Enterall() {
        return !(Firstname.getText().toString().trim().isEmpty() ||
                Lastname.getText().toString().trim().isEmpty() ||
                age.getText().toString().trim().isEmpty() ||
                weight.getText().toString().trim().isEmpty() ||
                height.getText().toString().trim().isEmpty() ||
                address.getText().toString().trim().isEmpty()
        );
    }

    private String getEmail(FirebaseUser currentUser) {
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            Toast.makeText(this, "User not logged in", LENGTH_SHORT).show();
            return null;
        }
    }


    //To save data to database
    private boolean saveUser(String firstname, String lastname, int weight, int height, String dob, String addressText, String email) {
        if (email != null) {
            Database db = new Database(Profileedit.this);
            return db.addUser(firstname, lastname, weight, height, dob, addressText, email);
        } else {
            Toast.makeText(Profileedit.this, "Unable to save data", LENGTH_SHORT).show();
            return false;
        }
    }

    private void saveImage(Bitmap bitmap, String email) {
        try {
            // Create a file path based on the user's email
            File directory = getFilesDir(); // Get the app's private storage directory
            File imageFile = new File(directory, email + "_profile_image.png"); // Image file name based on the email

            // Save the bitmap to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Compress and write the bitmap to file
            fos.close(); // Close the file output stream

            // After saving, pass the file path/URI to the second activity
            Intent intent = new Intent(Profileedit.this, home.class);
            intent.putExtra("imageFilePath", imageFile.getAbsolutePath()); // Pass the absolute file path to the second activity
            startActivity(intent); // Start the second activity

            Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }



}