package com.example.myapplicationasa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


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
                Column_AdmissionType + " TEXT CHECK( " + Column_AdmissionType + " IN ('Medical Checkup', 'Emergency Admission', 'Elective Admission')), " +
                Column_InitialTest + " TEXT, " +
                Column_TestDone + " TEXT, " +
                Column_TestDate + " INTEGER, " +
                Column_TestResult + " TEXT, " +
                Column_Recommendation + " TEXT, " +
                Column_demail + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addmedicnotes(Integer patientID, String physician, String admissionType,
                                 String testDone, Integer testDate, String testResult,
                                 String recommendation, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (patientID != null) {
            cv.put(Column_Patientid, patientID);
        } else {
            cv.putNull(Column_Patientid);
        }

        cv.put(Column_Physician, physician);
        cv.put(Column_AdmissionType, admissionType);
        cv.put(Column_TestDone, testDone);
        cv.put(Column_TestDate, testDate); // You might add a similar check here if needed
        cv.put(Column_TestResult, testResult);
        cv.put(Column_Recommendation, recommendation);
        cv.put(Column_demail, email);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "User saved successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    public List<Medical_notes> getmedicalnotes(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Column_demail + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        List<Medical_notes> medicalNotesList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {  // Loop through all rows
                int patientId = cursor.getInt(cursor.getColumnIndexOrThrow(Column_Patientid));
                String physician = cursor.getString(cursor.getColumnIndexOrThrow(Column_Physician));
                String admissionType = cursor.getString(cursor.getColumnIndexOrThrow(Column_AdmissionType));
                String testDone = cursor.getString(cursor.getColumnIndexOrThrow(Column_TestDone));
                int testDate = cursor.getInt(cursor.getColumnIndexOrThrow(Column_TestDate));
                String testResult = cursor.getString(cursor.getColumnIndexOrThrow(Column_TestResult));
                String recommendation = cursor.getString(cursor.getColumnIndexOrThrow(Column_Recommendation));

                // Create a new Medical_notes object and add it to the list
                Medical_notes medicalNotes = new Medical_notes(
                        patientId, physician, admissionType, testDone, testDate, testResult, email, recommendation
                );
                medicalNotesList.add(medicalNotes);
            }
            cursor.close();
        }

        return medicalNotesList;  // Return the list of medical notes
    }



}
