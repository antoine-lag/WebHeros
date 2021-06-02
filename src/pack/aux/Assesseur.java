package pack.aux;

import java.util.Collection;

import javax.persistence.EntityManager;

import pack.aux.GestionnaireAchivement.TypeAction;
import pack.data.Moderation;
import pack.data.Situation;
import pack.data.Utilisateur;
import pack.data.Vote;

public class Assesseur {
	///Votes ///
	//Ajoute un vote du joueur sur la situation, et la valide si elle passe un certain score
	public static Vote voter(int id_joueur, int id_situation, int note,EntityManager em)
	{
		int scoreValidation = 1;
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
			if((!mod.getValidee()) && calculerScore(mod.getId(),em)>=scoreValidation)
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
	public static int calculerScore(int id_moderation,EntityManager em)
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
	public static boolean aVote(int id_joueur, Moderation moderation)
	{
		return moderation.getVotes().stream().anyMatch(x->(x.getVotant().getId() == id_joueur));
	}
}
