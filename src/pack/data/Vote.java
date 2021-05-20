package pack.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Vote {
	@Id
	 @GeneratedValue(strategy=
	 GenerationType.IDENTITY)
	 int id;
	@ManyToOne
	Utilisateur votant;
	int score;
	
}
