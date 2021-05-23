package pack.data;

import java.util.*;
import javax.persistence.*;

@Entity
public class Jeu {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@OneToMany(fetch = FetchType.EAGER)
	Collection<Utilisateur> utilisateurs = new LinkedList<Utilisateur>();
	
	@OneToMany(fetch = FetchType.EAGER)
	Collection<Aventure> aventure = new LinkedList<Aventure>();

	@OneToMany(fetch = FetchType.EAGER)
	Collection<Evenement> evenement = new LinkedList<Evenement>();
	
	public Jeu() {}

	/**
	 * @param utilisateurs
	 * @param aventure
	 * @param evenement
	 */
	public Jeu(Collection<Utilisateur> utilisateurs, Collection<Aventure> aventure, Collection<Evenement> evenement) {
		super();
		this.utilisateurs = utilisateurs;
		this.aventure = aventure;
		this.evenement = evenement;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Collection<Utilisateur> getUtilisateurs() {
		setUtilisateurs(new HashSet<Utilisateur>(utilisateurs));
		return utilisateurs;
	}

	public void setUtilisateurs(Collection<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

	public Collection<Aventure> getAventure() {
		setAventure(new HashSet<Aventure>(aventure));
		return aventure;
	}

	public void setAventure(Collection<Aventure> aventure) {
		this.aventure = aventure;
	}

	public Collection<Evenement> getEvenement() {
		setEvenement(new HashSet<Evenement>(evenement));
		return evenement;
	}

	public void setEvenement(Collection<Evenement> evenement) {
		this.evenement = evenement;
	};
}
