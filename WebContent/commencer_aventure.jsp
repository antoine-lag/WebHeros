<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="commencer_aventure.css"/>
		<title>Commencer une aventure</title>
	</head>
	<body>
		<%InfoTableauBord infos = (InfoTableauBord) request.getSession(false).getAttribute("infoTableauBord");%>
		<%List<Integer> listeIdAventure = infos.getIdsAventures();%>
		<%List<String> listeNomAventure = infos.getNomsAventures();%>
		<h1>Démarrer une aventure :</h1>
		<form action="Servlet" method="get">
			<input type="hidden" name="mode" value = "accueil">	
			<%for (int i=0; i<listeIdAventure.size(); i++) {%>
				<%if (i==0) {%>
					<p><%=listeNomAventure.get(i)%> <input type="radio" name="idAventure" 
					value="<%=listeIdAventure.get(i)%>" checked/><p/>
				<%} else{%>
					<p><%=listeNomAventure.get(i)%> <input type="radio" name="idAventure" 
					value="<%=listeIdAventure.get(i)%>"/><p/>
				<%}%>
			<%}%>
			<input type="submit" value="C'est parti!"/>
		</form>
		<p id="seperateur"></p>
		<a href="accueil.jsp">Retour a l'accueil</a>
	</body>
</html>