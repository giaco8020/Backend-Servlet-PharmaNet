package ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import beans.Utils;
import cliente.Cliente;
import database.DatabaseMock;


@WebServlet("/Registrazione")
public class Registrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Registrazione() 
    {
        super();
    }
    
    /*				 		METODI 							*/
    
    public boolean checkValidCF(ArrayList<Cliente> cr , String cf)
	{
		for(Cliente c : cr)
		{
			if(c.getCodiceFiscale().equals(cf) == true)
			{
				return false;
			}
		}
		
		return true;
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		DatabaseMock db = new DatabaseMock();
		this.getServletContext().setAttribute("database", db);
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		out.println("METODO NON CONSENTITO");
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		String modalita = request.getParameter("modalita");
		
		if(modalita.equals("registrazione") == true)
		{
			//Registrazione Utente
			
			String jsonPayload = request.getParameter("json");
			//System.out.println(jsonPayload);
			Cliente nuovoCliente = null;
			
			try
			{
				nuovoCliente = Utils.readClienteFromJson(jsonPayload);
			}
			catch(Exception e)
			{
				out.println(Utils.returnJsonResponse(true,false,1, "Conversione da json a struttura Client "));
				return;
			}
			
			DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
			
			if(checkValidCF( db.getClientiRegistrati() ,nuovoCliente.getCodiceFiscale()) == true)
			{
				//CodiceFiscale non occupato -- REGISTRAZIONE POSSIBILE
				nuovoCliente.RegistrazioneCompletata();
				db.getClientiRegistrati().add(nuovoCliente);
			
				this.getServletContext().setAttribute("database", db);
				
				out.println(Utils.returnJsonResponse(false,false,0, "Registrazione Completata"));
				return;
			}
			else
			{
				out.println(Utils.returnJsonResponse(false,true,2, "Codice Fiscale occupato"));
				return;
			}

		}
		
	}

}
