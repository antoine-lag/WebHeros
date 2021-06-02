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
	<body>
		<h1>Tableau de Bord :</h1>
		<h2>Accomplissement</h2>
		<%InfoTableauBord infos = (InfoTableauBord) request.getSession().getAttribute("infoTableauBord");%>
		<%List<String> listeTexteAccomplissement = infos.getTextesAccomplissements();%>
		<%List<String> listeDateAccomplissement = infos.getDatesAccomplissements();%>
		<%for (int i=0; i<listeTexteAccomplissement.size(); i++) {%>
			<p>
				<%=listeDateAccomplissement.get(i)%> <br> <%=listeTexteAccomplissement.get(i)%>
			</p>
		<%}%>
		<h2>Statistiques</h2>
		<%StatistiqueUtilisateur stats = infos.getStats();%>
		<p>
			Nombre de situations visitees: <%=stats.getNbSituationsVisitees()%>
		</p>
		<p>
			Nombre de situations crees: <%=stats.getNbSituationsCrees()%>
		</p>		
		<p>
			Nombre de situations visitees: <%=stats.getNbSituationsCreeesValidees()%>
		</p>
		<p>
			Nombre de votes: <%=stats.getNbVotes()%>
		</p>
		<p>
			Nombre de votes positifs: <%=stats.getNbVotesPositifs()%>
		</p>		
		<p>
			Nombre de votes negatifs: <%=stats.getNbVotesNegatifs()%>
		</p>		
		<p>
			Nombre d'aventures commencees: <%=stats.getNbAventuresCommencees()%>
		</p>
		<p>
			Nombre d'accomplissements: <%=stats.getNbAccomplissementsRecus()%>
		</p>
		
		<a href="accueil.jsp">Retour a l'accueil</a>
		<p id="separateur"></p>
	</body>
</html>