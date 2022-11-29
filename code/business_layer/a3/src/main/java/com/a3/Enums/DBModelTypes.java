package com.a3.Enums;

public enum DBModelTypes {
    STUDENT("student"),
    ADMINISTRATOR("administrator"),
    COURSE("course"),
    STUDENT_COURSE("student-course")
    ;

    private final String type;

    DBModelTypes(String s) {
        type = s;
    }

    public String getType(){
        return type;
    }
}
