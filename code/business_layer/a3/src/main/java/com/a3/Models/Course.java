package com.a3.Models;

import com.a3.Interfaces.DBModel;

public class Course implements DBModel {
    public Long courseCode;
    public String courseTitle;
    public String roomNumber;
    public String instructor;
    public String days;
    public String courseTime;
    public String semester;
    public String startDate;
    public String endDate;

    public Course(){

    }

    public Course(Long code){
        courseCode = code;
    }

    public Course(
            Long courseCode,
            String courseTitle,
            String roomNumber,
            String instructor,
            String days,
            String courseTime,
            String semester,
            String startDate,
            String endDate){
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.roomNumber = roomNumber;
        this.instructor = instructor;
        this.days = days;
        this.courseTime = courseTime;
        this.semester = semester;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setCourseCode(Long code) {
        courseCode = code;
    }

}
