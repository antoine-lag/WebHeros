package pack.aux;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import pack.Facade;
import pack.data.Accomplissement;
import pack.data.Aventure;
import pack.data.Cheminement;
import pack.data.Choix;
import pack.data.Jeu;
import pack.data.Situation;
import pack.data.Utilisateur;

public class Agregateur {
	public static InfoTableauBord getInfoTableauBord(int idJeu, int idJoueur,EntityManager em)
	{
		List<String> nomsAventures = new LinkedList<String>();
		List<Integer> idsAventures = new LinkedList<Integer>();
		List<String> textesCheminements = new LinkedList<String>();
		List<Integer> idsCheminements = new LinkedList<Integer>();
		List<String> textesAccomplissements = new LinkedList<String>();
		List<String> datesAccomplissements = new LinkedList<String>();
		List<String> textesCompletsCheminements= new LinkedList<String>();
		List<Boolean> isActiveCheminements= new LinkedList<Boolean>();
		Jeu jeu = (Jeu)em.find(Jeu.class, idJeu);
		Utilisateur utilisateur = (Utilisateur)em.find(Utilisateur.class, idJoueur);
		for(Aventure av : jeu.getAventure())
		{
			nomsAventures.add(av.getNom());
			idsAventures.add(av.getId());
		}
		for(Cheminement ch : utilisateur.getCheminements())
		{
			textesCheminements.add(GestionnaireCheminement.getTexteCheminement(em, ch.getId()));
			textesCompletsCheminements.add(GestionnaireCheminement.getTexteCompletCheminement(em, ch.getId()));
			isActiveCheminements.add(ch.isActif());
			idsCheminements.add(ch.getId());
		}
		for(Accomplissement ac : utilisateur.getAccomplissements())
		{
			textesAccomplissements.add(ac.getNom());
			datesAccomplissements.add(ac.getDate());
		}
		InfoTableauBord tableau = new InfoTableauBord();
		tableau.setIdsAventures(idsAventures);
		tableau.setNomsAventures(nomsAventures);
		tableau.setIdsCheminements(idsCheminements);
		tableau.setTextesCheminements(textesCheminements);
		tableau.setDatesAccomplissements(datesAccomplissements);
		tableau.setTextesAccomplissements(textesAccomplissements);
		tableau.setStats(utilisateur.getStatistiques());
		tableau.setPremium(utilisateur.getPremium());
		tableau.setIsActiveCheminements(isActiveCheminements);
		return tableau;
	}
	public static String getJSONSituation(int idSituation,
			 int idJoueur,EntityManager em) {

		Situation s = em.find(Situation.class, idSituation);
		String situtationName;
		List<Choix>choicesList = new ArrayList<Choix>();
		//Getting situation name
		situtationName =  s.getTexte();
		
		//Retrieving choices list
		choicesList = new ArrayList<>(s.getChoix());
		JsonObject envoi = new JsonObject();
		envoi.put("situationName", situtationName);
		envoi.put("situationId", s.getId());
		envoi.put("aVote", Assesseur.aVote(idJoueur,s.getModeration()));
		envoi.put("situationValidee", s.getModeration().getValidee());
		JsonArray JchoicesList = new JsonArray();
		for(Choix c:choicesList)
		{
			JsonObject ch = new JsonObject();
			ch.put("text", c.getString_texte());
			ch.put("id",c.getId());
			ch.put("situationExiste",!situationDoitEtreCree(c.getId(),em));
			JchoicesList.add(ch);
		}
		envoi.put("choicesList", JchoicesList);
		String sJsonData = envoi.toJson();
		return sJsonData;
	}
	//Indique si un choix mene vers une situation qui n'existe pas encore
	public static boolean situationDoitEtreCree(int id_choix,EntityManager em)
	{
		Choix source = em.find(Choix.class, id_choix);
		return source.getSituation() == null;
	}
	public static String getJSONSituationChoix(int idChoix,
			int idJoueur,
			int idAventure,EntityManager em, Facade facade) {
		Choix c = em.find(Choix.class, idChoix);
		Situation s = c.getSituation();
		facade.visiter(idJoueur,s.getId(),idAventure,false);
		return getJSONSituation(s.getId(),idJoueur,em);
	}
}
