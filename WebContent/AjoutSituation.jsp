<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="AjoutSituation.css"/>
		<title>Ajout de Situations</title>
		<script type="text/javascript" src="ajout_choix.js"></script>
	</head>
	<body>
		<h1>Ajout d'une situation</h1>
		<h2>Vous voici dans la partie <b>édition</b> de l'histoire. Veuillez renseigner la <b>situation</b> inédite dans laquelle se trouve l'utilisateur ainsi que les <b>choix</b> que se présentent à lui</h2>
		<form action = "Servlet" method = "post" id="formAjout">
			<label>Texte de la situation :</label>
			<input type="hidden" id="modeSend" name="mode" value="ajoutSituation"/>
			<input type="hidden" name="idChoixSource" value="<%=request.getParameter("idChoixSource")%>"/>
			
			
			<p>
	    		<textarea rows="10" cols="30" wrap = "hard" id="inputTexte" name="texteSituation" placeholder="Texte de la situation" required></textarea>
	    	</p>
	    	
	   		<p class="button" id="boutonAjout">
	    		<button type="button" onclick="nouveauChoix()" id="bouton_ajout">Nouveau Choix</button>
	    	</p>

	    	<label for="nouvellesSituations">Options de choix :</label>
	 		<div id = "nouvellesSituations"></div>
	 		
	    	<input type="text" id="n1" name="choixSuite" required/>
	    	<div id="nouvSitu"></div>

	   	    <div class="button" id="boutonSend">
	        	<button type="submit">Envoyer</button>
	    	</div>
		</form>
	</body>
</html>
