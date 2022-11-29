<%@ page import="com.a3.JspConfig.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="javax.xml.bind.TypeConstraintException"%>
<%@ page import="com.a3.Mappers.AdministratorMapper"%>
<%@ page import="org.json.JSONException"%>
<%@ page import="com.a3.Models.Administrator"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        AdministratorMapper mapper = new AdministratorMapper(db);

        JSONObject data = new JSONObject(request.getReader().lines()
            .collect(Collectors.joining(System.lineSeparator())));

        Administrator admin = new Administrator(
            data.getString("email"),
            data.getString("password"),
            data.getString("first_name"),
            data.getString("last_name"),
            data.getString("phone_number"),
            data.getString("address"),
            data.getString("date_of_birth"));

        if (mapper.post(admin)){
            response.setStatus(HttpURLConnection.HTTP_OK);
            payload.put("message", "Success");
            payload.put("data", mapper.getJsonFrom(admin));
        } else {
            response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            payload.put("message", "Admin insert failed. No rows affected.");
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
