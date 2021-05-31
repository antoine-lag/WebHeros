<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="tableau_de_bord.css"/>
		<title>Tableau de bord</title>
	</head>
	<h1>Tableau de Bord :</h1>
	<body>
		<%InfoTableauBord infos = (InfoTableauBord) request.getSession().getAttribute("infoTableauBord");%>
		<%List<String> listeTexteAccomplissement = infos.getTextesAccomplissements();%>
		<%List<String> listeDateAccomplissement = infos.getDatesAccomplissements();%>
		<%for (int i=0; i<listeTexteAccomplissement.size(); i++) {%>
			<p>
				<%=listeDateAccomplissement.get(i) + "\n" + listeTexteAccomplissement.get(i)%>
			</p>
		<%}%>
		<a href="accueil.jsp">Retour a l'accueil</a>
	</body>
</html>