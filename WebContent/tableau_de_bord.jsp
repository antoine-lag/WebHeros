<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<!DOCTYPE html>
<%! int compteur; %>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Tableau de Bord</title>
	</head>
	<body>
		<form action="Servlet" method="get">
			<input type="hidden" name="mode" value = "accueil">	
			<%Jeu jeu = (Jeu) request.getSession().getAttribute("id_jeu");%>
			<%compteur = 1;%>
			<%for (Aventure av : jeu.getAventure()) {%>
				<%int id = av.getId(); %>
				<input type="radio" name="aventure" value="<%=id%>"/>
            	<label for="<%=compteur%>"> <%=av.getNom()%> </label><br>
            	<%compteur++;%>
			<%}%>
		</form>
	</body>
</html>