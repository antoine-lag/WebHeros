package pack.data;
import javax.persistence.*;
import java.util.*;

@Entity
public class Aventure {
	@Id
	int id;
	
	String nom;
	@OneToOne
	Situation debut;
	@OneToMany
	Collection<Situation> situations;
	
	public Aventure() {}
	
	public Aventure(int id, String nom, Situation debut, Collection<Situation> situations) {
		super();
		this.id = id;
		this.nom = nom;
		this.debut = debut;
		this.situations = situations;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Situation getDebut() {
		return debut;
	}
	public void setDebut(Situation debut) {
		this.debut = debut;
	}
	public Collection<Situation> getSituations() {
		return situations;
	}
	public void setSituations(Collection<Situation> situations) {
		this.situations = situations;
	}
	
	
}
