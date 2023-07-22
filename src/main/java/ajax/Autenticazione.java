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

import org.json.JSONObject;

import beans.Utils;
import cliente.Cliente;
import database.DatabaseMock;
import gestoreClinica.GestoreClinica;
import gestoreFarmacia.GestoreFarmacia;


@WebServlet("/Autenticazione")
public class Autenticazione extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       

    public Autenticazione() {
        super();
    }

    /*------------------------------------- METODI--------------------------------------------------------------------*/

    public int checkLoginCliente(ArrayList<Cliente> cr, String email, String HashPw)
	{
		for(Cliente c : cr)
		{
			if(c.getEmail().equals(email) == true)
			{
				if(c.getHashPw().equals(HashPw) == true)
				{
					//Email e Pw coincidente
					
					if(c.isDisabilitato() == false)
					{
						//Account non disabilitato --> LOGIN OK
						return 0;
					}
					else
					{
						//Account disabilitato --> ERRORE LOGIN
						return -2;
					}
					
				}
				else
				{
					int num = c.loginCredenzialiErrore(); //Medoto login non corretto
					
					//Se num == -1 ACCOUNT DISABILITATO
					//Se num > 0 tentativi rimanenti
					
					if(num == -1)
					{
						//Account disabilitato
						return -2;
					}
					else
					{
						//Pw sbagliata e tentativiLogin --
						return num;
					}
				}
				
			}
		}
		return -1; //Email non registrata
	}	
    
    public int checkLoginGestoreFarmacia(ArrayList<GestoreFarmacia> gestoreFarmacia, String username, String HashPw)
	{
		for(GestoreFarmacia gf : gestoreFarmacia)
		{
			if(gf.getUsername().equals(username) == true)
			{
				if(gf.getHash256_pw().equals(HashPw) == true)
				{
					//Email e Pw coincidente
					
					if(gf.isDisabilitato() == false)
					{
						//Account non disabilitato --> LOGIN OK
						return 0;
					}
					else
					{
						//Account disabilitato --> ERRORE LOGIN
						return -2;
					}
					
				}
				else
				{
					int num = gf.loginCredenzialiErrore(); //Medoto login non corretto
					
					//Se num == -1 ACCOUNT DISABILITATO
					//Se num > 0 tentativi rimanenti
					
					if(num == -1)
					{
						//Account disabilitato
						return -2;
					}
					else
					{
						//Pw sbagliata e tentativiLogin --
						return num;
					}
				}
				
			}
		}
		return -1; //Username non presente
	}	
    
    public int checkLoginGestoreClinica(ArrayList<GestoreClinica> gestoreClinica, String username, String HashPw)
	{
		for(GestoreClinica gc : gestoreClinica)
		{
			if(gc.getUsername().equals(username) == true)
			{
				if(gc.getHash256_pw().equals(HashPw) == true)
				{
					//Email e Pw coincidente
					
					if(gc.isDisabilitato() == false)
					{
						//Account non disabilitato --> LOGIN OK
						return 0;
					}
					else
					{
						//Account disabilitato --> ERRORE LOGIN
						return -2;
					}
					
				}
				else
				{
					int num = gc.loginCredenzialiErrore(); //Medoto login non corretto
					
					//Se num == -1 ACCOUNT DISABILITATO
					//Se num > 0 tentativi rimanenti
					
					if(num == -1)
					{
						//Account disabilitato
						return -2;
					}
					else
					{
						//Pw sbagliata e tentativiLogin --
						return num;
					}
				}
				
			}
		}
		return -1; //Username non presente
	}	

    
    
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setHeader("Access-Control-Allow-Origin", "*");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	response.setHeader("Access-Control-Allow-Origin", "*");

		PrintWriter out = response.getWriter();
		String modalita = request.getParameter("modalita");
		
		
		if(modalita.equals("utente") == true)
		{
			String jsonString = request.getParameter("json");
			
			String email = null;
		    String hashPw = null;
		    
		    try 
		    {
		        JSONObject json = new JSONObject(jsonString);
		        email = json.getString("email");
		        hashPw = json.getString("hashPw");
		    } 
		    catch (Exception e) 
		    {
		    	out.println(Utils.returnJsonResponse(true,false,3, "Errore Parsing Json request"));
				return;
		    }
		    
		    if(email != null && hashPw != null)
		    {
		    	
		    	DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
		    	
		    	if(db == null)
		    	{
		    		out.println(Utils.returnJsonResponse(false,true,4, "database null - nessuno è registrato"));
		    		return;
		    	}
		    	
		    	// Parametri inviati correttamente
		    	int errore = checkLoginCliente(db.getClientiRegistrati(),email, hashPw);
		    	
		    	//out.println(errore);
		    	
		    	switch (errore) 
		    	{
			        case 0:
			            //LOGIN CORRETTO
			        	
			        	//IMPLEMENTARE 2FA -- 
			        	
			        	//Assegno token AUTH alla classe Cliente
			        	String t = Utils.generateUniqueToken();
			        	Cliente c = db.getClienteByEmail(email);
			        	
			        	c.loginCorretto(t);
			        	
			        	response.setHeader("auth", t);
			        	
			        	
			        	out.println(Utils.returnJsonResponseJ(false,false,0, c.toJson()));
						break;
						
			        case -2:
			        	//ACCOUNT BLOCCATO
			        	
			        	out.println(Utils.returnJsonResponse(false,true,4, email + " Account Bloccato"));
						break;
			        case -1:
			        	
			        	//EMAIL NON REGISTRATA
			        	
			        	out.println(Utils.returnJsonResponse(false,true,5, email + " Email non registrata"));
						break;
			        default:
			        	
			        	//ERRORE PW -- compresi tentativi rimanenti
			        	out.println(Utils.returnJsonResponse(false,true,6, "Password non corretta - Tentativi Rimasti: " + (5-errore)));
						break;
		    	}
		    	
		    	
		    	this.getServletContext().setAttribute("database", db);
		    	return;
		    	
		    }
		    else
		    {
		    	out.println(Utils.returnJsonResponse(true,false,7, "errore ricezione parametri -- email e/o hashPW sono null"));
				return;
		    }
		    
		    
		}
		else if(modalita.equals("gestore") == true)
		{
			String jsonString = request.getParameter("json");
			
			String username = null;
		    String hashPw = null;
		    
		    try 
		    {
		        JSONObject json = new JSONObject(jsonString);
		        username = json.getString("username");
		        hashPw = json.getString("hashPw");
		    } 
		    catch (Exception e) 
		    {
		    	out.println(Utils.returnJsonResponse(true,false,3, "Errore Parsing Json request"));
				return;
		    }
		    
		    DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
		    
		    if(username != null && hashPw != null && username.contains("Farmacia") == true)
		    {
		    	//Login gestore farmaca
		    	// Parametri inviati correttamente
		    	int errore = checkLoginGestoreFarmacia(db.getFarmacieRegistrate(),username, hashPw);
		    	
		    	//out.println(errore);
		    	
		    	switch (errore) 
		    	{
			        case 0:
			            
			        	//LOGIN CORRETTO
			        	
			        	//Assegno token AUTH alla classe Cliente
			        	String t = Utils.generateUniqueToken();
			        	GestoreFarmacia gf = db.getFarmaciaByUsernameHashPw(username,hashPw);
			        	gf.loginCorretto(t);
			        	
			        	response.setHeader("auth", t);
			        
			        	out.println(Utils.returnJsonResponseJ(false,false,0, gf.toJson()));
						break;
						
			        case -2:
			        	//ACCOUNT BLOCCATO
			        	
			        	out.println(Utils.returnJsonResponse(false,true,4, username + " Account Bloccato"));
						break;
			        case -1:
			        	
			        	//USERNAME NON VALIDO
			        	
			        	out.println(Utils.returnJsonResponse(false,true,5, username + " Username non valido"));
						break;
			        default:
			        	
			        	//ERRORE PW -- compresi tentativi rimanenti
			        	out.println(Utils.returnJsonResponse(false,true,6, "Password non corretta - Tentativi Rimasti: " + (5-errore)));
						break;
		    	}
		    	
		    }
		    else if(username != null && hashPw != null && username.contains("Clinica") == true)
		    {
		    	//Login gestore clinica
		    	// Parametri inviati correttamente
		    	
		    	int errore = checkLoginGestoreClinica(db.getClinicheRegistrate(),username, hashPw);
		    	
		    	//out.println(errore);
		    	
		    	switch (errore) 
		    	{
			        case 0:
			            
			        	//LOGIN CORRETTO
			        	
			        	//Assegno token AUTH alla classe Cliente
			        	String t = Utils.generateUniqueToken();
			        	GestoreClinica gc = db.getClinicaByUsernameHashPw(username,hashPw);
			        	gc.loginCorretto(t);
			        	
			        	response.setHeader("auth", t);
			        
			        	out.println(Utils.returnJsonResponseJ(false,false,0, gc.toJson()));
						break;
						
			        case -2:
			        	//ACCOUNT BLOCCATO
			        	
			        	out.println(Utils.returnJsonResponse(false,true,4, username + " Account Bloccato"));
						break;
			        case -1:
			        	
			        	//USERNAME NON VALIDO
			        	
			        	out.println(Utils.returnJsonResponse(false,true,5, username + " Username non valido"));
						break;
			        default:
			        	
			        	//ERRORE PW -- compresi tentativi rimanenti
			        	out.println(Utils.returnJsonResponse(false,true,6, "Password non corretta - Tentativi Rimasti: " + (5-errore)));
						break;
		    	}
		    }
		    else
		    {
		    	out.println(Utils.returnJsonResponse(true,false,7, "errore ricezione parametri -- username e/o hashPW sono null"));
				return;
		    }
		    
		    
		}
		
		
	}

}
