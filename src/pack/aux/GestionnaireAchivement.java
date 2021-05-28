package pack.aux;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import pack.data.Accomplissement;
import pack.data.StatistiqueUtilisateur;
import pack.data.Utilisateur;

public class GestionnaireAchivement {
	public static enum TypeAction{VOTE,CREATION,VISITE,MODERATION, META};
	public static String getRecompense(EntityManager em, int id_stats, TypeAction contexte)
	{
		StatistiqueUtilisateur su = em.find(StatistiqueUtilisateur.class, id_stats);
		switch(contexte)
		{
			case VOTE:
				if(su.getNbVotes()==1)
				{
					return "Citoyen : votez une fois";
				}else if(su.getNbVotes()==10)
				{
					return "Moderateur : votez 10 fois";
				}else if(su.getNbVotes()==100)
				{
					return "Politicien : votez 100 fois";
				}
				if(su.getNbVotesNegatifs()==1)
				{
					return "Critique : votez négativement une fois";
				}else if(su.getNbVotesNegatifs()==10)
				{
					return "Chasseur : votez négativement 10 fois";
				}else if(su.getNbVotesNegatifs()==100)
				{
					return "Misanthrope : votez négativement 100 fois";
				}
				if(su.getNbVotesPositifs()==1)
				{
					return "Observateur : votez positivement une fois";
				}else if(su.getNbVotesPositifs()==10)
				{
					return "Amateur : votez positivement 10 fois";
				}else if(su.getNbVotesPositifs()==100)
				{
					return "Admirateur : votez positivement 100 fois";
				}
			case CREATION:
				if(su.getNbSituationsCrees()==1)
				{
					return "Inventeur : Creez une situation";
				}else if(su.getNbSituationsCrees()==10)
				{
					return "Ecrivain : Creez 10 situations";
				}else if(su.getNbSituationsCrees()==100)
				{
					return "Artiste : Creez 100 situations";
				}
			case VISITE:
				if(su.getNbSituationsVisitees() ==1)
				{
					return "Timide : visitez une situation";
				}
				else if(su.getNbSituationsVisitees() ==10)
				{
					return "Explorateur : visitez 10 situations";
				}
				else if(su.getNbSituationsVisitees() ==100)
				{
					return "Aventurier : visitez 100 situations";
				}
				if(su.getNbAventuresCommencees() ==1)
				{
					return "Nouveau : commencez une aventure";
				}
				else if(su.getNbAventuresCommencees() ==10)
				{
					return "Curieux : commencez 10 aventures";
				}else if(su.getNbAventuresCommencees() ==10)
				{
					return "Voyageur : commencez 100 aventures";
				}
			case MODERATION:
				if(su.getNbVotesRecus()==1)
				{
					return "Confidentiel : recevez un vote";
				}
				else if(su.getNbVotesRecus()==10)
				{
					return "Celebrite : recevez 10 votes";
				}
				else if(su.getNbVotesRecus()==100)
				{
					return "Superstar : recevez 100 votes";
				}
				if(su.getNbSituationsCreeesValidees()==1)
				{
					return "Valide : une des vos situations est valide";
				}
				else if(su.getNbSituationsCreeesValidees()==10)
				{
					return "Fiable : 10 de vos situations sont valides";
				}
				else if(su.getNbSituationsCreeesValidees()==100)
				{
					return "Equipier : 100 de vos situations sont valides";
				}
			case META:
				if(su.getNbAccomplissementsRecus()==1)
				{
					return "Bravo : recevez un accomplissement";
				}else if(su.getNbAccomplissementsRecus()==5)
				{
					return "Mention : recevez 5 accomplissements";
				}
				else if(su.getNbAccomplissementsRecus()==10)
				{
					return "Accompli : recevez 10 accomplissements";
				}	
		}
		return "";
	}
	
	//Donne une recompense a un utilisateur
	public static void distribuerRecompense(EntityManager em, int id_utilisateur, String type)
	{
		
		Utilisateur utilisateur = em.find(Utilisateur.class, id_utilisateur);
		
		if(!utilisateur.getAccomplissements().stream().anyMatch(a->a.getNom().equals(type)))
		{
			Accomplissement accomplissement = new Accomplissement();
			em.persist(accomplissement);
			accomplissement.setNom(type);
			accomplissement.setTitulaire(utilisateur);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date();
			accomplissement.setDate(formatter.format(date));
			utilisateur.getStatistiques().setNbAccomplissementsRecus(utilisateur.getStatistiques().getNbAccomplissementsRecus()+1);
			testRecompense(em,id_utilisateur,TypeAction.META);
		}
		
	}
	
	public static void testRecompense(EntityManager em, int id_utilisateur, TypeAction contexte)
	{
		Utilisateur u = em.find(Utilisateur.class, id_utilisateur);
		StatistiqueUtilisateur su = u.getStatistiques();
		String rec = getRecompense(em,su.getId(),contexte);
		if(!rec.equals(""))
		{
			distribuerRecompense(em,id_utilisateur,rec);
		}
	}

}
