<%@ page import="com.a3.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="com.a3.Mappers.CourseMapper"%>
<%@ page import="com.a3.Models.Course"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    CourseMapper mapper;
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        mapper = new CourseMapper(db);

        Course course = new Course(Long.parseLong(request.getParameter("code")));

        if (mapper.delete(course)){
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "Success");
            payload.put("data", course.courseCode);
        } else {
            response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
            payload.put("message", "Course delete failed. No rows affected.");
            payload.put("error", "Course code not found.");
        }

    } catch(NumberFormatException e){
        response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
        payload.put("message", "Could not parse course code!");
    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }
%>

<%= payload %>
