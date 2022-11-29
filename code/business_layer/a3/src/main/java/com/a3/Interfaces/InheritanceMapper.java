package com.a3.Interfaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.TypeConstraintException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public interface InheritanceMapper {
    JSONArray findAll() throws SQLException;
    boolean find(DBModel model) throws SQLException;
    boolean post(DBModel model) throws SQLException, JSONException, TypeConstraintException, ParseException;
    boolean update(DBModel model) throws SQLException, JSONException;
    boolean delete(DBModel model) throws SQLException, ParseException;
    JSONObject getJsonFrom(DBModel model);
    void load(DBModel model, ResultSet rs) throws SQLException;
}
