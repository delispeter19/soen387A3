<%@ page import="com.a3.JspConfig.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="org.json.JSONArray"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="com.a3.Mappers.CourseMapper"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        CourseMapper mapper = new CourseMapper(db);
        JSONArray courses = mapper.findAll();

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
