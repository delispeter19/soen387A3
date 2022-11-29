package com.a3.Factories;

import com.a3.Models.Administrator;
import com.a3.Models.Person;
import com.a3.Models.Student;

public class PersonFactory {
    public static Person newPerson(String personType, Long id){
        switch (personType){
            case "student":
                return new Student(id);
            case "administrator":
                return new Administrator(id);
            default:
                return null;
        }
    }
}
