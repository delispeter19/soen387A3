<%@ page import="com.a3.Database"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="java.util.regex.Pattern"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="com.a3.Models.Token"%>
<%@ page import="com.a3.Utils.MapperFactory"%>
<%@ page import="com.a3.Interfaces.InheritanceMapper"%>
<%@ page import="com.a3.Interfaces.DBModel"%><%@ page import="com.a3.Mappers.TokenMapper"%><%@ page import="com.a3.Utils.PersonFactory"%><%@ page import="com.a3.Models.Person"%>
<%@ page contentType="application/json" %>

<%
    Database database = new Database();
    JSONObject payload = new JSONObject();

    try (Connection db = database.connect()){

        if (request.getHeader("Authorization") != null){
            Pattern regex = Pattern.compile("Token (.*)");
            Matcher matcher = regex.matcher(request.getHeader("Authorization"));

            if (matcher.matches()){
                TokenMapper tokenMapper = new TokenMapper(db);
                Token token = new Token(request.getHeader("Authorization").split(" ")[1]);
                if (tokenMapper.find(token)){
                    InheritanceMapper mapper = MapperFactory.newMapper(token.userType, db);
                    Person person = PersonFactory.newPerson(token.userType, token.userId);
                    if (mapper != null && person != null){
                        if(mapper.find(person)){
                            response.setStatus(HttpURLConnection.HTTP_OK);
                            payload.put("message", "Success");
                            payload.put("user", person.toJson());
                            payload.put("type", token.userType);
                        } else {
                            response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
                            payload.put("message", "User does not exist!");
                        }
                    } else {
                        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
                        payload.put("message", "Something went wrong with a Factory!");
                        payload.put("error", "Invalid type used in Factory");
                    }
                } else {
                    response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
                    payload.put("message", "User Failure");
                    payload.put("auth", "Invalid Token");
                }
            } else {
                response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
                payload.put("message", "Please use 'Token <token>' format!");
                payload.put("error", "Incorrect Authorization Format");
            }
        } else {
            response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            payload.put("error", "Authorization Header not found!");
        }
    } catch(SQLException e){
        response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        payload.put("message", "Something went wrong!");
        payload.put("error", e);
    }

%>

<%= payload %>
