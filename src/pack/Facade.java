package pack;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pack.data.*;

@Singleton
@Path("/")
public class Facade {
	
	@PersistenceContext(unitName = "MaPU")
	private EntityManager em;
	
	
	@GET
	@Path("/getAventureName")
    @Produces({ "application/json" })
	public String getAventureName() {
		int id_aventure = 1;//HARDCODDED FOR NOW
		Aventure av = em.find(Aventure.class, id_aventure);
		String avName;
		if(av == null) {
			avName = "Aventure not found... Debug again !";
		}else {
			avName = av.getNom();
		}
		avName = "{\"aventureName\": \"" + avName + "\"}"; //JSON formatting
		return avName;
	}
	
	/*Fonctions à faire pour angular:
	 * String getSituation(String idAventure, String idSituation) : renvoie la situation correspondant à l'ID sous forme JSON à partir de l'ID d'une aventure
	 */
	
	///___________________________________///
	
	//Initialise une instance de jeu
	public Jeu initJeu()
	{
		Jeu jeu = new Jeu();
		em.persist(jeu);
		return jeu;
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
		Choix source = em.find(Choix.class, id_choix);
		if(source.getSituation() ==null)
		{
			Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure);
			source.setSituation(nouvelle);
		}
	}
	//affilier une nouvelle situation comme etant la premiere d'une aventure
	public void affilierSituationInitiale(String texte,List<String> textesChoix, int id_utilisateur,int id_aventure)
	{
		Situation nouvelle = ajouterSituation(texte,textesChoix,id_utilisateur,id_aventure);
		Aventure aventure = em.find(Aventure.class, id_aventure);
		aventure.setDebut(nouvelle);
		em.merge(nouvelle);
	}
	//Indique si un choix mene vers une situation qui n'existe pas encore
	public boolean situationDoitEtreCree(int id_choix)
	{
		Choix source = em.find(Choix.class, id_choix);
		return source.getSituation() != null;
	}
	//Cree une aventure avec une unique situation et ses choix
	public Aventure ajouterAventure(String nom, String texteSituation, List<String> textesChoix, int id_utilisateur,int id_jeu)
	{
		Aventure aventure = new Aventure();
		aventure.setNom(nom);
		em.persist(aventure);
		affilierSituationInitiale(texteSituation,textesChoix, id_utilisateur,aventure.getId());
		Jeu jeu = em.find(Jeu.class, id_jeu);
		jeu.getAventure().add(aventure);
		em.merge(aventure);
		return aventure;
		
	}
	
	///Votes ///
	//Ajoute un vote du joueur sur la situation, et la valide si elle passe un certain score
	public Vote voter(int id_joueur, int id_situation, int note)
	{
		int scoreValidation = 3;
		Utilisateur joueur = em.find(Utilisateur.class, id_joueur);
		Situation sit = em.find(Situation.class, id_situation);
		Vote vote = new Vote();
		em.persist(vote);
		vote.setScore(note);
		vote.setVotant(joueur);
		Moderation mod = sit.getModeration();
		if(!aVote(joueur.getId(),mod))
		{
			mod.getVotes().add(vote);
			if((!mod.getValidee()) && calculerScore(mod.getId())>scoreValidation)
			{
				mod.setValidee(true);
			}
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
