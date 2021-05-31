package pack.aux;

public class RechercheCheminement {
	int id;
	TypeRechercheCheminement type;
	public int getId() {
		return id;
	}
	public RechercheCheminement(int id, TypeRechercheCheminement type) {
		super();
		this.id = id;
		this.type = type;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TypeRechercheCheminement getType() {
		return type;
	}
	public void setType(TypeRechercheCheminement type) {
		this.type = type;
	}
}
