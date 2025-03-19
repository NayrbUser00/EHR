package com.example.myapplicationasa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE users (firstname TEXT, lastname TEXT, username TEXT, email TEXT, password TEXT)";
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void registrar(String firstname, String lastname, String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        cv.put("username", username);
        cv.put("email", email);  // Store email value
        cv.put("password", password); // Store password value

        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
    }



    public boolean login (String username, String password){
        boolean result = false;
        String str[] = new String[2];
        str [0] = username;
        str [1]= password;
        SQLiteDatabase db = getReadableDatabase();
       Cursor c = db.rawQuery ("select * from users where username =? and password =?",str);
       if (c.moveToFirst()) {
            result  = true;
       }
        return result;
   }
}


