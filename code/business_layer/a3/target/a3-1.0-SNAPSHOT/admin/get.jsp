<%@ page import="com.a3.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="org.json.JSONArray"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="com.a3.Utils.DBModelFinder"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    DBModelFinder finder;
    JSONArray admins;
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        finder = new DBModelFinder(db);
        admins = finder.findAdmins();

        if(admins.isEmpty()){
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "No Admins Found");
            payload.put("data", admins);
        } else {
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "Success");
            payload.put("data", admins);
        }

    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }
%>

<%= payload %>
