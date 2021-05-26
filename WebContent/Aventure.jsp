<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pack.data.Aventure" %>
<!DOCTYPE html>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>

<head>
	<meta charset="UTF-8">
	<title>Aventure</title>
</head>
<body>
	<div ng-app="webHerosApp" ng-controller="webHerosCtrl"> 
		<a href="index.html">Accueil</a>
		<h1>Vous naviguez dans l'aventure {{aventureName}}</h1>
		
		<div ng-show="showChoicesList">
			<ul>
				<li ng-repeat="c in choicesList">
					<button id="btn1" ng-click="doChoice({{c.id}})">{{c.text}}</button>
				</li>
			</ul>
		</div>
		
		<div ng-show="showSituationText">
			<h3>{{situationText}}</h3>
		</div>
		<br>
		
		
		<br>
		
		<div ng-show="showVoteButtons">
			<button ng-click="doVote('up')">Up Vote</button>
			<button ng-click="doVote('down')">Down vote</button>
		</div>
		
		<div ng-show="showAddSituation">
			<a href="AjoutSitutation.html">Ajouter une situation</a>
		</div>
		
		
		<div ng-show="showMessage">
			{{message}}
		</div>
	</div>
	
<script>
//- get situation (Root to begin with)
//- get choices; do the function in facade & manage params
//- display 1 button for each choice
//- redirect to addSituation
//- Set up vote buttons.
//- set restriction for voting depending on the user logged in
//PROBLEMES
//PB1 - Faire un GET + POST simultané

function initVars(scope) {
	//scope.situation = new Object();//Not sure
	//scope.user = new Object();
	scope.message = "___";
	scope.aventureName = "<%= (String) request.getSession(false).getAttribute("nomAventure") %>"
	scope.situationText = "";
	scope.situationId = "1";
	scope.aventureId = "<%= String.valueOf(request.getSession(false).getAttribute("idAventure")) %>";
	scope.userId = "<%= String.valueOf(request.getSession(false).getAttribute("idJoueur")) %>";
	//scope.cheminementId = "<%= String.valueOf(request.getSession(false).getAttribute("idCheminement")) %>";
	scope.choicesList = [{"id": "1", "text": "choix1"}, {"id": "2", "text": "choix2"}];
}
function initView(scope) {
	scope.showSituationText = false;
	scope.showChoicesList = true;
	scope.showVoteButtons = true;
	scope.showAddSituation = false;
	scope.showMessage = false;
}

function getSituation(idSituation, scope, http){
	http.get("rest/getsituation?idSituation="+idSituation).then(function(response) {
		if (response.status == 200) {
			scope.situationText = response.data.text;
			scope.showSituationText = true;
			scope.choicesList = response.data.choices;
			scope.showChoicesList = true;
			//String sJsonData = "{\"text\": \"" + situtationName + "\","+"\"choices\": [{\"id\": \"1\", \"text\":\"La réponse D\"}]}";
		} else {
			scope.message = "Failed to get situation data from situation Id";
			scope.showMessage = true;
		}
	});
}

function selectChoice(idChoice, scope, http) {
	initView(scope);
	console.log("Choice = "+idChoice+" selected =)");
	//Envoyer requette http get pour modifier avoir l'id de la situation correspondante et appel getSituation
	http.get("rest/selectchoice?idChoice"+idChoice).then(function(response) {
		if (response.status == 200) {
			scope.showSituationText = false;
			scope.showChoicesList = false;
			scope.situationId = response.data.situationId;
		} else {
			scope.message = "Failed to get situation Id from choice Id";
			scope.showMessage = true;
		}
	});
}


function vote(action, scope, http){
	//Add disable button when clicked or already voted
	switch(action){
		case "up":
			console.log("upVote clicked");
			break;
		case "down":
			console.log("downVote clicked");
			break;
	}
}

var app = angular.module('webHerosApp', []);
app.controller('webHerosCtrl', function($scope,$http) {
	initVars($scope);
 	initView($scope);
 	getSituation("1", $scope, $http);
    $scope.doChoice=function(idChoice) {selectChoice(idChoice,$scope,$http);}
    $scope.doVote=function(action) {vote(action, $scope, $http);}
});
	
</script>
</body>
</html>