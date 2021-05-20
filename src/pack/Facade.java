package pack;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pack.data.Aventure;

import pack.data.Choix;
import pack.data.Moderation;
import pack.data.Situation;
import pack.data.Utilisateur;

@Singleton
public class Facade {
	@PersistenceContext
	private EntityManager em;
	
	//Ajoute une situation et sa modération vide, ses options de choix ,puis ajoute cette situation a l'aventure
	public Situation ajouterSituation(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		
		Utilisateur utilisateur = em.find(Utilisateur.class, id_utilisateur);
		Moderation moderation = new Moderation();
		moderation.setCreateur(utilisateur);
		moderation.setValidee(false);
		em.persist(moderation);
		Situation situation = new Situation();
		situation.setModeration(moderation);
		situation.setTexte(texte);
		em.persist(situation);
		
		for(String txtChoix : textesChoix)
		{
			Choix choix = ajouterChoix(txtChoix);
			situation.getChoix().add(choix);
		}

		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.getSituations().add(situation);
		
		return situation;
		
	}
	//Ajoute un choix ne menant a rien dans la bdd
	public Choix ajouterChoix(String texteChoix)
	{
		Choix choix = new Choix();
		choix.setString_texte(texteChoix);
		em.persist(choix);
		return choix;
	}
	//affilier une nouvelle situation comme découlant d'un choix
	public void affilierSituationFille(int id_choix,String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure);
		Choix source = em.find(Choix.class, id_choix);
		source.setSituation(nouvelle);
	}
	//affilier une nouvelle situation comme etant la premiere d'une aventure
	public void affilierSituationInitiale(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure);
		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.setDebut(nouvelle);
	}
	//Indique si un choix mene vers une situation qui n'existe pas encore
	public boolean situationDoitEtreCree(int id_choix)
	{
		Choix source = em.find(Choix.class, id_choix);
		return source.getSituation() != null;
	}
	
}
