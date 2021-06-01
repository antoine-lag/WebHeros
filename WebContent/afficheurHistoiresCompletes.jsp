<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Lire une aventure</title>
	</head>
	<body>
		<%String infos = request.getParameter("histoireComplete");%>
		<%String[] infos_spl = infos.split("\n");%>
		<%for (String s : infos_spl) {%>
			<p><%=s %></p>
			<br>
		<%}%>
	</body>
</html>