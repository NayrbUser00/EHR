package com.example.myapplicationasa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity {

    private Database db;
    private TextView showfirstname, showlastname, showweight, showheight, showdob, showaddress;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        Button button = findViewById(R.id.button2);
        showfirstname = findViewById(R.id.showfirstname);
        showlastname = findViewById(R.id.showlastname);
        showweight = findViewById(R.id.showweight);
        showheight = findViewById(R.id.showheight);
        showdob = findViewById(R.id.showdob);
        showaddress = findViewById(R.id.showaddress);

        // Set button click listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Profileedit.class);
                startActivity(intent);
                finish();
            }
        });

        String email = null;
        if (user != null) {
            email = user.getEmail();
            Toast.makeText(this, "Logged in as: " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
        // Initialize Database
        db = new Database(this);

        // Fetch user profile using email (Replace "user@example.com" with actual logged-in user's email)
        Profile profile = db.getProfileByEmail(email);

        if (profile != null) {
            // Display data in TextViews
            showfirstname.setText(profile.getFirstName());
            showlastname.setText(profile.getLastName());
            showweight.setText(String.valueOf(profile.getWeight())); // Convert int to String
            showheight.setText(String.valueOf(profile.getWeight())); // Assuming height should be shown
            showdob.setText(profile.getage());
            showaddress.setText(profile.getAddress());
        } else {
            showfirstname.setText("No Data");
            showlastname.setText("No Data");
            showweight.setText("No Data");
            showheight.setText("No Data");
            showdob.setText("No Data");
            showaddress.setText("No Data");
        }
    }


}
