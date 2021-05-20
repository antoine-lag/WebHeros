package pack.data;
import java.util.Collection;

import javax.persistence.*;
@Entity
public class Utilisateur {
	@Id
	 @GeneratedValue(strategy=
	 GenerationType.IDENTITY)
	 int id;
	Boolean premium;
	String pseudonyme;
	String mail;
	@OneToMany
	Collection<Cheminement> cheminements;
	@OneToMany
	Collection<Accomplissement> accomplissements;
}
