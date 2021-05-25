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
    	bob = facade.ajouterUtilisateur("Bob", "boby@neutronMail.com", id_jeu, "mdpbob");
    	tom = facade.ajouterUtilisateur("Tom", "tomi@quarkMail.com", id_jeu, "tomi");
    	pierre = facade.ajouterUtilisateur("Pierre", "boby@electronMail.com", id_jeu, "pauljack");
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
		else if (request.getParameter("mode").equals("aventure")) {
			request.getRequestDispatcher("Aventure.html").forward(request, response);
		} else if (request.getParameter("mode").equals("accueil")) {
			HttpSession session = request.getSession(false);
			if (session!=null) {
				session.setAttribute("idAventure", Integer.parseInt(request.getParameter("aventure")));
				RequestDispatcher disp = request.getRequestDispatcher("Aventure.html");
				disp.forward(request, response);
			} else {
				RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
				disp.forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("mode").equals("connexion")) {
			// tentative de connexion
			String pseudo = request.getParameter("pseudo");
			String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
			String vrai_mdp = facade.getMdp(pseudo, id_jeu);
			if (vrai_mdp.equals(mdp)) {
				// identification reussie
				// creer une session
				HttpSession session = request.getSession(true);
				session.setAttribute("idJoueur", facade.getIDJoueur(pseudo, id_jeu));
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
		} else if (request.getParameter("mode").equals("inscription")) {
			// tentative inscription
			String pseudo = request.getParameter("pseudo");
			String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
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
					session.setAttribute("idJoueur", facade.getIDJoueur(pseudo, id_jeu));
					RequestDispatcher disp = request.getRequestDispatcher("tableau_de_bord.jsp");
					disp.forward(request, response);
				}
			}
		}else if (request.getParameter("mode").equals("ajoutSituation")) {
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
}
