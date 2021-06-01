<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pack.data.*" %>
<%@ page import="pack.aux.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="reprendre_aventure.css"/>
		<title>Reprendre une aventure</title>
	</head>
	<body>
		<%InfoTableauBord infos = (InfoTableauBord) request.getSession(false).getAttribute("infoTableauBord");%>
		<%List<Integer> listeIdCheminement = infos.getIdsCheminements();%>
		<%List<String> listeTexteCheminement = infos.getTextesCheminements();%>
		<%List<Boolean> listeIsActive = infos.getIsActiveCheminements();%> 
		<h1>Partie en cours:</h1>
		<form action="Servlet" method="get">
			<input type="hidden" name="mode" value="accueil"/>
			<%for (int i=0; i<listeIdCheminement.size(); i++) {%>
				<%if (listeIsActive.get(i)) {%>
					<p><%=listeTexteCheminement.get(i)%><input type="radio" name="idCheminement" 
					value="<%=listeIdCheminement.get(i)%>"/><p/>
				<%}%>
			<%}%>
			<input type="submit" value="C'est parti!"/>
		</form>
		<h1>Partie finie:</h1>
		<%for (int i=0; i<listeIdCheminement.size(); i++) {%>
			<%if (!listeIsActive.get(i)) {%>
				<p><%=listeTexteCheminement.get(i)%><p/>
			<%}%>
		<%}%>
	</body>
</html>