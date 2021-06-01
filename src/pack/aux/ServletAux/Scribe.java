package pack.aux.ServletAux;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import pack.Facade;

public class Scribe {
	public static int getIdJoueurSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		int idJoueur = (int)(session.getAttribute("idJoueur"));
		return idJoueur;
	}
	public static int getIdJeuSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		int id_jeu = (int)(session.getAttribute("idJeu"));
		return id_jeu;
	}

	public static int getIdAventureSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		int id_jeu = (int)(session.getAttribute("idAventure"));
		return id_jeu;
	}
	public static void remplirTableauBordInfo(HttpServletRequest request, Facade facade)
	{
		HttpSession session = request.getSession(false);
		int id_jeu = getIdJeuSession(request);
		int idJoueur = getIdJoueurSession(request);
		session.setAttribute("infoTableauBord", facade.getInfoTableauBord(id_jeu,idJoueur));
	}
	public static void ajoutInformationsPreJeuSession(HttpServletRequest request, int idCheminement, Facade facade)
	{
		HttpSession session = request.getSession(false);
		int idAventure = facade.getIdAventureCheminement(idCheminement);
		session.setAttribute("idAventure", idAventure);
		session.setAttribute("nomAventure",facade.getAventureName(idAventure));
		session.setAttribute("idCheminement",idCheminement);
		session.setAttribute("idSituation",facade.getIdPositionCheminement(idCheminement));
	}
}
