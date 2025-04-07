package com.example.myapplicationasa;

public class Profile {
    private String firstName;
    private String lastName;
    private int weight;
    private String address;
    private String age;
    private String email;

    private  int height;


    // Constructors, getters, and setters
    public Profile(String firstName, String lastName, int weight,  int height, String address, String age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.weight = weight;
        this.address = address;
        this.age = age;
        this.email = email;
        this.height = height;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getWeight() { return weight; }
    public String getAddress() { return address; }
    public String getage() { return age; }
    public String getEmail() { return email; }
    public int getheight(){return height;}
}



