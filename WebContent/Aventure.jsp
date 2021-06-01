<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pack.data.Aventure" %>
<!DOCTYPE html>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-route.js"></script>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="aventure.css"/>
	<title>Aventure</title>
</head>
<body>
	<div ng-app="webHerosApp" ng-controller="webHerosCtrl"> 
		
		<div class="header">
			<form action="index.html" method="get">
				<input type="submit" value="Accueil"/>
			</form>
			<form action="Servlet" method="get">
				<input type="hidden" name="mode" value="goTableauBord"/>
				<input type="submit" value="Tableau de bord"/>
			</form>
			<form action="Servlet" method="get">
				<input type="hidden" name="mode" value="deco"/>
				<input type="submit" value="Deconnexion"/>
			</form>
		</div>
		
		
		
		<h1>Vous naviguez dans l'aventure {{aventureName}}</h1>
		<br>
		
		<div ng-show="showRedirectMsg">
			Attention cette situation n'est pas validée par la communauté. Tout les choix vous redirigerons vers le tableau de bords.
		</div>
		<br>
		
		<div ng-show="showSituationText">
			<h3>{{situationText}}</h3>
		</div>
		<br>
		
		<div ng-show="showChoicesList">
			<ul>
				<li ng-repeat="c in choicesList">
					<button id="btn1" ng-click="doChoice(c)">{{c.text}}</button>
				</li>
			</ul>
		</div>
		<br>
		
		<div ng-show="showVoteButtons">
			<button ng-click="doVote('up')" ng-disabled="alreadyVoted">Up Vote</button>
			<button ng-click="doVote('down')" ng-disabled="alreadyVoted">Down vote</button>
		</div>
		<br>
		
		<div ng-show="showMessage">
			{{message}}
		</div>
	</div>
	
<script>
//- Bouton déco
//<a href="index.html">Accueil</a>
//			<a href="Servlet?mode=goTableauBord">Tableau de bord</a>
function initVars(scope) {
	scope.message = "___";
	scope.aventureName = "<%= (String) request.getSession(false).getAttribute("nomAventure") %>"
	scope.situationText = "";
	scope.situationId = "<%= String.valueOf(request.getSession(false).getAttribute("idSituation")) %>";
	scope.aventureId = "<%= String.valueOf(request.getSession(false).getAttribute("idAventure")) %>";
	scope.userId = "<%= String.valueOf(request.getSession(false).getAttribute("idJoueur")) %>";
	//scope.cheminementId = "<%= String.valueOf(request.getSession(false).getAttribute("idCheminement")) %>";
	scope.choicesList = [];
	scope.lastChoiceId = "1";
	scope.validatedSituation = "true";//Si la situation a atteint un nombre de upvote
}
function initView(scope) {
	scope.showSituationText = false;
	scope.showChoicesList = true;
	scope.showVoteButtons = true;
	scope.showAddSituation = false;
	scope.showMessage = false;
	scope.showRedirectMsg = false;
	scope.alreadyVoted = false;
	console.log("initView() : alreadyVoted = " + scope.alreadyVoted);
}

//Retrieve the situation data (id+text+choices) determined by the situation's ID
//OK
function getSituation(idSituation, scope, http){
	http.get("rest/getsituation?idSituation="+idSituation+
			 "&idJoueur="+scope.userId+
			"&idAventure="+scope.aventureId).then(function(response) {
		if (response.status == 200) {
			console.log("Situation " + idSituation + " loaded successfuly !");
			console.log(response.data);
			//Refresh page with new data
			scope.situationText = response.data.situationName;
			scope.situationId = response.data.situationId;
			scope.choicesList = response.data.choicesList;
			scope.validatedSituation = response.data.situationValidee;
			
			if(response.data.aVote == "true"){scope.alreadyVoted = true;}
			else{scope.alreadyVoted = false;}
			
			scope.showChoicesList = true;
			scope.showSituationText = true;
			if(!scope.validatedSituation){
				scope.showRedirectMsg = true;
			}
		} else {
			scope.message = "Failed to get situation data from situation Id";
			scope.showMessage = true;
		}
	});
}

//Retrieve the situation data (id + text + choices text,id) associated with the choice ID made
function selectChoice(choice, scope, http, location) {
	let idChoice = choice.id;
	scope.lastChoiceId = idChoice;
	let situationExist = choice.situationExiste;
	
	console.log("Choice "+idChoice+" selected ! Situation exist ? : " + situationExist);
	//If choice has no situation following (is a leaf of the tree) : redirect to addSituation page
	
	//A DECOMMENTER POUR LA RELEASE
	/*if(!scope.validatedSituation){
		window.location.href = 'Servlet?mode=goTableauBord';
	}
	else */if(situationExist){//if situation associated with this choice exist : request it to server
		http.get("rest/getsituationchoix?idChoix="+idChoice+
				"&idJoueur="+scope.userId+
				"&idAventure="+scope.aventureId).then(function(response) {
		if (response.status == 200) {
			console.log("Situation loaded successfuly from choice " + idChoice);
			console.log(response.data);
			//Display new data retrieved from server
			scope.situationText = response.data.situationName;
			scope.situationId = response.data.situationId;
			scope.choicesList = response.data.choicesList;
			scope.validatedSituation = response.data.situationValidee;
			
			if(response.data.aVote == "true"){scope.alreadyVoted = true;}
			else{scope.alreadyVoted = false;}
			
			scope.showSituationText = true;
			scope.showChoicesList = true;
			if(!scope.validatedSituation){
				scope.showRedirectMsg = true;
			}
		} else {
			scope.message = "Failed to get situation Id from choice Id";
			scope.showMessage = true;
			}
		});
	}
	//Si situation non validé : redirect to tableau bord
	else{//redirect to ajoutSituation page
		console.log("Choice selected is a leaf ! Redirecting...");
		window.location.href = 'Servlet?mode=initAjoutSituation&idChoixSource=' + scope.lastChoiceId;
	}
}


function vote(action, scope, http){
	//Add disable button when clicked or already voted
	let param = scope.userId + ";" + scope.situationId;
	scope.alreadyVoted = true;
	switch(action){
		case "up":
			param = param + ";up";
			console.log("upVote clicked");
			http.post("rest/vote",param).then(function(response) {
				if (response.status != 204){
					scope.message = "failed to add a person";
					scope.showMessage = true;
					scope.alreadyVoted = false;
				}
			});
			break;
		case "down":
			param = param + ";down";
			console.log("downVote clicked");
			http.post("rest/vote",param).then(function(response) {
				if (response.status != 204){
					scope.message = "failed to add a person";
					scope.showMessage = true;
					scope.alreadyVoted = false;
				}
			});
			break;
	}
	console.log("vote() : alreadyVoted = " + scope.alreadyVoted);
}

var app = angular.module('webHerosApp', []);
app.controller('webHerosCtrl', function($scope,$http) {
	initVars($scope);
 	initView($scope);
 	console.log($scope.showRedirectMsg);
 	getSituation($scope.situationId, $scope, $http);//l'utilisateur reprend là où il en était 
    $scope.doChoice=function(idChoice) {selectChoice(idChoice,$scope,$http);}
    $scope.doVote=function(action) {vote(action, $scope, $http);}
});

	
</script>
</body>
</html>