package pack;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import pack.aux.GestionnaireAchivement;
import pack.aux.GestionnaireCheminement;
import pack.aux.InfoTableauBord;
import pack.aux.GestionnaireAchivement.TypeAction;
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
	public void voter(int idJoueur, int idSituation, String voteValue) {
		System.out.println("\n\n\n\n################ rest/voter called###############");
		System.out.println("idJoueur = " + idJoueur);
		System.out.println("idSituation = " + idSituation);
		System.out.println("voteValue = " + voteValue);
	}
	
	@GET
	@Path("/getsituation")
	//Recup parametres get
	//angular routes : <script src=https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0rc1/angular-route.min.js></script>
	@Produces({ "application/json" })
	public String getSituation(@DefaultValue("-1") @QueryParam("idSituation") int idSituation) {
		System.out.println("\n\n\n\n################ rest/getsituation called###############");
		System.out.println("idSituation = " + idSituation);
		
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
		JsonArray JchoicesList = new JsonArray();
		for(Choix c:choicesList)
		{
			JsonObject ch = new JsonObject();
			ch.put("text", c.getString_texte());
			ch.put("id",c.getId());
			ch.put("situationExiste",!situationDoitEtreCree(c.getId()));
			JchoicesList.add(ch);
		}
		envoi.put("choicesList", JchoicesList);
		String sJsonData = envoi.toJson();
		return sJsonData;
	}
	
	@GET
	@Path("/getsituationchoix")
	@Produces({"application/json"})
	public String getSituationChoix(@DefaultValue("-1") @QueryParam("idChoix") int idChoix,
			@DefaultValue("-1") @QueryParam("idJoueur") int idJoueur,
			@DefaultValue("-1") @QueryParam("idAventure") int idAventure) {
		System.out.println("\n\n\n\n################ rest/getsituationchoix called###############");
		System.out.println("idChoix = " + idChoix);
		System.out.println("idJoueur = " + idJoueur);
		System.out.println("idAventure = " + idAventure);
		Choix c = em.find(Choix.class, idChoix);
		Situation s = c.getSituation();
		visiter(idJoueur,s.getId(),idAventure,false);
		return getSituation(s.getId());
	}
	
	
	//Initialise une instance de jeu
	public Jeu initJeu()
	{
		Jeu jeu = new Jeu();
		em.persist(jeu);
		return jeu;
	}
	public InfoTableauBord getInfoTableauBord(int idJeu, int idJoueur)
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
		return tableau;
	}
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
			em.merge(choix);
		}

		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.getSituations().add(situation);
		em.merge(situation);
		visiter(id_utilisateur, situation.getId(), id_aventure,true);
		utilisateur.getStatistiques().setNbSituationsCrees(utilisateur.getStatistiques().getNbSituationsCrees()+1);
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
	public Situation affilierSituationFille(int id_choix,String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		Choix source = em.find(Choix.class, id_choix);
		if(source.getSituation() ==null)
		{
			Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure);
			source.setSituation(nouvelle);
			return nouvelle;
		}
		return null;
	}
	//affilier une nouvelle situation comme etant la premiere d'une aventure
	public Situation affilierSituationInitiale(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure);
		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.setDebut(nouvelle);
		em.merge(nouvelle);
		
		return nouvelle;
	}
	//Indique si un choix mene vers une situation qui n'existe pas encore
	public boolean situationDoitEtreCree(int id_choix)
	{
		Choix source = em.find(Choix.class, id_choix);
		return source.getSituation() == null;
	}
	//Cree une aventure avec une unique situation et ses choix
	public int ajouterAventure(String nom, String texteSituation, List<String> textesChoix, int id_utilisateur,int id_jeu)
	{
		Aventure aventure = new Aventure();
		aventure.setNom(nom);
		em.persist(aventure);
		affilierSituationInitiale(texteSituation,textesChoix, id_utilisateur,aventure.getId());
		Jeu jeu = em.find(Jeu.class, id_jeu);
		jeu.getAventure().add(aventure);
		em.merge(aventure);
		return aventure.getId();
		
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
	public String getNomAventureCheminement( int id_cheminement)
	{
		return em.find(Cheminement.class, id_cheminement).getAventure().getNom();
		
	}
	
	///Votes ///
	//Ajoute un vote du joueur sur la situation, et la valide si elle passe un certain score
	public Vote voter(int id_joueur, int id_situation, int note)
	{
		int scoreValidation = 3;
		Utilisateur joueur = em.find(Utilisateur.class, id_joueur);

		joueur.getStatistiques().setNbVotes(joueur.getStatistiques().getNbVotes()+1);
		Situation sit = em.find(Situation.class, id_situation);
		Vote vote = new Vote();
		em.persist(vote);
		vote.setScore(note);
		vote.setVotant(joueur);
		Moderation mod = sit.getModeration();
		if(!aVote(joueur.getId(),mod))
		{
			if(note>0)
			{
				joueur.getStatistiques().setNbVotesPositifs(joueur.getStatistiques().getNbVotesPositifs()+1);
			}else if(note<0)
			{
				joueur.getStatistiques().setNbVotesNegatifs(joueur.getStatistiques().getNbVotesNegatifs()+1);
			}
			mod.getVotes().add(vote);
			if((!mod.getValidee()) && calculerScore(mod.getId())>scoreValidation)
			{
				mod.setValidee(true);
				mod.getCreateur().getStatistiques().
				setNbSituationsCreeesValidees(mod.getCreateur().getStatistiques()
						.getNbSituationsCreeesValidees()+1);
			}
			mod.getCreateur().getStatistiques().setVotesTotalRecus(mod.getCreateur().getStatistiques()
					.getVotesTotalRecus()+note);
			mod.getCreateur().getStatistiques().setNbVotesRecus(mod.getCreateur().getStatistiques()
					.getNbVotesRecus()+1);
			GestionnaireAchivement.testRecompense(em, id_joueur, TypeAction.VOTE);
			GestionnaireAchivement.testRecompense(em, mod.getCreateur().getId(), TypeAction.MODERATION);
		}
		
		return vote;
	}
	//Calcule le score associe a une moderation
	public int calculerScore(int id_moderation)
	{
		Moderation mod = em.find(Moderation.class, id_moderation);
		Collection<Vote> votes = mod.getVotes();
		int score = 0;
		for(Vote v : votes)
		{
			score += v.getScore();
		}
		return score;
	}
	//Indique si un joueur a vote pour une situation
	public boolean aVote(int id_joueur, Moderation moderation)
	{
		return moderation.getVotes().stream().anyMatch(x->(x.getVotant().getId() == id_joueur));
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
	
	//Joueurs//
	//Indique si un pseudo existe deja
	public boolean pseudoExiste(String pseudoJoueur, int id_jeu)
	{
		Jeu jeu = em.find(Jeu.class, id_jeu);
		Collection<Utilisateur> joueurs = jeu.getUtilisateurs();
		return joueurs.stream().anyMatch(j->j.getPseudonyme().equals(pseudoJoueur));

	}
	//Trouve l'ID d'un joueur a partir de son pseudo, -1 sinon
	public int getIDJoueur(String pseudoJoueur, int id_jeu)
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
	public Utilisateur ajouterUtilisateur(String pseudo,String email, int id_jeu, String mdp)
	{
		Utilisateur utilisateur = new Utilisateur();
		StatistiqueUtilisateur stat = new StatistiqueUtilisateur();
		em.persist(stat);
		utilisateur.setStatistiques(stat);
		utilisateur.setPremium(false);
		utilisateur.setMail(email);
		utilisateur.setPseudonyme(pseudo);
		utilisateur.setMdp(mdp);
		if(!pseudoExiste(pseudo, id_jeu))
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
	public Collection<Utilisateur> getUtilisateurs(int id_jeu)
	{
		Jeu jeu = em.find(Jeu.class, id_jeu);
		return jeu.getUtilisateurs();
	}
	public void mergeUtilisateurs(int id_jeu)
	{
		Jeu jeu = em.find(Jeu.class, id_jeu);
		for(Utilisateur u : jeu.getUtilisateurs())
		{
			em.merge(u);
		}
	}
	
	
	//Securite
	public String getMdp(String pseudo, int id_jeu) {
		int id_joueur = getIDJoueur(pseudo, id_jeu);
		String mdp;
		if (id_joueur!=-1) {
			Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
			mdp = utilisateur.getMdp();
		} else {
			mdp = "";
		}
		return mdp;
	}
	
	public String hasher(String mdp) {
		MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        byte[] hash = digest.digest(mdp.getBytes(StandardCharsets.UTF_8));
        return toHexString(hash);
	}
	
	public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
	




}
