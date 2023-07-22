package ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Utils;
import cliente.Cliente;
import database.DatabaseMock;
import gestoreClinica.GestoreClinica;
import gestoreFarmacia.GestoreFarmacia;


@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Logout() 
    {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		out.println("METODO NON CONSENTITO");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
		String modalita = request.getParameter("modalita");
		
		
		if(modalita.equals("utente") == true)
		{
			String authToken = request.getHeader("auth");
			
			if(authToken == null)
			{
				//Qualcosa è andato storto -- se hai fatto il login correttamente auth token deve essere impostato nell 
				//header della sessione con keyword 'auth'
				out.println(Utils.returnJsonResponse(true,false,6, "Qualcosa è andato storto -- auth non è presente nell headers"));
				return;
			}
			else
			{
				Cliente c = db.getClienteByAuth(authToken);
				
				if(c == null)
				{
					out.println(Utils.returnJsonResponse(true,false,7, "Qualcosa è andato storto -- auth è presente ma non associato a nessun account"));
					return;
				}
				else
				{
					c.logout();
					response.addHeader("auth", null);
					out.println(Utils.returnJsonResponse(false,false,0, "Logout effettuato con successo"));
					this.getServletContext().setAttribute("database", db);
					return;
				}
				
				
			}

		}
		else if(modalita.equals("farmacia") == true)
		{
			String authToken = request.getHeader("auth");
			
			if(authToken == null)
			{
				//Qualcosa è andato storto -- se hai fatto il login correttamente auth token deve essere impostato nell 
				//header della sessione con keyword 'auth'
				out.println(Utils.returnJsonResponse(true,false,6, "Qualcosa è andato storto -- auth non è presente nell headers"));
				return;
			}
			else
			{
				GestoreFarmacia gf = db.getFarmaciaByAuth(authToken);
				
				if(gf == null)
				{
					out.println(Utils.returnJsonResponse(true,false,7, "Qualcosa è andato storto -- auth è presente ma non associato a nessun account"));
					return;
				}
				else
				{
					gf.logout();
					response.addHeader("auth", null);
					out.println(Utils.returnJsonResponse(false,false,0, "Logout effettuato con successo"));
					this.getServletContext().setAttribute("database", db);
					return;
				}
				
				
			}
			
		}
		else if(modalita.equals("clinica") == true)
		{
			String authToken = request.getHeader("auth");
			
			if(authToken == null)
			{
				//Qualcosa è andato storto -- se hai fatto il login correttamente auth token deve essere impostato nell 
				//header della sessione con keyword 'auth'
				out.println(Utils.returnJsonResponse(true,false,6, "Qualcosa è andato storto -- auth non è presente nell headers"));
				return;
			}
			else
			{
				GestoreClinica gc = db.getClinicaByAuth(authToken);
				
				if(gc == null)
				{
					out.println(Utils.returnJsonResponse(true,false,7, "Qualcosa è andato storto -- auth è presente ma non associato a nessun account"));
					return;
				}
				else
				{
					gc.logout();
					response.addHeader("auth", null);
					out.println(Utils.returnJsonResponse(false,false,0, "Logout effettuato con successo"));
					this.getServletContext().setAttribute("database", db);
					return;
				}
				
				
			}
			
		}
	}
	
}

