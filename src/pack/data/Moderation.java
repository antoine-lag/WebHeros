package pack.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.persistence.*;

@Entity
public class Moderation {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@ManyToOne
	Utilisateur createur;
	
	@OneToMany
	Collection<Vote> votes = new LinkedList<Vote>();
	
	Boolean validee;
	
	public Moderation() {}

	/**
	 * @param createur
	 * @param votes
	 * @param validee
	 */
	public Moderation(Utilisateur createur, Collection<Vote> votes, Boolean validee) {
		super();
		this.createur = createur;
		this.votes = votes;
		this.validee = validee;
	}
	public Moderation(Utilisateur createur) {
		super();
		this.createur = createur;
		this.validee = false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Utilisateur getCreateur() {
		return createur;
	}

	public void setCreateur(Utilisateur createur) {
		this.createur = createur;
	}

	public Collection<Vote> getVotes() {
		setVotes(new HashSet<Vote>(votes));
		return votes;
	}

	public void setVotes(Collection<Vote> votes) {
		this.votes = votes;
	}

	public Boolean getValidee() {
		return validee;
	}

	public void setValidee(Boolean validee) {
		this.validee = validee;
	};
}
