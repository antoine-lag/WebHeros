package pack.data;

import javax.persistence.*;

@Entity
public class SituationClasse {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	int ordre;
	
	@ManyToOne(fetch = FetchType.EAGER)
	Situation situation;
	
	public SituationClasse() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}

	public Situation getSituation() {
		return situation;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	};

}
