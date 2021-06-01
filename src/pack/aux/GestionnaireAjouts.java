package pack.aux;

import java.util.List;

import javax.persistence.EntityManager;

import pack.Facade;
import pack.data.Aventure;
import pack.data.Choix;
import pack.data.Jeu;
import pack.data.Moderation;
import pack.data.Situation;
import pack.data.Utilisateur;

public class GestionnaireAjouts {
	//Initialise une instance de jeu
	public static Jeu ajoutJeu(EntityManager em)
	{
		Jeu jeu = new Jeu();
		em.persist(jeu);
		return jeu;
	}
	//Ajoute une situation et sa modération vide, ses options de choix ,puis ajoute cette situation a l'aventure
	public static Situation ajouterSituation(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure,EntityManager em, Facade facade)
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
			
			Choix choix = ajouterChoix(txtChoix,em);
			situation.getChoix().add(choix);
			em.merge(choix);
		}

		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.getSituations().add(situation);
		em.merge(situation);
		utilisateur.getStatistiques().setNbSituationsCrees(utilisateur.getStatistiques().getNbSituationsCrees()+1);
		return situation;
		
	}
	//Ajoute un choix ne menant a rien dans la bdd
	public static Choix ajouterChoix(String texteChoix,EntityManager em)
	{
		Choix choix = new Choix();
		choix.setString_texte(texteChoix);
		em.persist(choix);
		return choix;
	}
	//affilier une nouvelle situation comme découlant d'un choix
	public static Situation affilierSituationFille(int id_choix,String texte,List<String> textesChoix, int id_utilisateur,int id_aventure,EntityManager em, Facade facade)
	{
		Choix source = em.find(Choix.class, id_choix);
		if(source.getSituation() == null)
		{
			Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure,em,facade);
			source.setSituation(nouvelle);
			facade.visiter(id_utilisateur, nouvelle.getId(), id_aventure,true);
			return nouvelle;
		}
		return null;
	}
	//affilier une nouvelle situation comme etant la premiere d'une aventure
	public static Situation affilierSituationInitiale(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure,EntityManager em,Facade facade)
	{
		Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure,em,facade);
		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.setDebut(nouvelle);
		facade.visiter(id_utilisateur, nouvelle.getId(), id_aventure,false);
		em.merge(nouvelle);
		
		return nouvelle;
	}

	//Cree une aventure avec une unique situation et ses choix
	public static int ajouterAventure(String nom, String texteSituation, List<String> textesChoix, int id_utilisateur,int id_jeu,EntityManager em,Facade facade)
	{
		Aventure aventure = new Aventure();
		aventure.setNom(nom);
		em.persist(aventure);
		affilierSituationInitiale(texteSituation,textesChoix, id_utilisateur,aventure.getId(),em,facade);
		Jeu jeu = em.find(Jeu.class, id_jeu);
		jeu.getAventure().add(aventure);
		em.merge(aventure);
		return aventure.getId();
		
	}
	

}
