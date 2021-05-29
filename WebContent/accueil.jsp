<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="accueil.css"/>
	<title>Accueil</title>
</head>
	<body>
		<p><a href="commencer_aventure.jsp">Commencer une nouvelle aventure!</a></p>
		<p><a href="reprendre_aventure.jsp">Reprendre une aventure!</a></p>
		<p><a href="tableau_de_bord.jsp">Acceder au tableau de bord</a></p>
		<%InfoTableauBord infos = (InfoTableauBord) request.getSession().getAttribute("infoTableauBord");%>
		<%if (!infos.getPremium()) {%>
			<p><a href="payerPremium.html">Devenir PREMIUM!!!</a></p>
		<%} else {%>
			<form action="Servlet" method="get">
				<input type="hidden" name="mode" value="accueil"/>
				<input type="hidden" name="creationAventure" value="true"/>				
				<input type="submit" value="Creer aventure"/>
			</form>
		<%}%>
	</body>
</html>