package pack;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pack.data.Jeu;
import pack.data.Utilisateur;
import pack.data.Aventure;
import pack.data.Cheminement;

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
	Utilisateur tom;
	Utilisateur pierre;
	Aventure aventureChateauHante;
	
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
    	bob = facade.ajouterUtilisateur("Bob", "boby@neutronMail.com", id_jeu, "d0d95333b78031a3abe404609bd9af42a2bd22e6b6ab812465710acb90b6b138");//Hash of "mdp"
    	List<String> choix = Arrays.asList("Vous sortez du chateau.", "Vous allez dans la cave.", "Vous allez dans la tour pour voir l'extérieur.");
		aventureChateauHante = facade.ajouterAventure("Creepy castle", "Tout commence dans le chateau du Duc de Normandie...", choix, bob.getId(), id_jeu);
		System.out.println("\nID aventure: "+aventureChateauHante.getId());
		System.out.println("\nServlet initialised !");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("mode").equals("initAjoutSituation")) {
			initAjoutSituation(request,response);
		}else if (request.getParameter("mode").equals("aventureJsp")) {
			request.setAttribute("userId", 1);
			request.setAttribute("aventureId", 1);
			request.setAttribute("aventureName", "Here goes the aventure name");
			request.getRequestDispatcher("Aventure.jsp").forward(request, response);
		} else if (request.getParameter("mode").equals("accueil")) {
			choixAventureFait(request,response);
		}
	}

	public void choixAventureFait(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if (session!=null) {
			if(request.getParameter("creation") != null && request.getParameter("creation").equals("nouveau"))
			{
				int idAventure = Integer.parseInt(request.getParameter("aventure"));
				Aventure av = facade.getAventure(idAventure);
				Cheminement ch = facade.visiter((int)session.getAttribute("idJoueur"), av.getDebut().getId(), av.getId());
				session.setAttribute("idAventure", idAventure);
				session.setAttribute("nomAventure",facade.getAventureName(idAventure));
				session.setAttribute("idCheminement",ch.getId());
				RequestDispatcher disp = request.getRequestDispatcher("Aventure.jsp");
			}else
			{
				int idCheminement = Integer.parseInt(request.getParameter("cheminement"));
				Cheminement ch = facade.getCheminement(idCheminement);
				session.setAttribute("idAventure", ch.getAventure().getId());
				session.setAttribute("nomAventure",ch.getAventure().getNom());
				session.setAttribute("idCheminement",ch.getId());
				RequestDispatcher disp = request.getRequestDispatcher("Aventure.jsp");
			}
			disp.forward(request, response);
		} else {
			RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
			disp.forward(request, response);
		}
	}
	//Get pour obtenir la page d'ajout de situation
	public void initAjoutSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
		int idChoix = Integer.parseInt(request.getParameter("idChoix"));
		session.setAttribute("idChoixSource", idChoix);
		RequestDispatcher disp = request.getRequestDispatcher("AjoutSituation.html");
		disp.forward(request, response);
		} else {
			RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
			disp.forward(request, response);
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("mode").equals("connexion")) {
			connexion(request,response);
		} else if (request.getParameter("mode").equals("inscription")) {
			inscription(request,response);
		}else if (request.getParameter("mode").equals("ajoutSituation")) {
			postSituation(request,response);
		}
	}
	//Connecte le joueur
	public void connexion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// tentative de connexion
		String pseudo = request.getParameter("pseudo");
		String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
		String vrai_mdp = facade.getMdp(pseudo, id_jeu);
		if (vrai_mdp.equals(mdp)) {
			// identification reussie
			// creer une session
			HttpSession session = request.getSession(true);
			int idJoueur = facade.getIDJoueur(pseudo, id_jeu);
			session.setAttribute("idJoueur", idJoueur);
			session.setAttribute("pseudoJoueur", pseudo);
			
			session.setAttribute("infoTableauBord", facade.getInfoTableauBord(id_jeu,idJoueur));
			RequestDispatcher disp = request.getRequestDispatcher("tableau_de_bord.jsp");
			disp.forward(request, response);
		} else if(vrai_mdp.equals("")) {
			// pseudo existe pas
			RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
			disp.forward(request, response);
		} else {
			// mot de passe mauvais
			RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
			disp.forward(request, response);
		}
	}
	//Inscrit le joueur dans la bdd
	public void inscription(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// tentative inscription
		String pseudo = request.getParameter("pseudo");
		String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
		System.out.println("_______MDP: " + mdp + "_______________");
		String confirmation = facade.hasher(pseudo + request.getParameter("confirmation"));
		String email = request.getParameter("email");
		if (!mdp.equals(confirmation)) {
			// les deux mots de passes sont differents
			RequestDispatcher disp = request.getRequestDispatcher("inscription.html");
			disp.forward(request, response);
		} else {
			if (facade.ajouterUtilisateur(pseudo, email, id_jeu, mdp) == null) {
				// le pseudo existe deja
				RequestDispatcher disp = request.getRequestDispatcher("inscription.html");
				disp.forward(request, response);
			} else {
				// inscription reussie
				// creer une session
				HttpSession session = request.getSession(true);
				int idJ = facade.getIDJoueur(pseudo, id_jeu);
				session.setAttribute("idJoueur", idJ);
				session.setAttribute("pseudoJoueur", pseudo);
				session.setAttribute("infoTableauBord", facade.getInfoTableauBord(id_jeu,idJ));
				RequestDispatcher disp = request.getRequestDispatcher("tableau_de_bord.jsp");
				disp.forward(request, response);
			}
		}
	}
	//Ajoute la situation dans la bdd
	public void postSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
		int idJoueur = (int)(session.getAttribute("idJoueur"));
		int idAventure = (int)(session.getAttribute("idAventure"));
		int idChoixSoure = (int)(session.getAttribute("idChoixSource"));
		String texteSituation = request.getParameter("texteSituation");
	
		List<String> textesOptions = Arrays.asList(request.getParameter("choixSuite"));
		
		facade.affilierSituationFille(idChoixSoure, texteSituation, textesOptions, idJoueur, idAventure);
		RequestDispatcher disp = request.getRequestDispatcher("tableau_de_bord.jsp");
		disp.forward(request, response);
		} else {
			RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
			disp.forward(request, response);
		}
	}
}
