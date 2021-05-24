package pack.data;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.persistence.*;
@Entity
public class Utilisateur {
	
	Boolean premium;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	String pseudonyme;
	
	String mail;
	
	String mdp;
	
	@OneToMany(fetch = FetchType.EAGER)
	Collection<Cheminement> cheminements = new LinkedList<Cheminement>();
	
	@OneToMany(fetch = FetchType.EAGER)
	Collection<Accomplissement> accomplissements = new LinkedList<Accomplissement>();
	
	public Utilisateur() {}

	/**
	 * @param premium
	 * @param pseudonyme
	 * @param mail
	 * @param cheminements
	 * @param accomplissements
	 */
	public Utilisateur(Boolean premium,  String mdp, String pseudonyme, String mail, 
			Collection<Cheminement> cheminements, Collection<Accomplissement> accomplissements) {
		super();
		this.premium = premium;
		this.pseudonyme = pseudonyme;
		this.mail = mail;
		this.cheminements = cheminements;
		this.accomplissements = accomplissements;
		this.mdp = mdp;
	}
	
	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public Boolean getPremium() {
		return premium;
	}

	public void setPremium(Boolean premium) {
		this.premium = premium;
	}

	public String getPseudonyme() {
		return pseudonyme;
	}

	public void setPseudonyme(String pseudonyme) {
		this.pseudonyme = pseudonyme;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Collection<Cheminement> getCheminements() {
		setCheminements(new HashSet<Cheminement>(cheminements));
		return cheminements;
	}

	public void setCheminements(Collection<Cheminement> cheminements) {
		this.cheminements = cheminements;
	}

	public Collection<Accomplissement> getAccomplissements() {
		setAccomplissements(new HashSet<Accomplissement>(accomplissements));
		return accomplissements;
	}

	public void setAccomplissements(Collection<Accomplissement> accomplissements) {
		this.accomplissements = accomplissements;
	};
	
}
