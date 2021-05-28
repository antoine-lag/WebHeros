package pack.data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Vote {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@ManyToOne
	Utilisateur votant;
	
	int score;
	
	public Vote() {}

	/**
	 * @param votant
	 * @param score
	 */
	public Vote(Utilisateur votant, int score) {
		super();
		this.votant = votant;
		this.score = score;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Utilisateur getVotant() {
		return votant;
	}

	public void setVotant(Utilisateur votant) {
		this.votant = votant;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	};
}
