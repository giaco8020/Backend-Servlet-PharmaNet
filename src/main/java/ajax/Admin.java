package ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import beans.Utils;
import cliente.Cliente;
import database.DatabaseMock;
import gestoreFarmacia.GestoreFarmacia;

@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Admin() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
		
		if(db == null)
		{
			out.println("Database non inizializzato");
		}
		
		String modalita = request.getParameter("modalita");
		
		//Controllo se parametro corretto
		if(modalita == null)
		{
			out.println("Errore inserire modalita come parametro");
		}
		
		if(modalita.equals("vediUtentiRegistrati") == true)
		{
			String filterCf = request.getParameter("cf");
			
			if(filterCf == null)
			{
				//Tutti utenti in formato json
				out.println(db.getClientiRegistratiJson());
			}
			else
			{
				//Ho inserito un cv come parametro filtro -- cerco utente con quel cv
				Cliente c = db.getClienteByCV(filterCf);
				out.println(c.toJson());
			}
			return;
		}
		else if(modalita.equals("vediGFRegistrate") == true)
		{
			
			String filterCap = request.getParameter("cap");
			
			
			if(filterCap == null)
			{
				//Tutti utenti in formato json
				out.println(db.getFarmacieRegistrateJson());
			}
			else
			{
				//Ho inserito un cv come parametro filtro -- cerco utente con quel cv
				ArrayList<GestoreFarmacia> lista = db.getFarmacieByCAP(Integer.parseInt(filterCap));
				Gson gson = new Gson();
				out.println(gson.toJson(lista));
				
			}
			return;
		}
		else if (modalita.equals("sbloccaAccount"))
		{
			String cf = request.getParameter("CF");
			Cliente c = db.getClienteByCV(cf);
			c.setDisabilitato(false);
			this.getServletContext().setAttribute("database", db);
		}
		else if(modalita.equals("test") == true)
		{
		
			String capFiltro = request.getParameter("cap");
			out.println(Utils.filterJson(db.getFarmacieByCAPJson(Integer.parseInt(capFiltro))));
		}
		
		
		
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		out.println("METODO NON CONSENTITO");
		
	}

}
