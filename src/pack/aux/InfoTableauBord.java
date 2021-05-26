package pack.aux;

import java.io.Serializable;
import java.util.List;

import pack.data.StatistiqueUtilisateur;

public class InfoTableauBord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	StatistiqueUtilisateur stats;
	public StatistiqueUtilisateur getStats() {
		return stats;
	}
	public void setStats(StatistiqueUtilisateur stats) {
		this.stats = stats;
	}
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
	List<String> textesAccomplissements;
	public List<String> getTextesAccomplissements() {
		return textesAccomplissements;
	}
	public void setTextesAccomplissements(List<String> textesAccomplissements) {
		this.textesAccomplissements = textesAccomplissements;
	}
	public List<String> getDatesAccomplissements() {
		return datesAccomplissements;
	}
	public void setDatesAccomplissements(List<String> datesAccomplissements) {
		this.datesAccomplissements = datesAccomplissements;
	}
	List<String> datesAccomplissements;
	List<String> textesCheminements;
	List<String> textesCompletsCheminements;
	List<Boolean> isActiveCheminements;
	public List<String> getTextesCompletsCheminements() {
		return textesCompletsCheminements;
	}
	public void setTextesCompletsCheminements(List<String> textesCompletsCheminements) {
		this.textesCompletsCheminements = textesCompletsCheminements;
	}
	public List<Boolean> getIsActiveCheminements() {
		return isActiveCheminements;
	}
	public void setIsActiveCheminements(List<Boolean> isActiveCheminements) {
		this.isActiveCheminements = isActiveCheminements;
	}
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
