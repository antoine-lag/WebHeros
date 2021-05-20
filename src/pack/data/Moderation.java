package pack.data;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class Moderation {
	@Id
	 @GeneratedValue(strategy=
	 GenerationType.IDENTITY)
	 int id;
	@ManyToOne
	Utilisateur createur;
	@OneToMany
	Collection<Vote> votes;
	Boolean validee;
}
