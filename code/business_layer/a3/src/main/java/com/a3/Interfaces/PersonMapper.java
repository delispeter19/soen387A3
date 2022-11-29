package com.a3.Interfaces;

import org.json.JSONException;

import java.sql.SQLException;

public interface PersonMapper extends InheritanceMapper{
    boolean login(DBModel model) throws SQLException, JSONException;
}
