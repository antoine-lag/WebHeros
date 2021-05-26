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
	public InfoTableauBord(List<String> nomsAventures, List<Integer> idsAventures) {
		super();
		this.nomsAventures = nomsAventures;
		this.idsAventures = idsAventures;
	}
}
