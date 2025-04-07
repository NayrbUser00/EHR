package com.example.myapplicationasa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.PublicKey;


public class diagnosis extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "medical_notes";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "medical_notes";
    private static final String Column_ID = "_id";
    private static final String Column_Patientid = "Patientid";
    private static final String Column_Physician = "Physician";
    private static final String Column_AdmissionType = "AdmissionType";
    private static final String Column_InitialTest = "Initial_Test";
    private static final String Column_TestDone = "Test_done";
    private static final String Column_TestResult = "Test_result";
    private static final String Column_Recommendation = "Recommendation";
    private static final String Column_TestDate = "Test_Date";
    private static final String Column_demail= "email";

    public diagnosis(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Column_Patientid + " INTEGER, " +
                Column_Physician + " TEXT, " +
                Column_AdmissionType + " TEXT CHECK,( " + Column_AdmissionType + " IN ('Medical Checkup', 'Emergency Admission', 'Elective Admission')), " +
                Column_InitialTest + " TEXT, " +
                Column_TestDone + " TEXT, " +
                Column_TestDate + " INTEGER, " +
                Column_TestResult + " TEXT, " +
                Column_Recommendation + " TEXT, " +
                Column_demail + " TEXT);";


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    boolean addmedicnotes(Integer Patientid, String Physcian, String AdmisionType, String TestDone, Integer TestDate, String TestResult, String Recommendation, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Column_Patientid, Patientid);
        cv.put(Column_Physician, Physcian);
        cv.put(Column_AdmissionType, AdmisionType);
        cv.put(Column_TestDone, TestDone);
        cv.put(Column_TestDate, TestDate);
        cv.put(Column_TestResult, TestResult);
        cv.put(Column_Recommendation, Recommendation);
        cv.put(Column_demail, email);

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

    public  Medical_notes getmedicalnotes(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Column_demail + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        Medical_notes medicalNotes= null;
        if (cursor != null && cursor.moveToFirst()){
            int Patienid = cursor.getInt(cursor.getColumnIndexOrThrow(Column_Patientid));
            String Physcian = cursor.getString(cursor.getColumnIndexOrThrow(Column_Physician));
            String AdmissionType = cursor.getString(cursor.getColumnIndexOrThrow(Column_AdmissionType));
            String TestDone = cursor.getString(cursor.getColumnIndexOrThrow(Column_TestDone));
            int TestDate = cursor.getInt(cursor.getColumnIndexOrThrow(Column_TestDate));
            String TestResult = cursor.getString(cursor.getColumnIndexOrThrow(Column_TestResult));
            String Recommendation = cursor.getString(cursor.getColumnIndexOrThrow(Column_Recommendation));
            medicalNotes = new Medical_notes(Patienid,Physcian,AdmissionType,TestDone,TestDate,TestResult,email,Recommendation);
        }
        if (cursor != null) {
            cursor.close();


        }
        return medicalNotes;
    }


}
