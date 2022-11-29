package com.a3.Models;

public class Administrator extends Person {

    public Administrator(){

    }

    public Administrator(Long id){
        super(id);
    }

    public Administrator(String email, String password){
        super(email, password);
    }

    public Administrator(
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

    public Administrator(
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
