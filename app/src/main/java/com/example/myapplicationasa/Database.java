package com.example.myapplicationasa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Database extends SQLiteOpenHelper {


    private Context context;
    private static final String DATABASE_NAME = "Profile";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_profile";
    private static final String Column_ID = "_id";
    private static final String Column_Firstname = "Firstname";
    private static final String Column_Lastname = "Lastname";
    private static final String Column_Weight = "Weight";
    private static final String Column_Height = "Height";
    private static final String Column_Age = "Age";
    private static final String Column_Address = "Address";
    private static final String Column_email = "email";


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Make sure there's a space here after `INTEGER`
                Column_Firstname + " TEXT, " +
                Column_Lastname + " TEXT, " +
                Column_Weight + " INTEGER, " +
                Column_Height + " INTEGER, " +
                Column_Age + " TEXT, " +
                Column_Address + " TEXT, " +
                Column_email + " TEXT);";

        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    //to inser data
    boolean addUser(String Firstname, String Lastname, Integer Weight, Integer Height, String Age, String address, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Column_Firstname, Firstname);
        cv.put(Column_Lastname, Lastname);
        cv.put(Column_Weight, Weight);
        cv.put(Column_Height, Height);
        cv.put(Column_Age, Age);
        cv.put(Column_Address, address);
        cv.put(Column_email, email);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            if (context != null) {
                Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show();
            } else {
                System.err.println("Context is null");
            }
            return false;
        } else {
            if (context != null) {
                Toast.makeText(context, "User saved successfully", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }


    public Profile getProfileByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Column_email + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        Profile profile = null;
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(Column_Firstname));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(Column_Lastname));
            int weight = cursor.getInt(cursor.getColumnIndexOrThrow(Column_Weight));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(Column_Height));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(Column_Address));
            String age = cursor.getString(cursor.getColumnIndexOrThrow(Column_Age));
            profile = new Profile(firstName, lastName, weight,height, address, age, email);
        }
        if (cursor != null) {
            cursor.close();


        }
        return profile;
    }


}