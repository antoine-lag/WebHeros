package pack.data;

import javax.persistence.*;

@Entity
public class SituationClasse {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	int ordre;
	
	@ManyToOne
	Situation situation;
	
	public SituationClasse() {};

}
