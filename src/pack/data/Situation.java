package pack.data;

import java.util.*;
import javax.persistence.*;

@Entity
public class Situation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	String texte;
	
	@OneToMany
	Collection<Choix> choix = new LinkedList<Choix>();
	
	@OneToOne
	Moderation moderation;
	
	public Situation() {}

	/**
	 * @param texte
	 * @param choix
	 * @param moderation
	 */
	public Situation(String texte, Collection<Choix> choix, Moderation moderation) {
		super();
		this.texte = texte;
		this.choix = choix;
		this.moderation = moderation;
	}
	public Situation(String texte, Moderation moderation) {
		super();
		this.texte = texte;
		this.moderation = moderation;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Collection<Choix> getChoix() {
		setChoix(new HashSet<Choix>(choix));
		return choix;
	}

	public void setChoix(Collection<Choix> choix) {
		this.choix = choix;
	}

	public Moderation getModeration() {
		return moderation;
	}

	public void setModeration(Moderation moderation) {
		this.moderation = moderation;
	};
}
