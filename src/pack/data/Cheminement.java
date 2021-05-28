package pack.data;
import javax.persistence.*;
import java.util.*;

@Entity
public class Cheminement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	boolean actif;
	
	@ManyToOne
	Situation position;
	
	@OneToMany
	Collection<SituationClasse> parcours = new LinkedList<SituationClasse>();
	
	@ManyToOne
	Aventure aventure;
	
	public Cheminement() {}

	public Cheminement(int id, boolean actif, Situation position, Collection<SituationClasse> parcours,
			Aventure aventure) {
		super();
		this.id = id;
		this.actif = actif;
		this.position = position;
		this.parcours = parcours;
		this.aventure = aventure;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public Situation getPosition() {
		return position;
	}

	public void setPosition(Situation position) {
		this.position = position;
	}

	public Collection<SituationClasse> getParcours() {
		setParcours(new HashSet<SituationClasse>(parcours));
		return parcours;
	}

	public void setParcours(Collection<SituationClasse> parcours) {
		this.parcours = parcours;
	}

	public Aventure getAventure() {
		return aventure;
	}

	public void setAventure(Aventure aventure) {
		this.aventure = aventure;
	}
}
