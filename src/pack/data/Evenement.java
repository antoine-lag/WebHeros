package pack.data;

import java.util.*;
import javax.persistence.*;

@Entity
public class Evenement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;

	String dateDebut;
	
	String dateFin;
	
	String type;
	
	@OneToMany(fetch = FetchType.EAGER)
	Collection<Utilisateur> participants = new LinkedList<Utilisateur>();
		
	public Evenement() {}
	
	/**
	 * @param dateDebut
	 * @param dateFin
	 * @param participants
	 * @param type
	 */
	public Evenement(String dateDebut, String dateFin, Collection<Utilisateur> participants, String type) {
		super();
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.participants = participants;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}

	public Collection<Utilisateur> getParticipants() {
		setParticipants(new HashSet<Utilisateur>(participants));
		return participants;
	}

	public void setParticipants(Collection<Utilisateur> participants) {
		this.participants = participants;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
