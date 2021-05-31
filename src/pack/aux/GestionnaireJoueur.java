package pack.aux;

import java.util.Collection;
import java.util.Optional;

import javax.persistence.EntityManager;

import pack.data.Jeu;
import pack.data.StatistiqueUtilisateur;
import pack.data.Utilisateur;

public class GestionnaireJoueur {
	//Joueurs//
	//Indique si un pseudo existe deja
	public static boolean pseudoExiste(String pseudoJoueur, int id_jeu,EntityManager em)
	{
		Jeu jeu = em.find(Jeu.class, id_jeu);
		Collection<Utilisateur> joueurs = jeu.getUtilisateurs();
		return joueurs.stream().anyMatch(j->j.getPseudonyme().equals(pseudoJoueur));

	}
	//Trouve l'ID d'un joueur a partir de son pseudo, -1 sinon
	public static int getIDJoueur(String pseudoJoueur, int id_jeu,EntityManager em)
	{
		Jeu jeu = em.find(Jeu.class, id_jeu);
		Collection<Utilisateur> joueurs = jeu.getUtilisateurs();
		Optional<Utilisateur> ut = joueurs.stream().filter(j->j.getPseudonyme().equals(pseudoJoueur)).findFirst();
		if(ut.isPresent())
		{
			return ut.get().getId();
		}else
		{
			return -1;
		}
	}
	//Ajoute un utilisateur a la base de donnees, null si deja present
	public static Utilisateur ajouterUtilisateur(String pseudo,String email, int id_jeu, String mdp,EntityManager em)
	{
		Utilisateur utilisateur = new Utilisateur();
		StatistiqueUtilisateur stat = new StatistiqueUtilisateur();
		em.persist(stat);
		utilisateur.setStatistiques(stat);
		utilisateur.setPremium(false);
		utilisateur.setMail(email);
		utilisateur.setPseudonyme(pseudo);
		utilisateur.setMdp(mdp);
		if(!pseudoExiste(pseudo, id_jeu,em))
		{
			em.persist(utilisateur);
			Jeu jeu = em.find(Jeu.class, id_jeu);
			Collection<Utilisateur> utilisateurs = jeu.getUtilisateurs();
			utilisateurs.add(utilisateur);
			jeu.setUtilisateurs(utilisateurs);
			em.merge(utilisateur);
			return utilisateur;
		} else {
			return null;
		}
	}
	public static Collection<Utilisateur> getUtilisateurs(int id_jeu,EntityManager em)
	{
		Jeu jeu = em.find(Jeu.class, id_jeu);
		return jeu.getUtilisateurs();
	}

}
