package pack.aux;

import java.util.Collection;

import javax.persistence.EntityManager;

import pack.data.Aventure;
import pack.data.Cheminement;
import pack.data.Choix;
import pack.data.Situation;
import pack.data.SituationClasse;
import pack.data.Utilisateur;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class GestionnaireCheminement {

	public static Cheminement visiter(EntityManager em, int id_joueur, int id_situation, int id_aventure)
	{
		int idChem = trouverCheminementDonnantSur(em,id_joueur,id_situation);
		Situation situation = em.find(Situation.class, id_situation);
		
		//Todo
		if(idChem == -1)
		{
			//nouveau depart
			Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
			Aventure aventure = em.find(Aventure.class, id_aventure);
			Cheminement cheminement = new Cheminement();
			em.persist(cheminement);
			cheminement.setActif(true);
			cheminement.setAventure(aventure);
			cheminement.setPosition(situation);
			utilisateur.getCheminements().add(cheminement);
			SituationClasse sc = new SituationClasse();
			sc.setOrdre(0);
			sc.setSituation(situation);
			em.persist(sc);
			cheminement.getParcours().add(sc);
			return cheminement;
			
		}else if(idChem==-2)
		{
			//Rien a faire, on est deja sur une feuille
			return null;
		}
		else {
			//Etendre le cheminement
			Cheminement cheminement = em.find(Cheminement.class, idChem);
			cheminement.setPosition(situation);
			SituationClasse sc = new SituationClasse();
			sc.setOrdre(getOrdreMax(em,idChem)+1);
			sc.setSituation(situation);
			em.persist(sc);
			cheminement.getParcours().add(sc);
			return cheminement;
		}
	}
	public static int trouverCheminementDonnantSur(EntityManager em, int id_joueur, int id_situation)
	{
		Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
		Collection<Cheminement> cheminements = utilisateur.getCheminements();
		for(Cheminement ch : cheminements)
		{
			Situation position = ch.getPosition();
			if(position.getId()==id_situation)
			{
				return -2;
			}
			Collection<Choix> choix = position.getChoix();
			if(choix.stream().anyMatch(c->c.getSituation().getId() == id_situation))
			{
				return ch.getId();
			}
		}
		return -1;
	}
	public static int getOrdreMax(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		return cheminement.getParcours().stream().max(Comparator.comparingInt(SituationClasse::getOrdre)).get().getOrdre();
	}
	
	public static int getIdSituationActuelle(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		return cheminement.getPosition().getId();
	}
	
	public static int getIdAventureActuelle(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		return cheminement.getAventure().getId();
	}
	public static String getTexteCheminement(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		String texte = cheminement.getAventure().getNom();
		String hist = cheminement.getPosition().getTexte();
		int tailleParcours = cheminement.getParcours().size();
		String resume = hist.substring(0,Math.min(hist.length()-1, 10));
		texte += " : "+resume + " ... ( " +tailleParcours+" situations visitées )" ;
		return texte;
	}
	public static String getTexteCompletCheminement(EntityManager em, int id_cheminement)
	{
		String texte = "";
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		texte += cheminement.getAventure().getNom()+"\n";
		List<SituationClasse> chem = cheminement.getParcours().stream()
				.sorted(Comparator.comparingInt(SituationClasse::getOrdre)).collect(Collectors.toList());
		for(SituationClasse s : chem)
		{
			texte += s.getSituation().getTexte()+"\n\n";
		}
		return texte;
	}
	
}
