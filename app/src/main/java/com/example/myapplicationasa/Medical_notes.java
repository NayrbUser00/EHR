package com.example.myapplicationasa;

public class Medical_notes {
    private  int Patientid;
    private String Physcian;
    private String AdmisionType;
    private String TestDone;
    private int TestDate;

    private String TestResult;
    private String Recommendation;
    private String demail;

    public Medical_notes(Integer Patientid, String Physcian, String AdmisionType, String TestDone,
                         Integer TestDate, String TestResult, String Recommendation, String email){
        this.Patientid = Patientid;
        this.Physcian = Physcian;
        this.AdmisionType = AdmisionType;
        this.TestDone = TestDone;
        this.TestDate = TestDate;
        this.TestResult = TestResult;
        this.Recommendation = Recommendation;
        this.demail = email;
    }

    public int getPatientid(){return Patientid;}
    public String getPhyscian(){return Physcian;}
    public String getAdmisionType(){return AdmisionType;}
    public String getTestDone(){return TestDone;}
    public int getTestDate(){return TestDate;}
    public String getTestResult(){return TestResult;}
    public String getRecommendation(){return Recommendation;}
    public String getDemail(){return  demail;}
}
