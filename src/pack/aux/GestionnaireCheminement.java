package pack.aux;

import java.util.Collection;

import javax.persistence.EntityManager;

import pack.aux.GestionnaireAchivement.TypeAction;
import pack.data.Aventure;
import pack.data.Cheminement;
import pack.data.Choix;
import pack.data.Situation;
import pack.data.SituationClasse;
import pack.data.Utilisateur;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
public class GestionnaireCheminement {

	public static Cheminement visiter(EntityManager em, int id_joueur, int id_situation, int id_aventure)
	{
		RechercheCheminement rc = trouverCheminementDonnantSur(em,id_joueur,id_situation,id_aventure);

		if(rc.getType().equals(TypeRechercheCheminement.NOUVEAU_DEPART))
		{
			//nouveau depart
			return nouveauCheminement(em,id_joueur,id_aventure);
			
		}else if(rc.getType().equals(TypeRechercheCheminement.EXTREMITE))
		{
			//Rien a faire, on est deja sur une feuille
			return em.find(Cheminement.class, rc.getId());
		}
		else if(rc.getType().equals(TypeRechercheCheminement.TROUVE)){
			//Cheminement menant trouve
			return etendreCheminement(em,rc.getId(),id_joueur,id_situation);
		}else
		{
			//Tente de commencer un cheminement au milieu de l'aventure
			return null;
		}
	}
	public static RechercheCheminement trouverCheminementDonnantSur(EntityManager em, int id_joueur, int id_situation, int id_aventure)
	{
		Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
		Aventure av = em.find(Aventure.class, id_aventure);
		Collection<Cheminement> cheminementsTotaux = utilisateur.getCheminements();
		Collection<Cheminement> cheminements = cheminementsTotaux.stream().filter(c->c.isActif()).collect(Collectors.toSet());
		for(Cheminement ch : cheminements)
		{
			Situation position = ch.getPosition();
			if(position.getId()==id_situation)
			{
				new RechercheCheminement(ch.getId(),TypeRechercheCheminement.EXTREMITE);
			}
			Collection<Choix> choix = position.getChoix();
			if(choix != null)
			{
				for(Choix c : choix)
				{
					if(c.getSituation()!= null)
					{
						if(c.getSituation().getId() == id_situation)
						{
							new RechercheCheminement(ch.getId(),TypeRechercheCheminement.TROUVE);
						}
					}
				}
			}
		}
		if(av.getDebut() == null || av.getDebut().getId() == id_situation)
		{
			return new RechercheCheminement(0,TypeRechercheCheminement.NOUVEAU_DEPART);
		}else
		{
			return new RechercheCheminement(0,TypeRechercheCheminement.ERREUR);
		}
	}
	public static int getOrdreMax(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		return cheminement.getParcours().stream().max(Comparator.comparingInt(SituationClasse::getOrdre)).get().getOrdre();
	}
	
	public static int getIdSituationActuelle(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		return cheminement.getPosition().getId();
	}
	
	public static int getIdAventureActuelle(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		return cheminement.getAventure().getId();
	}
	public static String getTexteCheminement(EntityManager em, int id_cheminement)
	{
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		String texte = cheminement.getAventure().getNom();
		String hist = cheminement.getPosition().getTexte();
		int tailleParcours = cheminement.getParcours().size();
		String resume = hist.substring(0,Math.min(hist.length()-1, 20));
		texte += " : "+resume + " ... ( " +tailleParcours+" situations visitées )" ;
		return texte;
	}
	public static String getTexteCompletCheminement(EntityManager em, int id_cheminement)
	{
		String texte = "";
		Cheminement cheminement = em.find(Cheminement.class, id_cheminement);
		texte += cheminement.getAventure().getNom()+"\n";
		List<SituationClasse> chem = cheminement.getParcours().stream()
				.sorted(Comparator.comparingInt(SituationClasse::getOrdre)).collect(Collectors.toList());
		for(int i=0;i<chem.size();i++)
		{
			Situation s = chem.get(i).getSituation();
			if(i<chem.size()-1)
			{
				Situation sNext = chem.get(i+1).getSituation();
				texte += "-> "+getTexteChoixLien(em,s.getId(),sNext.getId())+"\n";
			}
			texte += s.getTexte()+"\n\n";
		}
		return texte;
	}
	public static String getTexteChoixLien(EntityManager em, int id_situationA, int id_situationB)
	{
		Situation sa = em.find(Situation.class, id_situationA);
		Situation sb = em.find(Situation.class, id_situationB);
		Optional<Choix> chx = sa.getChoix().stream().filter(c->c.getSituation().getId()==sb.getId()).findFirst();
		if(chx.isPresent())
		{
			return chx.get().getString_texte();
		}
		return "";
	}
	public static Cheminement nouveauCheminement(EntityManager em, int id_joueur, int id_aventure)
	{
		Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
		Aventure aventure = em.find(Aventure.class, id_aventure);
		Situation situation = aventure.getDebut();
		System.out.println("#######ID SITUATION NOUVEAU CHEM##### : " + situation.getId());
		
		Cheminement cheminement = new Cheminement();
		em.persist(cheminement);
		cheminement.setActif(true);
		cheminement.setAventure(aventure);
		cheminement.setPosition(situation);
		utilisateur.getCheminements().add(cheminement);
		SituationClasse sc = new SituationClasse();
		sc.setOrdre(0);
		sc.setSituation(situation);
		em.persist(sc);
		cheminement.getParcours().add(sc);
		utilisateur.getStatistiques().setNbSituationsVisitees(utilisateur.getStatistiques().getNbSituationsVisitees()+1);
		utilisateur.getStatistiques().setNbAventuresCommencees(utilisateur.getStatistiques().getNbAventuresCommencees()+1);
		GestionnaireAchivement.testRecompense(em, id_joueur, TypeAction.VISITE);
		GestionnaireAchivement.testRecompense(em, id_joueur, TypeAction.CREATION);
		return cheminement;
	}
	public static Cheminement etendreCheminement(EntityManager em,int idChem, int id_joueur, int id_situation)
	{
		Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
		Situation situation = em.find(Situation.class, id_situation);
		Cheminement cheminement = em.find(Cheminement.class, idChem);
		cheminement.setPosition(situation);
		SituationClasse sc = new SituationClasse();
		sc.setOrdre(getOrdreMax(em,idChem)+1);
		sc.setSituation(situation);
		em.persist(sc);
		cheminement.getParcours().add(sc);
		utilisateur.getStatistiques().setNbSituationsVisitees(utilisateur.getStatistiques().getNbSituationsVisitees()+1);
		GestionnaireAchivement.testRecompense(em, id_joueur, TypeAction.VISITE);
		return cheminement;
	}
	
}
