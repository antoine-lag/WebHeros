package pack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

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

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	Facade facade;
	
	//Temporairement on utilise un seul joueur
	int id_jeu;

    /**
     * Default constructor. 
     */
    public Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if(request.getParameter("mode").equals("init")) {
			Jeu jeu= facade.initJeu();
			id_jeu = jeu.getId();
			String stringSetup = "Setup done !";
			response.getWriter().println("<html><body>"+stringSetup+"</body></html>");
		} else if (request.getParameter("mode").equals("ajoutSituation")) {
			HttpSession session = request.getSession(false);
			int idJoueur = (int)(session.getAttribute("idJoueur"));
			int idAventure = (int)(session.getAttribute("idAventure"));
			int idChoixSoure = (int)(session.getAttribute("idChoixSource"));
			String texteSituation = request.getParameter("texteSituation");
		
			List<String> textesOptions = Arrays.asList(request.getParameter("choixSuite"));
			
			facade.affilierSituationFille(idChoixSoure, texteSituation, textesOptions, idJoueur, idAventure);
			String stringSetup = "Situation ajoutee";
			response.getWriter().println("<html><body>"+stringSetup+"</body></html>");
		} else if (request.getParameter("mode").equals("test")) {
			HttpSession session = request.getSession(false);
			int idJoueur = (int)(session.getAttribute("idJoueur"));
			int idAventure = (int)(session.getAttribute("idAventure"));
			response.getWriter().println("<html><body>"+"id_joueur = "+idJoueur+"\nidAventure = "+idAventure+"</body></html>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("mode").equals("connexion")) {
			String pseudo = request.getParameter("pseudo");
			String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
			String vrai_mdp = facade.getMdp(pseudo, id_jeu);
			if (vrai_mdp.equals(mdp)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("idJoueur", facade.getIDJoueur(pseudo, id_jeu));
				session.setAttribute("idAventure", id_jeu);
				response.getWriter().println("<html><body>"+"Connexion reussie"+"</body></html>");
			} else {
				RequestDispatcher disp = request.getRequestDispatcher("connexion.html");
				disp.forward(request, response);
			}
		} else if (request.getParameter("mode").equals("inscription")) {
			String pseudo = request.getParameter("pseudo");
			String mdp = facade.hasher(pseudo + request.getParameter("pwd"));
			String confirmation = facade.hasher(pseudo + request.getParameter("confirmation"));
			String email = request.getParameter("email");
			if (!mdp.equals(confirmation)) {
				RequestDispatcher disp = request.getRequestDispatcher("inscription.html");
				disp.forward(request, response);
			} else {
				if (facade.ajouterUtilisateur(pseudo, email, id_jeu, mdp) == null) {
					RequestDispatcher disp = request.getRequestDispatcher("inscription.html");
					disp.forward(request, response);
				} else {
					HttpSession session = request.getSession(true);
					session.setAttribute("idJoueur", facade.getIDJoueur(pseudo, id_jeu));
					session.setAttribute("idAventure", id_jeu);
					response.getWriter().println("<html><body>"+"Inscription et Connexion reussie"+"</body></html>");
				}
			}
		}
	}
}
