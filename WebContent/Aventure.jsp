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
		<a href="index.html">Accueil</a>
		<h1>Vous naviguez dans l'aventure {{aventureName}}</h1>
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
			<button ng-click="doVote('up')">Up Vote</button>
			<button ng-click="doVote('down')">Down vote</button>
		</div>
		<br>
		
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
//


function initVars(scope) {
	scope.message = "___";
	scope.aventureName = "<%= (String) request.getSession(false).getAttribute("nomAventure") %>"
	scope.situationText = "";
	scope.situationId = "1";
	scope.aventureId = "<%= String.valueOf(request.getSession(false).getAttribute("idAventure")) %>";
	scope.userId = "<%= String.valueOf(request.getSession(false).getAttribute("idJoueur")) %>";
	//scope.cheminementId = "<%= String.valueOf(request.getSession(false).getAttribute("idCheminement")) %>";
	scope.choicesList = [];
	scope.lastChoiceId = "1";
}
function initView(scope) {
	scope.showSituationText = false;
	scope.showChoicesList = true;
	scope.showVoteButtons = true;
	scope.showAddSituation = false;
	scope.showMessage = false;
}

//Retrieve the situation data (id+text+choices) determined by the situation's ID
//OK
function getSituation(idSituation, scope, http){
	http.get("rest/getsituation?idSituation="+idSituation).then(function(response) {
		if (response.status == 200) {
			console.log("Situation " + idSituation + " loaded successfuly !");
			console.log(response.data);
			//Refresh page with new data
			scope.situationText = response.data.situationName;
			scope.showSituationText = true;
			scope.situationId = response.data.situationId;
			scope.choicesList = response.data.choicesList;
			scope.showChoicesList = true;
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
	
	if(situationExist){//if situation associated with this choice exist : request it to server
		//DEBUG HERE
		http.get("rest/getsituationchoix?idChoix="+idChoice+
				"&idJoueur="+scope.userId+
				"&idAventure="+scope.aventureId).then(function(response) {
		if (response.status == 200) {
			console.log("Situation loaded successfuly from choice " + idChoice);
			console.log(response.data);
			//Display new data retrieved from server
			scope.situationText = response.data.text;
			scope.showSituationText = true;
			scope.situationId = response.data.situationId;
			scope.choicesList = response.data.choicesList;
			scope.showChoicesList = true;
		} else {
			scope.message = "Failed to get situation Id from choice Id";
			scope.showMessage = true;
			}
		});
	}else{//redirect to ajoutSituation page
		console.log("Choice selected is a leaf ! Redirecting...");
		//window.location.href = 'Servlet?mode=initAjoutSituation&idChoix=' + scope.lastChoiceId;
	}
}


function vote(action, scope, http){
	//Add disable button when clicked or already voted
	switch(action){
		case "up":
			console.log("upVote clicked");
			//DEBUG HERE ALSO
			http.post("rest/vote",scope.userId, scope.situationId, "up").then(function(response) {
				if (response.status == 204) scope.message = "person was added";
				else scope.message = "failed to add a person";
				scope.showMessage = true;
			});
			break;
		case "down":
			console.log("downVote clicked");
			http.post("rest/vote",scope.userId, scope.situationId, "down").then(function(response) {
				if (response.status == 204) scope.message = "person was added";
				else scope.message = "failed to add a person";
				scope.showMessage = true;
			});
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