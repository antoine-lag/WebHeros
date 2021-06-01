package pack.aux.ServletAux;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pack.Facade;

public class Creation {
	//Ajoute la situation dans la bdd(texteSituation,choixSuite[])
	public static void postSituation(HttpServletRequest request, HttpServletResponse response, Facade facade) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			int id_jeu = Scribe.getIdJeuSession(request);
			int idJoueur = Scribe.getIdJoueurSession(request);
			int idAventure = Scribe.getIdAventureSession(request);
			int idChoixSoure = Integer.parseInt(request.getParameter("idChoixSource"));
			String texteSituation = request.getParameter("texteSituation");
			System.out.println(id_jeu + ", " + 
					idJoueur + ", " + idAventure + ", " + idChoixSoure);
			List<String> textesOptions = Arrays.asList(request.getParameter("choixSuite"));
			facade.affilierSituationFille(idChoixSoure, texteSituation, textesOptions, idJoueur, idAventure);
			Routeur.renvoyerVersTableauBord(request,response,facade);
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}

	//Ajoute l'aventure dans la bdd(texteSituation,choixSuite[],nomAventure)
	public static void postAventure(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			int id_jeu = Scribe.getIdJeuSession(request);
			int idJoueur = Scribe.getIdJoueurSession(request);
			if(facade.estPremium(idJoueur)) {
				String texteSituation = request.getParameter("texteSituation");
				String nomAventure = request.getParameter("nomAventure");
				List<String> textesOptions = Arrays.asList(request.getParameter("choixSuite"));
				int av_id = facade.ajouterAventure(nomAventure, texteSituation, textesOptions, idJoueur, id_jeu);
				nouveauCheminement(request,response, av_id,facade);				
			} else {
				Routeur.renvoyerVersTableauBord(request, response,facade);
			}
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}
	//(idAventure) ou (idCheminement) ou (creationAventure)
	public static void choixAventureFait(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{

		HttpSession session = request.getSession(false);
		if (session!=null) {
			if(request.getParameter("idAventure") != null)
			{
				int id_aventure = Integer.parseInt(request.getParameter("idAventure"));
				nouveauCheminement(request,response,id_aventure,facade);
			}else if(request.getParameter("idCheminement") != null)
			{
				poursuivreCheminement(request,response,facade);
			}else if(request.getParameter("creationAventure") != null)
			{
				initAjoutAventure(request,response);
			}
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}
	public static void nouveauCheminement(HttpServletRequest request, HttpServletResponse response,int idAventure, Facade facade) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if (session!=null) {
			int ch_id = facade.nouveauCheminement(Scribe.getIdJoueurSession(request), idAventure);
			Scribe.ajoutInformationsPreJeuSession(request,ch_id,facade);
			Routeur.renvoiAAventure(request,response);
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}
	//(idCheminement)
	public static void poursuivreCheminement(HttpServletRequest request, HttpServletResponse response, Facade facade) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if (session!=null) {
			int idCheminement = Integer.parseInt(request.getParameter("idCheminement"));
			Scribe.ajoutInformationsPreJeuSession(request,idCheminement,facade);
			
			Routeur.renvoiAAventure(request,response);
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}

	//Get pour obtenir la page d'ajout de situation(idChoix)
	public static void initAjoutSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			Routeur.renvoiAAjoutSituation(request,response);
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}
	//Get pour obtenir la page d'ajout d Aventure
	public static void initAjoutAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if(session != null) {
			Routeur.renvoiAAjoutAventure(request,response);
		} else {
			Routeur.renvoiALaConnexion(request,response);
		}
	}
}
