package ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import beans.Utils;
import cliente.Cliente;
import database.DatabaseMock;
import gestoreClinica.GestoreClinica;
import gestoreFarmacia.GestoreFarmacia;


@WebServlet("/ProfiloAjax")
public class ProfiloAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProfiloAjax() 
    {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
		String modalita = request.getParameter("modalita");
		
		//Richiesta svolta da un utente
		if(modalita.equals("utente") == true)
		{
			String azione = request.getParameter("azione");
			
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
					//Errore -- non risulta nessun account con quel auth -- qualcosa è andato storto
					out.println(Utils.returnJsonResponse(true,false,7, "Auth token non valido -- nessun client con quell'auth token"));
					return;
				}
				else
				{
					
					if(azione == null)
					{
						out.println(Utils.returnJsonResponse(true,false,7, "parametro 'azione' NON PRESENTE"));
						return;
					}
					
					if(azione.equals("mostraProfilo") == true)
					{
						//Tutto ok -- mostro profilo
						out.println(Utils.returnJsonResponse(false,false,0, c.toJson()));
						
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("modificaQualcosa") == true)
					{
						//Altra modalita con permessi utente
						this.getServletContext().setAttribute("database", db);
					}
				}
			}
				
		}
		else if(modalita.equals("gestoreClinica") == true)
		{
			
			String azione = request.getParameter("azione");
			
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
					//Errore -- non risulta nessun account con quel auth -- qualcosa è andato storto
					out.println(Utils.returnJsonResponse(true,false,7, "Auth token non valido -- nessun gestoreClinica con quell'auth token"));
					return;
				}
				else
				{
					
					if(azione.equals("mostraProfilo") == true)
					{
						//Tutto ok -- mostro profilo
						out.println(Utils.returnJsonResponse(false,false,0, gc.toJson()));
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("modificaOrarioApertura") == true)
					{
						//Tutto ok -- cambio orari apertura
						try
						{
							String nuovoArrayOrario = request.getParameter("orario");
							
							JSONArray jsonArray = new JSONArray(nuovoArrayOrario);
							
							String[] stringArray = new String[jsonArray.length()];
							
							for (int i = 0; i < jsonArray.length(); i++) {
							    stringArray[i] = jsonArray.getString(i);
							}
							
							gc.setOrarioApertura(stringArray);
							
							this.getServletContext().setAttribute("database", db);
							out.println(Utils.returnJsonResponse(false,false,0, "Orario Apertura cambiato con successo"));
							return;
							
						}
						catch(Exception e)
						{
							//Errore durante cambio orari apertura
							out.println(Utils.returnJsonResponse(true,false,9, "Errore durante modificaOrarioApertura() "));
							return;
						}

					}
					
					
				}
				
			}
			
			
			
		}
		else if(modalita.equals("gestoreFarmacia") == true)
		{
			
			String azione = request.getParameter("azione");
			
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
					//Errore -- non risulta nessun account con quel auth -- qualcosa è andato storto
					out.println(Utils.returnJsonResponse(true,false,7, "Auth token non valido -- nessun gestoreFarmacia con quell'auth token"));
					return;
				}
				else
				{
					
					if(azione.equals("mostraProfilo") == true)
					{
						//Tutto ok -- mostro profilo
						out.println(Utils.returnJsonResponse(false,false,0, gf.toJson()));
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("modificaOrarioApertura") == true)
					{
						//Tutto ok -- cambio orari apertura
						try
						{
							String nuovoArrayOrario = request.getParameter("orario");
							
							JSONArray jsonArray = new JSONArray(nuovoArrayOrario);
							
							String[] stringArray = new String[jsonArray.length()];
							
							for (int i = 0; i < jsonArray.length(); i++) {
							    stringArray[i] = jsonArray.getString(i);
							}
							
							gf.setOrarioApertura(stringArray);
							
							this.getServletContext().setAttribute("database", db);
							out.println(Utils.returnJsonResponse(false,false,0, "Orario Apertura cambiato con successo"));
							return;
							
						}
						catch(Exception e)
						{
							//Errore durante cambio orari apertura
							out.println(Utils.returnJsonResponse(true,false,9, "Errore durante modificaOrarioApertura() "));
							return;
						}

					}
					
					
				}
				
			}
			
			
			
		}
		
	}

}
