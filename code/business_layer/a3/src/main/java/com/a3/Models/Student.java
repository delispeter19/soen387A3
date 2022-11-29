package com.a3.Models;

public class Student extends Person {

    public Student(){}

    public Student(Long id){
        super(id);
    }

    public Student(String email, String password){
        super(email, password);
    }

    public Student(
            Long id,
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String address,
            String dateOfBirth){
        super(id, email, password, firstName, lastName, phoneNumber, address, dateOfBirth);
    }

    public Student(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String address,
            String dateOfBirth){
        super(email, password, firstName, lastName, phoneNumber, address, dateOfBirth);
    }
}
