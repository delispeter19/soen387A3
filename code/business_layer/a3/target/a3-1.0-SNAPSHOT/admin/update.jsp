<%@ page import="com.a3.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="javax.xml.bind.TypeConstraintException"%>
<%@ page import="org.json.JSONException"%>
<%@ page import="com.a3.Mappers.AdministratorMapper"%>
<%@ page import="com.a3.Models.Administrator"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    AdministratorMapper mapper;
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        mapper = new AdministratorMapper(db);

        JSONObject data = new JSONObject(request.getReader().lines()
            .collect(Collectors.joining(System.lineSeparator())));

        Administrator admin = new Administrator(
            data.getLong("id"),
            data.getString("email"),
            data.getString("password"),
            data.getString("first_name"),
            data.getString("last_name"),
            data.getString("phone_number"),
            data.getString("address"),
            data.getString("date_of_birth"));

        if (mapper.update(admin)){
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "Success");
            payload.put("data", admin.toJson());
        } else {
            response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
            payload.put("message", "Admin update failed. No rows affected.");
            payload.put("error", "Admin id not found.");
        }

    } catch(JSONException e){
        response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
        payload.put("message", "Incorrect or Missing field name(s) in request body!");
        payload.put("error", e);
    } catch(TypeConstraintException e){
        response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
        payload.put("message", e.getMessage());
        payload.put("error", e.getLinkedException());
    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }
%>

<%= payload %>
