let compteur = 0;
let max_choix = 4;

function nouveauChoix() {
	// ajouter un choix
	var x = document.createElement("INPUT");
	x.setAttribute("name", "choixSuite");
	x.setAttribute("required", "true");
	document.getElementById("formAjout").insertBefore(x,document.getElementById("nouvSitu"));
	
	// griser le bouton
	if (compteur==max_choix-2) {
		bouton_ajout.disabled = true;
	}
	
	compteur++;
}