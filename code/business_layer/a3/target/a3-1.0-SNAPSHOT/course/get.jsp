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
    JSONArray courses;
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        finder = new DBModelFinder(db);
        courses = finder.findCourses();

        if(courses.isEmpty()){
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "No Courses Found");
            payload.put("data", courses);
        } else {
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "Success");
            payload.put("data", courses);
        }

    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }
%>

<%= payload %>
