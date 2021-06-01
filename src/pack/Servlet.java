package pack;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import pack.data.Jeu;

import pack.data.Utilisateur;
import pack.aux.ServletAux.Creation;
import pack.aux.ServletAux.Routeur;
import pack.aux.ServletAux.Securite;


/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	Facade facade;
	
	Jeu jeuPrincipal;
	Utilisateur bob;
	int aventureChateauHante;
	
	//Temporairement on utilise un seul jeu
	int id_jeu;

    /**
     * Default constructor. 
     */
    public Servlet() {
    	
    }
    
    public void init() throws ServletException{
    	/*INITIALISATION
    	 * Un seul jeu. 3 utilisateurs (nom, mdp): (Bob, mdpbob) (Tom, tomi) (Pierre, pauljack).
    	 * Une aventure: chateau hanté
    	 * Une situation initiale: "Tout commence dans ..." + 3 choix
    	 */
    	jeuPrincipal = facade.initJeu();
    	id_jeu = jeuPrincipal.getId();
    	bob = facade.ajouterUtilisateur("boby", "boby@neutronMail.com", id_jeu, "3500c7baadd59b7db297fc1328d3b9a3a3606198a53e966deba7b91b761fd22d");//Hash of "boby" + "mdp"
    	List<String> choix = Arrays.asList("Vous sortez du chateau.", "Vous allez dans la cave.", "Vous allez dans la tour pour voir l'extérieur.");
		aventureChateauHante = facade.ajouterAventure("Creepy castle", "Tout commence dans le chateau du Duc de Normandie...", choix, bob.getId(), id_jeu);
		facade.affilierSituationFille(1, "Après être sorti du chateau vous vous rendez compte que vous avez oublié votre lampe torche dans la chambre du 5 ème étage.", 
				Arrays.asList("Vous re rentrez dans le chateau.", "Vous continuez sans lampe torche.", "Vous tapez une sieste."),
				bob.getId(), aventureChateauHante);
		facade.forcerValidee(facade.getAventure(aventureChateauHante).getId());
		
		System.out.println("\nID aventure: "+aventureChateauHante);
		System.out.println("\nServlet initialised !");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mode")==null)
		{
			Routeur.renvoyerVersTableauBord(request, response, facade);
		}
		if (request.getParameter("mode").equals("initAjoutSituation")) {
			//get parameter idChoix
			Creation.initAjoutSituation(request,response);
		}else if (request.getParameter("mode").equals("accueil")) {
			Creation.choixAventureFait(request,response,facade);
		}else if (request.getParameter("mode").equals("deco")) {
			Securite.deconnexion(request,response);
		}
		else if (request.getParameter("mode").equals("goPayerPremium")) {
			Routeur.renvoiAPremium(request,response);
		}else if (request.getParameter("mode").equals("goTableauBord")) {
			Routeur.renvoyerVersTableauBord(request,response,facade);
		}
		else{
			Routeur.renvoyerVersTableauBord(request, response, facade);
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mode")==null)
		{
			Routeur.renvoyerVersTableauBord(request, response, facade);
		}
		if (request.getParameter("mode").equals("connexion")) {
			Securite.connexion(request,response,facade);
		} else if (request.getParameter("mode").equals("inscription")) {
			Securite.inscription(request,response,facade);
		}else if (request.getParameter("mode").equals("ajoutSituation")) {
			Creation.postSituation(request,response,facade);
		}else if (request.getParameter("mode").equals("ajoutAventure")) {
			Creation.postAventure(request,response,facade);
		}else if (request.getParameter("mode").equals("payerPremium")) {
			Securite.postPremium(request,response,facade);
		}else if (request.getParameter("mode").equals("afficheurHistoire")) {
			Routeur.renvoiAAfficheurHistoire(request,response);
		}else{
			Routeur.renvoyerVersTableauBord(request, response, facade);
		}
	}
}
