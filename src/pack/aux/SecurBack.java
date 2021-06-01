package pack.aux;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;

import pack.Facade;
import pack.data.Utilisateur;

public class SecurBack {
	//Securite
		public static String getMdp(String pseudo, int id_jeu,EntityManager em, Facade facade) {
			int id_joueur = facade.getIDJoueur(pseudo, id_jeu);
			String mdp;
			if (id_joueur!=-1) {
				Utilisateur utilisateur = em.find(Utilisateur.class, id_joueur);
				mdp = utilisateur.getMdp();
			} else {
				mdp = "";
			}
			
			
			return mdp;
		}
		
		public static String hasher(String mdp) {
			MessageDigest digest = null;
	        try {
	            digest = MessageDigest.getInstance("SHA-256");
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        assert digest != null;
	        byte[] hash = digest.digest(mdp.getBytes(StandardCharsets.UTF_8));
	        return toHexString(hash);
		}
		
		private static String toHexString(byte[] hash)
	    {
	        // Convert byte array into signum representation
	        BigInteger number = new BigInteger(1, hash);

	        // Convert message digest into hex value
	        StringBuilder hexString = new StringBuilder(number.toString(16));

	        // Pad with leading zeros
	        while (hexString.length() < 32)
	        {
	            hexString.insert(0, '0');
	        }

	        return hexString.toString();
	    }
		
		

}
