package com.a3.Models;

import com.a3.Interfaces.DBModel;

public class Person implements DBModel {
    public Long id;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String address;
    public String dateOfBirth;

    public Person() {

    }

    public Person(Long id){
        this.id = id;
    }

    public Person(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Person(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String address,
            String dateOfBirth){
        this(null, email, password, firstName, lastName, phoneNumber, address, dateOfBirth);
    }

    public Person(
            Long id,
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String address,
            String dateOfBirth){
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public void setId(Long id){
        this.id = id;
    }
}
