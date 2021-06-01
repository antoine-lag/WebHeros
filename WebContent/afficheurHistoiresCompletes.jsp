<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="afficheurHistoiresCompletes.css"/>
		<title>Lire une aventure</title>
	</head>
	<h1>Récapitulatif</h1>
	<body>
		<%String infos = request.getParameter("histoireComplete");%>
		<%String[] infos_spl = infos.split("\n");%>
		<%for (String s : infos_spl) {%>
			<p><%=s %></p>
		<%}%>
	</body>
</html>