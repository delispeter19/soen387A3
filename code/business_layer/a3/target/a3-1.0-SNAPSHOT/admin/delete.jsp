<%@ page import="com.a3.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="com.a3.Mappers.AdministratorMapper"%><%@ page import="com.a3.Models.Administrator"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    AdministratorMapper mapper;
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        mapper = new AdministratorMapper(db);

        Administrator admin = new Administrator(Long.parseLong(request.getParameter("id")));

        if (mapper.delete(admin)){
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "Success");
            payload.put("data", admin.id);
        } else {
            response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
            payload.put("message", "Admin delete failed. No rows affected.");
            payload.put("error", "Admin id not found.");
        }

    } catch(NumberFormatException e){
        response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
        payload.put("message", "Could not parse id!");
    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }
%>

<%= payload %>
