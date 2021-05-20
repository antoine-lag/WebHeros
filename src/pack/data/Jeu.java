package pack.data;

import java.util.*;
import javax.persistence.*;

@Entity
public class Jeu {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@OneToMany
	Collection<Utilisateur> utilisateurs;
	
	@OneToMany
	Collection<Aventure> aventure;

	@OneToMany
	Collection<Evenement> evenement;
	
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
		return utilisateurs;
	}

	public void setUtilisateurs(Collection<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

	public Collection<Aventure> getAventure() {
		return aventure;
	}

	public void setAventure(Collection<Aventure> aventure) {
		this.aventure = aventure;
	}

	public Collection<Evenement> getEvenement() {
		return evenement;
	}

	public void setEvenement(Collection<Evenement> evenement) {
		this.evenement = evenement;
	};
}
