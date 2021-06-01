package pack.aux.ServletAux;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		RequestDispatcher disp = request.getRequestDispatcher("Aventure.jsp");
		disp.forward(request, response);
	}
	public static void renvoiAAjoutAventure(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("AjoutAventure.html");
		disp.forward(request, response);
	}
	public static void renvoiAAjoutSituation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("AjoutSituation.jsp");
		disp.forward(request, response);
	}
	public static void renvoiAPremium(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher disp = request.getRequestDispatcher("payerPremium.html");
		disp.forward(request, response);
	}
	public static void renvoyerVersTableauBord(HttpServletRequest request, HttpServletResponse response,Facade facade) throws ServletException, IOException
	{
		Scribe.remplirTableauBordInfo(request,facade);
		RequestDispatcher disp = request.getRequestDispatcher("accueil.jsp");
		disp.forward(request, response);
	}
}
