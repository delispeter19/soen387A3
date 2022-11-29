package com.a3.Models;

import com.a3.Interfaces.DBModel;

public class StudentCourse implements DBModel {
    public Long courseCode;
    public Long studentId;

    public StudentCourse(){

    }

    public StudentCourse(Long id, Long code){
        studentId = id;
        courseCode = code;
    }
}
