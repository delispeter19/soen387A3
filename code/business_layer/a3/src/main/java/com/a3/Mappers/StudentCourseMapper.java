package com.a3.Mappers;

import com.a3.Enums.CourseSQL;
import com.a3.Enums.StudentCourseSQL;
import com.a3.Interfaces.DBModel;
import com.a3.Interfaces.InheritanceMapper;
import com.a3.Models.Course;
import com.a3.Models.Student;
import com.a3.Models.StudentCourse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.TypeConstraintException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StudentCourseMapper implements InheritanceMapper {
    private final Connection conn;

    public StudentCourseMapper(Connection db){
        conn = db;
    }

    @Override
    public JSONArray findAll() throws SQLException {
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(StudentCourseSQL.SELECT.getQuery())){
                List<JSONObject> studentCourses = new ArrayList<>();
                StudentCourse studentCourse = new StudentCourse();
                while(rs.next()) {
                    load(studentCourse, rs);
                    studentCourses.add(getJsonFrom(studentCourse));
                }

                return new JSONArray(studentCourses);
            }
        }
    }

    @Override
    public boolean find(DBModel model) throws SQLException {
        StudentCourse studentCourse = (StudentCourse) model;
        try(PreparedStatement stmt = conn.prepareStatement(StudentCourseSQL.SELECT_ROW.getQuery())){
            stmt.setLong(1, studentCourse.studentId);
            stmt.setLong(2, studentCourse.courseCode);
            try(ResultSet rs = stmt.executeQuery()){

                if(rs.next()){
                    load(studentCourse, rs);

                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean post(DBModel model) throws SQLException, JSONException, TypeConstraintException, ParseException {
        StudentCourse studentCourse = (StudentCourse) model;
        if (isLessThan5Courses(studentCourse) && isLessThan1WeekAfterStart(studentCourse)){
            return postVerified(studentCourse);
        } else {
            return false;
        }
    }

    @Override
    public boolean update(DBModel model) throws SQLException, JSONException {
        return false;
    }

    @Override
    public boolean delete(DBModel model) throws SQLException, ParseException {
        StudentCourse studentCourse = (StudentCourse) model;
        if (isBeforeEndOfSemester(studentCourse)){
            return deleteVerified(studentCourse);
        } else {
            return false;
        }
    }

    @Override
    public JSONObject getJsonFrom(DBModel model){
        StudentCourse studentCourse = (StudentCourse) model;
        JSONObject json = new JSONObject();
        json.put("student_id", studentCourse.studentId);
        json.put("course_code", studentCourse.courseCode);

        return json;
    }

    @Override
    public void load(DBModel model, ResultSet rs) throws SQLException {
        StudentCourse studentCourse = (StudentCourse) model;
        studentCourse.studentId = rs.getLong("student_id");
        studentCourse.courseCode = rs.getLong("course_code");
    }

    public JSONArray findStudentsByCourse(long courseCode) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(StudentCourseSQL.SELECT_STUDENTS.getQuery())){
            stmt.setLong(1, courseCode);
            try(ResultSet rs = stmt.executeQuery()){
                List<JSONObject> students = new ArrayList<>();
                Student student = new Student();
                StudentMapper mapper = new StudentMapper(conn);
                while(rs.next()) {
                    mapper.load(student, rs);
                    students.add(mapper.getJsonFrom(student));
                }

                return new JSONArray(students);
            }
        }
    }

    public JSONArray findCoursesByStudent(long studentId) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(StudentCourseSQL.SELECT_COURSES.getQuery())){
            stmt.setLong(1, studentId);
            try(ResultSet rs = stmt.executeQuery()){
                List<JSONObject> courses = new ArrayList<>();
                Course course = new Course();
                CourseMapper mapper = new CourseMapper(conn);
                while(rs.next()) {
                    mapper.load(course, rs);
                    courses.add(mapper.getJsonFrom(course));
                }

                return new JSONArray(courses);
            }
        }
    }

    public JSONObject getCourseAsJson(StudentCourse studentCourse) throws SQLException {
        Course course = new Course(studentCourse.courseCode);
        CourseMapper mapper = new CourseMapper(conn);
        mapper.find(course);

        return mapper.getJsonFrom(course);
    }

    private boolean postVerified(StudentCourse studentCourse) throws SQLException, JSONException, TypeConstraintException {
        try(PreparedStatement stmt = conn.prepareStatement(StudentCourseSQL.INSERT.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, studentCourse.studentId);
            stmt.setLong(2, studentCourse.courseCode);

            int numRows;

            try {
                numRows = stmt.executeUpdate();
            } catch(SQLException e){
                throw new TypeConstraintException("One or more values do not follow column type constraints!", e);
            }

            return numRows > 0;
        }
    }

    private boolean deleteVerified(StudentCourse studentCourse) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(StudentCourseSQL.DELETE.getQuery())) {
            stmt.setLong(1, studentCourse.studentId);
            stmt.setLong(2, studentCourse.courseCode);

            int numRows = stmt.executeUpdate();

            return numRows > 0;
        }
    }

    private boolean isBeforeEndOfSemester(StudentCourse studentCourse) throws SQLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate = sdf.parse(getEndDate(studentCourse));
        java.util.Date now = new Date();
        TimeUnit timeUnit = TimeUnit.DAYS;

        return timeUnit.convert(now.getTime()-startDate.getTime(),TimeUnit.MILLISECONDS) < 0;
    }

    private boolean isLessThan1WeekAfterStart(StudentCourse studentCourse) throws SQLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(getStartDate(studentCourse));
        Date now = new Date();
        TimeUnit timeUnit = TimeUnit.DAYS;

        return timeUnit.convert(now.getTime()-startDate.getTime(),TimeUnit.MILLISECONDS) <= 7;
    }

    private boolean isLessThan5Courses(StudentCourse studentCourse) throws SQLException {
        String semester = getSemester(studentCourse);

        try(PreparedStatement stmt = conn.prepareStatement(StudentCourseSQL.SELECT_NUM_COURSES_BY_SEMESTER.getQuery())) {
            stmt.setString(1, semester);
            stmt.setLong(2, studentCourse.studentId);

            try(ResultSet rs = stmt.executeQuery()){
                rs.next();
                int numCourseForSemester = rs.getInt("numberOfCourses");

                return numCourseForSemester < 5;
            }
        }
    }

    private String getSemester(StudentCourse studentCourse) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.SELECT_SEMESTER.getQuery())){
            stmt.setLong(1, studentCourse.courseCode);
            try(ResultSet rs = stmt.executeQuery()){
                rs.next();
                return rs.getString("semester");
            }
        }
    }

    private String getStartDate(StudentCourse studentCourse) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.SELECT_START_DATE.getQuery())){
            stmt.setLong(1, studentCourse.courseCode);
            try(ResultSet rs = stmt.executeQuery()){
                rs.next();
                return rs.getString("start_date");
            }
        }
    }

    private String getEndDate(StudentCourse studentCourse) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.SELECT_END_DATE.getQuery())){
            stmt.setLong(1, studentCourse.courseCode);
            try(ResultSet rs = stmt.executeQuery()){
                rs.next();
                return rs.getString("end_date");
            }
        }
    }
}
