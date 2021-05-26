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
		<title>Tableau de Bord</title>
	</head>
	<body>
		<form action="Servlet" method="get">
			<input type="hidden" name="mode" value = "accueil">	
			<%InfoTableauBord infos = (InfoTableauBord) request.getSession().getAttribute("infoTableauBord");%>
			<% listeIdAventure = infos.getIdsAventures();%>
			<% listeNomAventure = infos.getNomsAventures();%>
			<%for (i=0; i<listeIdAventure.size(); i++) {%>
				<p><%=listeNomAventure.get(i)%> <input type="radio" name="aventure" value="<%=listeIdAventure.get(i)%>"/><p/>
			<%}%>
			<input type="submit" value="Inscription"/>
		</form>
	</body>
</html>