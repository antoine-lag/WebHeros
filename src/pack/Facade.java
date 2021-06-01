package pack;

import java.util.*;


import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;



import pack.aux.Agregateur;
import pack.aux.Assesseur;

import pack.aux.GestionnaireCheminement;
import pack.aux.GestionnaireJoueur;
import pack.aux.InfoTableauBord;
import pack.aux.SecurBack;
import pack.aux.GestionnaireAjouts;
import pack.data.*;

@Singleton
@Path("/")
public class Facade {
	
	@PersistenceContext(unitName = "MaPU")
	private EntityManager em;
	
	public String getAventureName(int id_aventure) {
		Aventure av = em.find(Aventure.class, id_aventure);
		String avName;
		if(av == null) {
			avName = null;
		}else {
			avName = av.getNom();
		}
		return avName;
	}
	
	@POST
	@Path("/vote")
	@Consumes({"application/json"})
	public void voter(String param) {
		String[] params = param.split(";");
		if(params.length == 3) {
			int idJoueur = Integer.valueOf(params[0]);
			int idSituation = Integer.valueOf(params[1]);
			String voteValue = params[2];
			if(voteValue.equals("up"))
			{
				Assesseur.voter(idJoueur,idSituation,1,em);
			}
			if(voteValue.equals("down"))
			{
				Assesseur.voter(idJoueur,idSituation,-1,em);
			}
		}
	}
	
	@GET
	@Path("/getsituation")
	//Recup parametres get
	//angular routes : <script src=https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0rc1/angular-route.min.js></script>
	@Produces({ "application/json" })
	public String getSituation(@DefaultValue("-1") @QueryParam("idSituation") int idSituation,
			@DefaultValue("-1") @QueryParam("idJoueur")  int idJoueur, @DefaultValue("-1") @QueryParam("idAventure")  int idAventure) { 
			String res = Agregateur.getJSONSituation(idSituation, idJoueur, em);
			visiter(idJoueur, idSituation, idAventure, false);
			return res;
	}
	
	@GET
	@Path("/getsituationchoix")
	@Produces({"application/json"})
	public String getSituationChoix(@DefaultValue("-1") @QueryParam("idChoix") int idChoix,
			@DefaultValue("-1") @QueryParam("idJoueur") int idJoueur,
			@DefaultValue("-1") @QueryParam("idAventure") int idAventure) {
		return Agregateur.getJSONSituationChoix(idChoix, idJoueur, idAventure, em, this);
	}
	
	
	//Initialise une instance de jeu
	public Jeu initJeu()
	{
		return GestionnaireAjouts.ajoutJeu(em);
	}
	public InfoTableauBord getInfoTableauBord(int idJeu, int idJoueur)
	{
		return Agregateur.getInfoTableauBord(idJeu, idJoueur, em);
	}
	//affilier une nouvelle situation comme découlant d'un choix
	public Situation affilierSituationFille(int id_choix,String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		return GestionnaireAjouts.affilierSituationFille(id_choix, texte, textesChoix, id_utilisateur, id_aventure, em, this);
	}
	//affilier une nouvelle situation comme etant la premiere d'une aventure
	public Situation affilierSituationInitiale(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		return GestionnaireAjouts.affilierSituationInitiale(texte, textesChoix, id_utilisateur, id_aventure, em, this);
	}

	//Cree une aventure avec une unique situation et ses choix
	public int ajouterAventure(String nom, String texteSituation, List<String> textesChoix, int id_utilisateur,int id_jeu)
	{
		return GestionnaireAjouts.ajouterAventure(nom, texteSituation, textesChoix, id_utilisateur, id_jeu, em, this);
	}
	public Aventure getAventure(int id_aventure)
	{
		return em.find(Aventure.class, id_aventure);
		
	}
	public Cheminement visiter(int id_joueur,int id_situation, int id_aventure, boolean clore)
	{
		Cheminement chem = GestionnaireCheminement.visiter(em, id_joueur, id_situation, id_aventure);
		if(chem != null)
		{
			if(chem.isActif())
			{
				if(clore)
				{
					chem.setActif(false);
				}
			}
		}
		//System.out.println("---------"+em.find(Cheminement.class, chem.getId()).isActif()+"---------");
		System.out.println("++++++++++++"+chem.isActif()+"+++++++++++++");
		return chem;
		
	}
	public int nouveauCheminement(int id_joueur, int id_aventure)
	{
		return GestionnaireCheminement.nouveauCheminement(em, id_joueur, id_aventure).getId();
		
	}
	public Cheminement getCheminement( int id_cheminement)
	{
		return em.find(Cheminement.class, id_cheminement);
		
	}
	public int getIdAventureCheminement( int id_cheminement)
	{
		return em.find(Cheminement.class, id_cheminement).getAventure().getId();
		
	}
	public int getIdPositionCheminement( int id_cheminement)
	{
		return em.find(Cheminement.class, id_cheminement).getPosition().getId();
		
	}
	public String getNomAventureCheminement( int id_cheminement)
	{
		return em.find(Cheminement.class, id_cheminement).getAventure().getNom();
		
	}
	

	public boolean estPremium(int id_joueur)
	{
		return em.find(Utilisateur.class, id_joueur).getPremium();
	}
	public void setPremium(int id_joueur)
	{
		em.find(Utilisateur.class, id_joueur).setPremium(true);
	}
	
	//Indique si une situation est validee et peut etre poursuivie
	public boolean estValidee(int id_situation)
	{
		Situation situation = em.find(Situation.class,id_situation);
		return situation.getModeration().getValidee();
	}
	public void forcerValidee(int id_situation)
	{
		Situation situation = em.find(Situation.class,id_situation);
		situation.getModeration().setValidee(true);
	}
	
	//Joueurs//
	//Indique si un pseudo existe deja
	public boolean pseudoExiste(String pseudoJoueur, int id_jeu)
	{
		return GestionnaireJoueur.pseudoExiste(pseudoJoueur, id_jeu, em);

	}
	//Trouve l'ID d'un joueur a partir de son pseudo, -1 sinon
	public int getIDJoueur(String pseudoJoueur, int id_jeu)
	{
		return GestionnaireJoueur.getIDJoueur(pseudoJoueur, id_jeu, em);
	}
	//Ajoute un utilisateur a la base de donnees, null si deja present
	public Utilisateur ajouterUtilisateur(String pseudo,String email, int id_jeu, String mdp)
	{
		return GestionnaireJoueur.ajouterUtilisateur(pseudo, email, id_jeu, mdp, em);
	}
	public Collection<Utilisateur> getUtilisateurs(int id_jeu)
	{
		return GestionnaireJoueur.getUtilisateurs(id_jeu, em);
	}

	
	
	//Securite
	public String getMdp(String pseudo, int id_jeu) {
		return SecurBack.getMdp(pseudo, id_jeu, em, this);
	}

}
