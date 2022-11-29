package com.a3.Mappers;

import com.a3.Enums.AdminSQL;
import com.a3.Interfaces.DBModel;
import com.a3.Interfaces.PersonMapper;
import com.a3.Models.Administrator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.TypeConstraintException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministratorMapper implements PersonMapper {
    private final Connection conn;

    public AdministratorMapper(Connection db){
        conn = db;
    }

    @Override
    public JSONArray findAll() throws SQLException {
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(AdminSQL.SELECT.getQuery())){
                List<JSONObject> admins = new ArrayList<>();
                Administrator admin = new Administrator();
                while(rs.next()) {
                    load(admin, rs);
                    admins.add(getJsonFrom(admin));
                }

                return new JSONArray(admins);
            }
        }
    }

    @Override
    public boolean find(DBModel model) throws SQLException {
        Administrator admin = (Administrator) model;
        try(PreparedStatement stmt = conn.prepareStatement(AdminSQL.SELECT_ROW.getQuery())){
            stmt.setLong(1, admin.id);
            try(ResultSet rs = stmt.executeQuery()){

                if(rs.next()){
                    load(admin, rs);

                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean post(DBModel model) throws SQLException, JSONException, TypeConstraintException {
        Administrator admin = (Administrator) model;
        try(PreparedStatement stmt = conn.prepareStatement(AdminSQL.INSERT.getQuery(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, admin.email);
            stmt.setString(2, admin.password);
            stmt.setString(3, admin.firstName);
            stmt.setString(4, admin.lastName);
            stmt.setString(5, admin.phoneNumber);
            stmt.setString(6, admin.address);
            stmt.setString(7, admin.dateOfBirth);

            int numRows;

            try {
                numRows = stmt.executeUpdate();
            } catch(SQLException e){
                throw new TypeConstraintException("One or more values do not follow column type constraints!", e);
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                // Might be redundant
                if (numRows > 0 && generatedKeys.next()) {
                    admin.setId(generatedKeys.getLong(1));
                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean update(DBModel model) throws SQLException, JSONException {
        Administrator admin = (Administrator) model;
        try(PreparedStatement stmt = conn.prepareStatement(AdminSQL.UPDATE.getQuery())) {
            stmt.setString(1, admin.email);
            stmt.setString(2, admin.password);
            stmt.setString(3, admin.firstName);
            stmt.setString(4, admin.lastName);
            stmt.setString(5, admin.phoneNumber);
            stmt.setString(6, admin.address);
            stmt.setString(7, admin.dateOfBirth);
            stmt.setLong(8, admin.id);

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
        Administrator admin = (Administrator) model;
        try(PreparedStatement stmt = conn.prepareStatement(AdminSQL.DELETE.getQuery())) {
            stmt.setLong(1, admin.id);

            int numRows = stmt.executeUpdate();

            return numRows > 0;
        }
    }


    @Override
    public JSONObject getJsonFrom(DBModel model){
        Administrator admin = (Administrator) model;
        JSONObject json = new JSONObject();
        json.put("employment_id", admin.id);
        json.put("email", admin.email);
        json.put("password", admin.password);
        json.put("first_name", admin.firstName);
        json.put("last_name", admin.lastName);
        json.put("phone_number", admin.phoneNumber);
        json.put("address", admin.address);
        json.put("date_of_birth", admin.dateOfBirth);

        return json;
    }

    @Override
    public void load(DBModel model, ResultSet rs) throws SQLException {
        Administrator admin = (Administrator) model;
        admin.id = rs.getLong("employment_ID");
        admin.email = rs.getString("email");
        admin.password = rs.getString("password");
        admin.firstName = rs.getString("first_name");
        admin.lastName = rs.getString("last_name");
        admin.phoneNumber = rs.getString("phone_number");
        admin.address = rs.getString("address");
        admin.dateOfBirth = rs.getString("date_of_birth");
    }

    @Override
    public boolean login(DBModel model) throws SQLException, JSONException {
        Administrator admin = (Administrator) model;
        try (PreparedStatement stmt = conn.prepareStatement(AdminSQL.LOGIN.getQuery())){
            stmt.setString(1, admin.email);

            ResultSet rs = stmt.executeQuery();

            // check if email exists
            if(rs.next()) {
                // check if password matches
                if (rs.getString("password").equals(admin.password)) {
                    load(admin, rs);

                    return true;
                }
            }
        }

        return false;
    }
}
