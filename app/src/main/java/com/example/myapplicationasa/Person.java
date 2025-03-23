package com.example.myapplicationasa;

public class Person {
    private String firstname, lastname, dateofbirth, address;
    private Integer height,  weight;

    public Person(){}

    public Person(String firstname, String lastname, String dateofbirth, Integer height, Integer weight, String address ){
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateofbirth = dateofbirth;
        this.height = height;
        this.weight = weight;
        this.address = address;

    }


}
