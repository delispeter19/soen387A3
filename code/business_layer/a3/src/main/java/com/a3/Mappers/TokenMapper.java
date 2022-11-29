package com.a3.Mappers;

import com.a3.Enums.CourseSQL;
import com.a3.Enums.TokenSQL;
import com.a3.Interfaces.DBModel;
import com.a3.Interfaces.InheritanceMapper;
import com.a3.Models.Token;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.TypeConstraintException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TokenMapper implements InheritanceMapper {
    private final Connection conn;

    public TokenMapper(Connection db){
        conn = db;
    }

    public JSONArray findAll() throws SQLException {
        return null;
    }

    @Override
    public boolean find(DBModel model) throws SQLException {
        Token token = (Token) model;
        try(PreparedStatement stmt = conn.prepareStatement(TokenSQL.SELECT_ROW.getQuery())){
            stmt.setString(1, token.id);
            try(ResultSet rs = stmt.executeQuery()){

                if(rs.next()){
                    load(token, rs);

                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public boolean post(DBModel model) throws SQLException, JSONException, TypeConstraintException {
        Token token = (Token) model;
        try(PreparedStatement stmt = conn.prepareStatement(TokenSQL.INSERT.getQuery())) {
            stmt.setString(1, token.id);
            stmt.setLong(2, token.userId);
            stmt.setString(3, token.userType);

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
    public boolean update(DBModel model) throws SQLException, JSONException {
        return false;
    }

    @Override
    public boolean delete(DBModel model) throws SQLException {
        Token token = (Token) model;
        try(PreparedStatement stmt = conn.prepareStatement(TokenSQL.DELETE.getQuery())) {
            stmt.setString(1, token.id);

            int numRows = stmt.executeUpdate();

            return numRows > 0;
        }
    }

    @Override
    public JSONObject getJsonFrom(DBModel model) {
        return null;
    }

    @Override
    public void load(DBModel model, ResultSet rs) throws SQLException {
        Token token = (Token) model;
        token.id = rs.getString("id");
        token.userId = rs.getLong("user_id");
        token.userType = rs.getString("user_type");
    }
}
