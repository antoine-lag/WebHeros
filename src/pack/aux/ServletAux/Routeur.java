package pack.aux.ServletAux;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pack.Facade;

public class Routeur {
	public static void renvoiALaConnexion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("index.html");
		disp.forward(request, response);
	}
	public static void renvoiALInscription(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("inscription.html");
		disp.forward(request, response);
	}
	public static void renvoiAAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(testConnexion(request,response))
		{
			RequestDispatcher disp = request.getRequestDispatcher("Aventure.jsp");
			disp.forward(request, response);
		}
	}
	public static void renvoiAAjoutAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(testConnexion(request,response))
		{
			RequestDispatcher disp = request.getRequestDispatcher("AjoutAventure.html");
			disp.forward(request, response);
		}
	}
	public static void renvoiAAjoutSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(testConnexion(request,response))
		{
			RequestDispatcher disp = request.getRequestDispatcher("AjoutSituation.jsp");
			disp.forward(request, response);
		}
	}
	public static void renvoiAPremium(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(testConnexion(request,response))
		{
			RequestDispatcher disp = request.getRequestDispatcher("payerPremium.html");
			disp.forward(request, response);
		}
	}
	public static void renvoiAAfficheurHistoire(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(testConnexion(request,response))
		{
			RequestDispatcher disp = request.getRequestDispatcher("afficheurHistoiresCompletes.jsp");
			disp.forward(request, response);
		}
	}
	public static void renvoyerVersTableauBord(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{
		if(testConnexion(request,response))
		{
			Scribe.remplirTableauBordInfo(request,facade);
			RequestDispatcher disp = request.getRequestDispatcher("accueil.jsp");
			disp.forward(request, response);
		}
	}
	public static boolean testConnexion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session= request.getSession(false);
		if(session == null)
		{
			renvoiALaConnexion(request,response);
			return false;
		}else
		{
			return true;
		}
	}
}
