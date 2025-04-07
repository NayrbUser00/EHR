package com.example.myapplicationasa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class home extends AppCompatActivity {


private TextView Welcomebanner,agebanner,bmi;
private Database db;
private  ImageButton filemanager, medicalnotes, editprofile;


private static final int PICK_IMAGE_REQUEST = 1;
private ImageView profileimage;
FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);




        Welcomebanner = findViewById(R.id.Welcomebanner);
        agebanner = findViewById(R.id.agebanner);
        bmi = findViewById(R.id.BMI);
        db = new Database(this);
        //for profile image
        profileimage = findViewById(R.id.profileimage);
        profileimage.setOnClickListener(v -> openImageChooser());

        String email = null;
        if (user != null) {
            email = user.getEmail();
            Toast.makeText(this, "Logged in as: " + email, Toast.LENGTH_SHORT).show();
            loadImage(email);
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
       profile = db.getProfileByEmail(email);
        if (profile!= null)  {
            Welcomebanner.setText("Welcome "+ profile.getFirstName() + " "+ profile.getLastName() + "!");
            agebanner.setText("Your current age: " + profile.getage());
            bmi.setText("Your current BMI is" + getbmi());
        }
        else {
            Welcomebanner.setText("Please complete the profile details");
        }

        //for documents button
       filemanager = findViewById(R.id.filemanager);
        filemanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, documents.class);
                startActivity(intent);
                finish();
            }

        });


        editprofile = findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, Profileedit.class);
                startActivity(intent);
                finish();
            }
        });

        // for medical notes
        medicalnotes = findViewById(R.id.medicalnotes);
        medicalnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, MedicalNotes.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Convert the URI to a bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Get the current user email to save the image uniquely

                if (user != null) {
                    String email = user.getEmail().replace(".", "_");
                    saveImage(bitmap, email);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void saveImage(Bitmap bitmap, String email) {
        try {
            // Create a file path based on the user's email
            File directory = getFilesDir();
            File imageFile = new File(directory, email + "_profile_image.png");

            // Save the bitmap to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            // After saving, load the image into the ImageView
            profileimage.setImageBitmap(bitmap);
            Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(String email) {
        try {
            // Create the file path using the user's email
            File directory = getFilesDir();
            File imageFile = new File(directory, email.replace(".", "_") + "_profile_image.png");

            // If the file exists, load it into the ImageView
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                profileimage.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getbmi() {
        if (profile == null) {
            return "Profile data unavailable"; // Check if profile is null
        }

        double weight = profile.getWeight(); // Ensure weight is a double
        double height = (double) profile.getheight() / 100.0; // Convert cm to meters

        if (height <= 0) {
            return "Invalid height value"; // Prevent division by zero
        }

        double bmi = weight / (height * height); // Correct BMI formula

        // Format the output properly
        if (bmi < 18.5) {
            return String.format("%.2f - Underweight", bmi);
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return String.format("%.2f - Healthy", bmi);
        } else if (bmi >= 25 && bmi <= 29.9) {
            return String.format("%.2f - Overweight", bmi);
        } else {
            return String.format("%.2f - Obese", bmi);
        }
    }






}