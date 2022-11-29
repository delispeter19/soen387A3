package com.a3.Mappers;

import com.a3.Enums.CourseSQL;
import com.a3.Interfaces.DBModel;
import com.a3.Interfaces.InheritanceMapper;
import com.a3.Models.Course;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.TypeConstraintException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseMapper implements InheritanceMapper {
    private final Connection conn;

    public CourseMapper(Connection db){
        conn = db;
    }

    @Override
    public JSONArray findAll() throws SQLException {
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(CourseSQL.SELECT.getQuery())){
                List<JSONObject> courses = new ArrayList<>();
                Course course = new Course();
                while(rs.next()) {
                    load(course, rs);
                    courses.add(getJsonFrom(course));
                }

                return new JSONArray(courses);
            }
        }
    }

    @Override
    public boolean find(DBModel model) throws SQLException {
        Course course = (Course) model;
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.SELECT_ROW.getQuery())){
            stmt.setLong(1, course.courseCode);
            try(ResultSet rs = stmt.executeQuery()){

                if(rs.next()){
                    load(course, rs);

                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean post(DBModel model) throws SQLException, JSONException, TypeConstraintException {
        Course course = (Course) model;
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.INSERT.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, course.courseCode);
            stmt.setString(2, course.courseTitle);
            stmt.setString(3, course.roomNumber);
            stmt.setString(4, course.instructor);
            stmt.setString(5, course.days);
            stmt.setString(6, course.courseTime);
            stmt.setString(7, course.semester);
            stmt.setString(8, course.startDate);
            stmt.setString(9, course.endDate);

            int numRows;

            try {
                numRows = stmt.executeUpdate();
            } catch(SQLException e){
                throw new TypeConstraintException("One or more values do not follow column type constraints!", e);
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                // Might be redundant
                if (numRows > 0 && generatedKeys.next()) {
                    course.setCourseCode(generatedKeys.getLong(1));
                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean update(DBModel model) throws SQLException, JSONException {
        Course course = (Course) model;
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.UPDATE.getQuery())) {
            stmt.setString(1, course.courseTitle);
            stmt.setString(2, course.roomNumber);
            stmt.setString(3, course.instructor);
            stmt.setString(4, course.days);
            stmt.setString(5, course.courseTime);
            stmt.setString(6, course.semester);
            stmt.setString(7, course.startDate);
            stmt.setString(8, course.endDate);
            stmt.setLong(9, course.courseCode);

            int numRows;

            try {
                numRows = stmt.executeUpdate();
            } catch(SQLException e){
                throw new TypeConstraintException("One or more values do not follow column type constraints!", e);
            }

            return numRows > 0;
        }
    }

    @Override
    public boolean delete(DBModel model) throws SQLException {
        Course course = (Course) model;
        try(PreparedStatement stmt = conn.prepareStatement(CourseSQL.DELETE.getQuery())) {
            stmt.setLong(1, course.courseCode);

            int numRows = stmt.executeUpdate();

            return numRows > 0;
        }
    }

    @Override
    public JSONObject getJsonFrom(DBModel model){
        Course course = (Course) model;
        JSONObject json = new JSONObject();
        json.put("course_code", course.courseCode);
        json.put("course_title", course.courseTitle);
        json.put("room_number", course.roomNumber);
        json.put("instructor", course.instructor);
        json.put("days", course.days);
        json.put("course_time", course.courseTime);
        json.put("semester", course.semester);
        json.put("start_date", course.startDate);
        json.put("end_date", course.endDate);

        return json;
    }

    @Override
    public void load(DBModel model, ResultSet rs) throws SQLException {
        Course course = (Course) model;
        course.courseCode = rs.getLong("course_code");
        course.courseTitle = rs.getString("course_title");
        course.roomNumber = rs.getString("room_number");
        course.instructor = rs.getString("instructor");
        course.days = rs.getString("days");
        course.courseTime = rs.getString("course_time");
        course.semester = rs.getString("semester");
        course.startDate = rs.getString("start_date");
        course.endDate = rs.getString("end_date");
    }
}
