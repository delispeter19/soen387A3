<%@ page import="com.a3.JspConfig.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.stream.Collectors"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="com.a3.Models.Token"%>
<%@ page import="com.a3.Enums.DBModelTypes"%>
<%@ page import="com.a3.Mappers.StudentMapper"%>
<%@ page import="org.json.JSONException"%>
<%@ page import="com.a3.Models.Student"%>
<%@ page import="com.a3.Mappers.TokenMapper"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        StudentMapper mapper = new StudentMapper(db);

        JSONObject data = new JSONObject(request.getReader().lines()
            .collect(Collectors.joining(System.lineSeparator())));

        Student student = new Student(data.getString("email"), data.getString("password"));

        if (mapper.login(student)){
            TokenMapper tokenMapper = new TokenMapper(db);
            Token token = new Token(student.id, DBModelTypes.STUDENT.getType());

            if(tokenMapper.post(token)){
                response.setStatus(HttpURLConnection.HTTP_OK);
                payload.put("message", "Login Success");
                payload.put("user", mapper.getJsonFrom(student));
                payload.put("token", token.id);
                payload.put("type", "student");
            } else {
                response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
                payload.put("message", "Token creation failed. No rows affected.");
            }
        } else {
            response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
            payload.put("message", "Incorrect email/password!");
        }

    } catch(JSONException e){
        response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
        payload.put("message", "Incorrect or Missing field name(s) in request body!");
        payload.put("error", e);
    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }
%>

<%= payload %>
