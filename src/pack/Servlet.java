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
import pack.data.Situation;
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
		System.out.println("\nID aventure: "+aventureChateauHante);
		System.out.println("\nServlet initialised !");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("mode").equals("initAjoutSituation")) {
			initAjoutSituation(request,response);
		}else if (request.getParameter("mode").equals("accueil")) {
			choixAventureFait(request,response);
		}
		else if (request.getParameter("mode").equals("goPayerPremium")) {
			renvoiAPremium(request,response);
		}
		//CECI EST UN TEST
		else if(request.getParameter("mode").equals("aventure")) {
			request.getSession(true).setAttribute("nomAventure", "Crash test !!");
			request.getSession(false).setAttribute("idAventure", 1);
			request.getSession(false).setAttribute("idJoueur", 1);
			request.getSession(false).setAttribute("idCheminement", 1);
			RequestDispatcher disp = request.getRequestDispatcher("Aventure.jsp");
			disp.forward(request, response);
		}
	}

	public void choixAventureFait(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession session = request.getSession(false);
		if (session!=null) {
			if(request.getParameter("idAventure") != null)
			{
				nouveauCheminement(request,response);
			}else if(request.getParameter("idCheminement") != null)
			{
				poursuivreCheminement(request,response);
			}else if(request.getParameter("creationAventure") != null)
			{
				initAjoutAventure(request,response);
			}
		} else {
			renvoiALaConnexion(request,response);
		}
	}
	public void nouveauCheminement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if (session!=null) {
			int idAventure = (int) session.getAttribute("idAventure");
			int ch_id = facade.nouveauCheminement((int)session.getAttribute("idJoueur"), idAventure);
			session.setAttribute("idAventure", idAventure);
			session.setAttribute("nomAventure",facade.getAventureName(idAventure));
			session.setAttribute("idCheminement",ch_id);
			renvoiAAventure(request,response);
		} else {
			renvoiALaConnexion(request,response);
		}
	}
	public void poursuivreCheminement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if (session!=null) {
			int idCheminement = Integer.parseInt(request.getParameter("idCheminement"));

			session.setAttribute("idAventure", facade.getIdAventureCheminement(idCheminement));
			session.setAttribute("nomAventure",facade.getNomAventureCheminement(idCheminement));
			session.setAttribute("idCheminement",idCheminement);
			renvoiAAventure(request,response);
		} else {
			renvoiALaConnexion(request,response);
		}
	}

	//Get pour obtenir la page d'ajout de situation
	public void initAjoutSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
		int idChoix = Integer.parseInt(request.getParameter("idChoix"));
		session.setAttribute("idChoixSource", idChoix);
		renvoiAAjoutSituation(request,response);
		} else {
			renvoiALaConnexion(request,response);
		}
	}
	//Get pour obtenir la page d'ajout d Aventure
	public void initAjoutAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			renvoiAAjoutAventure(request,response);
		} else {
			renvoiALaConnexion(request,response);
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
		}else if (request.getParameter("mode").equals("ajoutAventure")) {
			postAventure(request,response);
		}else if (request.getParameter("mode").equals("payerPremium")) {
			postPremium(request,response);
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
			connexionOuInsriptionReussie(request,response,id_jeu,pseudo);
		} else if(vrai_mdp.equals("")) {
			// pseudo existe pas
			renvoiALaConnexion(request,response);
		} else {
			// mot de passe mauvais
			renvoiALaConnexion(request,response);
		}
	}
	//Inscrit le joueur dans la bdd
	public void inscription(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// tentative inscription
		String pseudo = request.getParameter("pseudo");
		String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
		String confirmation = facade.hasher(pseudo + request.getParameter("confirmation"));
		String email = request.getParameter("email");
		if (!mdp.equals(confirmation)) {
			// les deux mots de passes sont differents
			renvoiALInscription(request,response);
		} else {
			if (facade.ajouterUtilisateur(pseudo, email, id_jeu, mdp) == null) {
				// le pseudo existe deja
				renvoiALInscription(request,response);
			} else {
				// inscription reussie
				// creer une session
				connexionOuInsriptionReussie(request,response,id_jeu,pseudo);
			}
		}
	}
	//Ajoute la situation dans la bdd
	public void postSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			int id_jeu = (int)(session.getAttribute("idJeu"));
			int idJoueur = (int)(session.getAttribute("idJoueur"));
			int idAventure = (int)(session.getAttribute("idAventure"));
			int idChoixSoure = (int)(session.getAttribute("idChoixSource"));
			
			String texteSituation = request.getParameter("texteSituation");
		
			List<String> textesOptions = Arrays.asList(request.getParameter("choixSuite"));
			facade.affilierSituationFille(idChoixSoure, texteSituation, textesOptions, idJoueur, idAventure);
	
			renvoyerVersTableauBord(request,response, id_jeu);
		} else {
			renvoiALaConnexion(request,response);
		}
	}
	public void postAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			int id_jeu = (int)(session.getAttribute("idJeu"));
			int idJoueur = (int)(session.getAttribute("idJoueur"));
			if(facade.estPremium(idJoueur)) {
				String texteSituation = request.getParameter("texteSituation");
				String nomAventure = request.getParameter("nomAventure");
			
				List<String> textesOptions = Arrays.asList(request.getParameter("choixSuite"));
				int av_id = facade.ajouterAventure(nomAventure, texteSituation, textesOptions, idJoueur, id_jeu);
				session.setAttribute("idAventure", av_id);
				nouveauCheminement(request,response);				
			} else {
				renvoyerVersTableauBord(request, response, id_jeu);
			}
		} else {
			renvoiALaConnexion(request,response);
		}
	}
	public void postPremium(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			int id_jeu = (int)(session.getAttribute("idJeu"));
			int idJoueur = (int)(session.getAttribute("idJoueur"));
			if(!facade.estPremium(idJoueur)) {
				String certif = request.getParameter("certificat");
				if(certif.equals("vywLyucHFnNecn49"))
				{
					facade.setPremium(idJoueur);
					session.setAttribute("premium", facade.estPremium(idJoueur));
				}
			}
			renvoyerVersTableauBord(request,response,id_jeu);
		} else {
			renvoiALaConnexion(request,response);
		}
	}
	//Renvoie vers le tableau de bord
	public void renvoyerVersTableauBord(HttpServletRequest request, HttpServletResponse response, int id_jeu) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		int idJoueur = (int)session.getAttribute("idJoueur" );
		session.setAttribute("infoTableauBord", facade.getInfoTableauBord(id_jeu,idJoueur));
		RequestDispatcher disp = request.getRequestDispatcher("accueil.jsp");
		disp.forward(request, response);
	}
	public void connexionOuInsriptionReussie(HttpServletRequest request, HttpServletResponse response, int id_jeu, String pseudo) throws ServletException, IOException
	{
		HttpSession session = request.getSession(true);
		
		int idJ = facade.getIDJoueur(pseudo, id_jeu);
		session.setAttribute("idJoueur", idJ);
		session.setAttribute("pseudoJoueur", pseudo);
		session.setAttribute("premium", facade.estPremium(idJ));
		session.setAttribute("idJeu", id_jeu);
		renvoyerVersTableauBord(request,response, id_jeu);
	}
	
	public void renvoiALaConnexion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
		disp.forward(request, response);
	}
	public void renvoiALInscription(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("inscription.html");
		disp.forward(request, response);
	}
	public void renvoiAAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("Aventure.jsp");
		disp.forward(request, response);
	}
	public void renvoiAAjoutAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("AjoutAventure.html");
		disp.forward(request, response);
	}
	public void renvoiAAjoutSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("AjoutSituation.html");
		disp.forward(request, response);
	}
	public void renvoiAPremium(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("payerPremium.html");
		disp.forward(request, response);
	}
}
