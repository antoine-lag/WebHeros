<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<%! int i; %>
<%! List<Integer> listeIdAventure; %>
<%! List<String> listeNomAventure; %>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="tableau_de_bord.css"/>
		<title>Tableau de Bord</title>
	</head>
	<body>
		<h1>Mon tableau de Bord</h1>
		<form action="Servlet" method="get">
			<h4>Démarrer une aventure :</h4>
			<input type="hidden" name="mode" value = "accueil">	
			<%InfoTableauBord infos = (InfoTableauBord) request.getSession().getAttribute("infoTableauBord");%>
			<% listeIdAventure = infos.getIdsAventures();%>
			<% listeNomAventure = infos.getNomsAventures();%>
			<%for (i=0; i<listeIdAventure.size(); i++) {%>
				<p><%=listeNomAventure.get(i)%> <input type="radio" name="aventure" value="<%=listeIdAventure.get(i)%>"/><p/>
			<%}%>
			<input type="submit" value="C'est parti !"/>
		</form>
	</body>
</html>