package pack.aux;

import java.io.Serializable;
import java.util.List;

public class InfoTableauBord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<String> getNomsAventures() {
		return nomsAventures;
	}
	public void setNomsAventures(List<String> nomsAventures) {
		this.nomsAventures = nomsAventures;
	}
	public List<Integer> getIdsAventures() {
		return idsAventures;
	}
	public void setIdsAventures(List<Integer> idsAventures) {
		this.idsAventures = idsAventures;
	}
	List<String> nomsAventures ;
	List<Integer> idsAventures;
	List<Integer> idsCheminements;
	public InfoTableauBord(List<String> nomsAventures, List<Integer> idsAventures, List<Integer> idsCheminements,
			List<String> textesCheminements) {
		super();
		this.nomsAventures = nomsAventures;
		this.idsAventures = idsAventures;
		this.idsCheminements = idsCheminements;
		this.textesCheminements = textesCheminements;
	}
	List<String> textesCheminements;
	public List<Integer> getIdsCheminements() {
		return idsCheminements;
	}
	public void setIdsCheminements(List<Integer> idsCheminements) {
		this.idsCheminements = idsCheminements;
	}
	public List<String> getTextesCheminements() {
		return textesCheminements;
	}
	public void setTextesCheminements(List<String> textesCheminements) {
		this.textesCheminements = textesCheminements;
	}

}
