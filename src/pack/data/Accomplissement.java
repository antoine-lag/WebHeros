package pack.data;

import javax.persistence.*;

@Entity
public class Accomplissement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	String date;
	
	String nom;
	
	@ManyToOne
	Utilisateur titulaire;
	
	
	 
	
	public Accomplissement(int id, String date, String nom, Utilisateur titulaire) {
		super();
		this.id = id;
		this.date = date;
		this.nom = nom;
		this.titulaire = titulaire;
	}
	
	public Accomplissement () {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Utilisateur getTitulaire() {
		return titulaire;
	}
	public void setTitulaire(Utilisateur titulaire) {
		this.titulaire = titulaire;
	}
}
