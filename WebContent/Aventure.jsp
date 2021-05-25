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
		<h1>Vous naviguez dans l'aventure {{aventureName}}</h1>
		
		<div ng-show="showSituationText">
			<h3>{{situationText}}</h3>
		</div>
		<br>
		
		</div ng-show="showChoicesList">
			<ul>
				<li ng-repeat="c in choicesList">
					<button ng-click="selectChoice('c.id')">{{c.text}}</button>
				</li>
			</ul>
		</div>
		<br>
		
		<div ng-show="showVoteButtons">
			<button ng-click="doVote('up')">Up Vote</button>
			<button ng-click="doVote('down')">Down vote</button>
		</div>
		
		<div ng-show="showAddSituation">
			<a href="AjoutSitutation.html">Ajouter une situation</a>
		</div>
		
		
		
		<a href="index.html">Accueil</a>
		
		<div ng-show="showMessage">
			{{message}}
		</div>
	</div>
	
<script>
//1 - get user from server with user ID taken from ??
//2 - get situation (Root to begin with)
//3 - display 1 button for each choice
//4 - redirect to addSituation
//5 - Set up vote buttons.
//6 - set restriction for voting depending on the user logged in
//PROBLEMES
//PB1 - les show à false s'affichent quand même

function initVars(scope) {
	//scope.situation = new Object();//Not sure
	//scope.user = new Object();
	scope.message = "___";
	scope.aventureName = "<%= (String) request.getAttribute("aventureName") %>"
	scope.aventureId = "<%= (String) request.getAttribute("aventureId") %>"
	scope.situationText = "";
	scope.userId = "<%= (String) request.getAttribute("userId") %>"
	
}
function initView(scope) {
	scope.showSituationText = false;
	scope.showChoicesList = false;
	scope.showVoteButtons = true;
	scope.showAddSituation = false;
	scope.showMessage = false;
}
function initAventure(scope, http){
	initView(scope);
	http.get("rest/getAventureName").then(function(response) {
		if (response.status == 200) {
			scope.aventureName = response.data.aventureName;
		} else {
			scope.message = "Failed to get aventure name";
			scope.showMessage = true;
		}
	});
}


function initSituation(scope, http){
	initView(scope);
	http.get("rest/getSituation", scope.aventureName).then(function(response) {
		if (response.status == 200) {
			scope.situation = response.data.situation;
		} else {
			scope.message = "Failed to get situation";
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

function selectChoice(idChoice, scope, http) {
	initView(scope);
	initVars(scope);
	console.log("Choice "+idChoice+" selected");
	//Envoyer requette http get pour modifier la situation et l'affichage
}


var app = angular.module('webHerosApp', []);
app.controller('webHerosCtrl', function($scope,$http) {
	initVars($scope);
 	initView($scope);
    $scope.doChoice=function(idChoice) {selectChoice(idChoice,$scope,$http);}
    $scope.doVote=function(action) {vote(action, $scope, $http);}
});
	
</script>
</body>
</html>