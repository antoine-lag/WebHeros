package pack.aux;

import java.util.Collection;

import javax.persistence.EntityManager;

import pack.data.Cheminement;
import pack.data.Choix;
import pack.data.Situation;
import pack.data.SituationClasse;
import pack.data.Utilisateur;

public class GestionnaireCheminement {

	public static void visiter(EntityManager em, int id_joueur, int id_situation)
	{
		int idChem = trouverCheminementDonnantSur(em,id_joueur,id_situation);
		SituationClasse sc = new SituationClasse();
		em.persist(sc);
		//Todo
		if(idChem != -1)
		{
			Cheminement cheminement = em.find(Cheminement.class, id_joueur);
			
		}else
		{
			Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
			Cheminement cheminement = new Cheminement();
			em.persist(cheminement);
			utilisateur.getCheminements().add(cheminement);
		}
	}
	public static int trouverCheminementDonnantSur(EntityManager em, int id_joueur, int id_situation)
	{
		Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
		Collection<Cheminement> cheminements = utilisateur.getCheminements();
		for(Cheminement ch : cheminements)
		{
			Situation position = ch.getPosition();
			Collection<Choix> choix = position.getChoix();
			if(choix.stream().anyMatch(c->c.getSituation().getId() == id_situation))
			{
				return ch.getId();
			}
		}
		return -1;
	}
}
