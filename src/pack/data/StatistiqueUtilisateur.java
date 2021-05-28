package pack.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StatistiqueUtilisateur {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	int nbSituationsVisitees=0;//
	int nbSituationsCrees=0; //
	int nbAventuresCommencees=0;//
	int nbVotes=0;//
	int nbVotesPositifs=0;//
	int nbVotesNegatifs=0;//
	int nbSituationsCreeesValidees=0;//
	int votesTotalRecus=0;//
	int nbVotesRecus=0;//
	int nbAccomplissementsRecus=0;

	public int getNbAccomplissementsRecus() {
		return nbAccomplissementsRecus;
	}

	public void setNbAccomplissementsRecus(int nbAccomplissementsRecus) {
		this.nbAccomplissementsRecus = nbAccomplissementsRecus;
	}

	public int getNbVotesRecus() {
		return nbVotesRecus;
	}

	public void setNbVotesRecus(int nbVotesRecus) {
		this.nbVotesRecus = nbVotesRecus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNbSituationsVisitees() {
		return nbSituationsVisitees;
	}

	public void setNbSituationsVisitees(int nbSituationsVisitees) {
		this.nbSituationsVisitees = nbSituationsVisitees;
	}

	public int getNbSituationsCrees() {
		return nbSituationsCrees;
	}

	public void setNbSituationsCrees(int nbSituationsCrees) {
		this.nbSituationsCrees = nbSituationsCrees;
	}

	public int getNbAventuresCommencees() {
		return nbAventuresCommencees;
	}

	public void setNbAventuresCommencees(int nbAventuresCommencees) {
		this.nbAventuresCommencees = nbAventuresCommencees;
	}

	public int getNbVotes() {
		return nbVotes;
	}

	public void setNbVotes(int nbVotes) {
		this.nbVotes = nbVotes;
	}

	public int getNbVotesPositifs() {
		return nbVotesPositifs;
	}

	public void setNbVotesPositifs(int nbVotesPositifs) {
		this.nbVotesPositifs = nbVotesPositifs;
	}

	public int getNbVotesNegatifs() {
		return nbVotesNegatifs;
	}

	public void setNbVotesNegatifs(int nbVotesNegatifs) {
		this.nbVotesNegatifs = nbVotesNegatifs;
	}

	public int getNbSituationsCreeesValidees() {
		return nbSituationsCreeesValidees;
	}

	public void setNbSituationsCreeesValidees(int nbSituationsCreeesValidees) {
		this.nbSituationsCreeesValidees = nbSituationsCreeesValidees;
	}

	public int getVotesTotalRecus() {
		return votesTotalRecus;
	}

	public void setVotesTotalRecus(int votesTotalRecus) {
		this.votesTotalRecus = votesTotalRecus;
	}

	public StatistiqueUtilisateur() {
		
	}
	
}
