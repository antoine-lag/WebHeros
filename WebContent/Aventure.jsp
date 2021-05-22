<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pack.data.Aventure" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% Aventure av = (Aventure) request.getAttribute("Aventure"); %>
	<h1>Tu parcours l'aventure <%=av.getNom()%>	</h1>
</body>
</html>