package pack.data;

import javax.persistence.*;

@Entity
public class Choix {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;

	String string_texte;
	
	@OneToOne
	Situation situation;
	
	public Choix() {}

	/**
	 * @param string_texte
	 * @param situation
	 */
	public Choix(String string_texte, Situation situation) {
		super();
		this.string_texte = string_texte;
		this.situation = situation;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getString_texte() {
		return string_texte;
	}

	public void setString_texte(String string_texte) {
		this.string_texte = string_texte;
	}

	public Situation getSituation() {
		return situation;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}
}
