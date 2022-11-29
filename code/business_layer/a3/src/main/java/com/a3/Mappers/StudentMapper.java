package com.a3.Mappers;

import com.a3.Enums.StudentSQL;
import com.a3.Interfaces.DBModel;
import com.a3.Interfaces.PersonMapper;
import com.a3.Models.Student;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.TypeConstraintException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentMapper implements PersonMapper {
    private final Connection conn;

    public StudentMapper(Connection db){
        conn = db;
    }

    @Override
    public JSONArray findAll() throws SQLException {
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(StudentSQL.SELECT.getQuery())){
                List<JSONObject> students = new ArrayList<>();
                Student student = new Student();
                while(rs.next()) {
                    load(student, rs);
                    students.add(getJsonFrom(student));
                }

                return new JSONArray(students);
            }
        }
    }

    @Override
    public boolean find(DBModel model) throws SQLException {
        Student student = (Student) model;
        try(PreparedStatement stmt = conn.prepareStatement(StudentSQL.SELECT_ROW.getQuery())){
            stmt.setLong(1, student.id);
            try(ResultSet rs = stmt.executeQuery()){

                if(rs.next()){
                    load(student, rs);

                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean post(DBModel model) throws SQLException, JSONException, TypeConstraintException {
        Student student = (Student) model;
        try(PreparedStatement stmt = conn.prepareStatement(StudentSQL.INSERT.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.email);
            stmt.setString(2, student.password);
            stmt.setString(3, student.firstName);
            stmt.setString(4, student.lastName);
            stmt.setString(5, student.phoneNumber);
            stmt.setString(6, student.address);
            stmt.setString(7, student.dateOfBirth);

            int numRows;

            try {
                numRows = stmt.executeUpdate();
            } catch(SQLException e){
                throw new TypeConstraintException("One or more values do not follow column type constraints!", e);
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                // Might be redundant
                if (numRows > 0 && generatedKeys.next()) {
                    student.setId(generatedKeys.getLong(1));
                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean update(DBModel model) throws SQLException, JSONException {
        Student student = (Student) model;
        try(PreparedStatement stmt = conn.prepareStatement(StudentSQL.UPDATE.getQuery())) {
            stmt.setString(1, student.email);
            stmt.setString(2, student.password);
            stmt.setString(3, student.firstName);
            stmt.setString(4, student.lastName);
            stmt.setString(5, student.phoneNumber);
            stmt.setString(6, student.address);
            stmt.setString(7, student.dateOfBirth);
            stmt.setLong(8, student.id);

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
        Student student = (Student) model;
        try(PreparedStatement stmt = conn.prepareStatement(StudentSQL.DELETE.getQuery())) {
            stmt.setLong(1, student.id);

            int numRows = stmt.executeUpdate();

            return numRows > 0;
        }
    }

    @Override
    public JSONObject getJsonFrom(DBModel model) {
        Student student = (Student) model;
        JSONObject json = new JSONObject();
        json.put("id", student.id);
        json.put("email", student.email);
        json.put("password", student.password);
        json.put("first_name", student.firstName);
        json.put("last_name", student.lastName);
        json.put("phone_number", student.phoneNumber);
        json.put("address", student.address);
        json.put("date_of_birth", student.dateOfBirth);

        return json;
    }

    @Override
    public void load(DBModel model, ResultSet rs) throws SQLException {
        Student student = (Student) model;
        student.id = rs.getLong("ID");
        student.email = rs.getString("email");
        student.password = rs.getString("password");
        student.firstName = rs.getString("first_name");
        student.lastName = rs.getString("last_name");
        student.phoneNumber = rs.getString("phone_number");
        student.address = rs.getString("address");
        student.dateOfBirth = rs.getString("date_of_birth");
    }

    @Override
    public boolean login(DBModel model) throws SQLException, JSONException {
        Student student = (Student) model;
        try (PreparedStatement stmt = conn.prepareStatement(StudentSQL.LOGIN.getQuery())){
            stmt.setString(1, student.email);

            ResultSet rs = stmt.executeQuery();

            // check if email exists
            if(rs.next()) {
                // check if password matches
                if (rs.getString("password").equals(student.password)) {
                    load(student, rs);

                    return true;
                }
            }
        }

        return false;
    }
}
