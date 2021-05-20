package pack.data;
import java.util.Collection;

import javax.persistence.*;
@Entity
public class Utilisateur {
	
	Boolean premium;
	
	@Id
	String pseudonyme;
	
	String mail;
	
	@OneToMany
	Collection<Cheminement> cheminements;
	
	@OneToMany
	Collection<Accomplissement> accomplissements;
	
	public Utilisateur() {}

	/**
	 * @param premium
	 * @param pseudonyme
	 * @param mail
	 * @param cheminements
	 * @param accomplissements
	 */
	public Utilisateur(Boolean premium, String pseudonyme, String mail, Collection<Cheminement> cheminements,
			Collection<Accomplissement> accomplissements) {
		super();
		this.premium = premium;
		this.pseudonyme = pseudonyme;
		this.mail = mail;
		this.cheminements = cheminements;
		this.accomplissements = accomplissements;
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
		return cheminements;
	}

	public void setCheminements(Collection<Cheminement> cheminements) {
		this.cheminements = cheminements;
	}

	public Collection<Accomplissement> getAccomplissements() {
		return accomplissements;
	}

	public void setAccomplissements(Collection<Accomplissement> accomplissements) {
		this.accomplissements = accomplissements;
	};
}
