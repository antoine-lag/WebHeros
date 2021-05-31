package pack.aux.ServletAux;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pack.Facade;
import pack.aux.SecurBack;

public class Securite {
	//(certificat)
	public static void postPremium(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			int idJoueur = Scribe.getIdJoueurSession(request);
			if(!facade.estPremium(idJoueur)) {
				String certif = request.getParameter("certificat");
				if(certif.equals("vywLyucHFnNecn49"))
				{
					facade.setPremium(idJoueur);
					session.setAttribute("premium", facade.estPremium(idJoueur));
				}
			}
			Routeur.renvoyerVersTableauBord(request,response,facade);
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}
	//Renvoie vers le tableau de bord

	public static void connexionOuInsriptionReussie(HttpServletRequest request, HttpServletResponse response, int id_jeu, String pseudo,Facade facade) throws ServletException, IOException
	{
		HttpSession session = request.getSession(true);
		
		int idJ = facade.getIDJoueur(pseudo, id_jeu);
		session.setAttribute("idJoueur", idJ);
		session.setAttribute("pseudoJoueur", pseudo);
		session.setAttribute("premium", facade.estPremium(idJ));
		session.setAttribute("idJeu", id_jeu);
		Routeur.renvoyerVersTableauBord(request,response,facade);
	}
	//Connecte le joueur (pseudo,pwd)
	public static void connexion(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{
		// tentative de connexion
		String pseudo = request.getParameter("pseudo");
		
		//Provisoire
		int id_jeu = 1;
		String mdp = SecurBack.hasher(pseudo + request.getParameter("pwd"));
		String vrai_mdp = facade.getMdp(pseudo, id_jeu);
		if (vrai_mdp.equals(mdp)) {
			// identification reussie
			// creer une session
			connexionOuInsriptionReussie(request,response,id_jeu,pseudo,facade);
		} else if(vrai_mdp.equals("")) {
			// pseudo existe pas
			Routeur.renvoiALaConnexion(request,response);
		} else {
			// mot de passe mauvais
			Routeur.renvoiALaConnexion(request,response);
		}
	}
	//Inscrit le joueur dans la bdd(pseudo,pwd,confirmation,email)
	public static void inscription(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{
		// tentative inscription
		//Provisoire
		int id_jeu = 1;
		String pseudo = request.getParameter("pseudo");
		String mdp = SecurBack.hasher(pseudo + request.getParameter("pwd"));
		String confirmation = SecurBack.hasher(pseudo + request.getParameter("confirmation"));
		String email = request.getParameter("email");
		if (!mdp.equals(confirmation)) {
			// les deux mots de passes sont differents
			Routeur.renvoiALInscription(request,response);
		} else {
			if (facade.ajouterUtilisateur(pseudo, email, id_jeu, mdp) == null) {
				// le pseudo existe deja
				Routeur.renvoiALInscription(request,response);
			} else {
				// inscription reussie
				// creer une session
				connexionOuInsriptionReussie(request,response,id_jeu,pseudo,facade);
			}
		}
	}
	public static void deconnexion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null)
		{
			session.invalidate();
		}
		Routeur.renvoiALaConnexion(request,response);
	}
}
