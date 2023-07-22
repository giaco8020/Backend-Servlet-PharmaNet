package ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
import gestoreClinica.GestoreClinica;
import gestoreClinica.Prenotazione;



@WebServlet("/GestoreClinicaAjax")
public class GestoreClinicaAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;


	public GestoreClinicaAjax() {
		super();
		// TODO Auto-generated constructor stub
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		PrintWriter out = response.getWriter();
		DatabaseMock db = (DatabaseMock) this.getServletContext().getAttribute("database");
		String modalita = request.getParameter("modalita");

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

					if(azione.equals("clinicheDisponibili") == true)
					{
						//out.println(db.getClinicheRegistrateJson());
						out.println(Utils.filterJson2(db.getClinicheRegistrateJson()));
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("filtraClinicheDisponibili") == true)
					{
						//Altra modalita con permessi utente
						String capFiltro = request.getParameter("cap");
						out.println(Utils.filterJson2(db.getClinicheByCAPJson(Integer.parseInt(capFiltro))));
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("prenotaVisita") == true) 
					{
						String idClinica = request.getParameter("idClinica");
						String idVisita = request.getParameter("idVisita");

						GestoreClinica gc = db.getClinicaById(idClinica);

						if(gc == null)
						{
							//Errore -- id clinica non valido... 
							out.println(Utils.returnJsonResponse(true,false,10, "Errore - id clinica non valido"));
							return;
						}
						else
						{
							int controllo = gc.prenotaVisita(idVisita, c.getCodiceFiscale());

							this.getServletContext().setAttribute("database", db);
							if(controllo == 1)
							{
								out.println(Utils.returnJsonResponse(false,false,0, "Prenotazione Effettuata con successo"));
								return;
							}
							else if (controllo == -1)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - id Visita non esistente"));
								return;
							}
							else if (controllo == -2)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - Visita gia prenotata"));
								return;
							}
							else if (controllo == -3)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - errore generale"));
								return;
							}
						}

					}
					else if(azione.equals("annullaPrenotazione") == true)
					{
						String idClinica = request.getParameter("idClinica");
						String idVisita = request.getParameter("idVisita");
						String idPrenotazione = request.getParameter("idPrenotazione");

						GestoreClinica gc = db.getClinicaById(idClinica);

						if(gc == null)
						{
							//Errore -- id clinica non valido... 
							out.println(Utils.returnJsonResponse(true,false,10, "Errore - id clinica non valido"));
							return;
						}
						else
						{
							int controllo = gc.annullaVisita(idPrenotazione, idVisita, c.getCodiceFiscale());

							this.getServletContext().setAttribute("database", db);

							if(controllo == 1)
							{
								out.println(Utils.returnJsonResponse(false,false,0, "Annullamento Visita Effettuata con successo"));
								return;
							}
							else if (controllo == -1)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - id Visita non esistente"));
								return;
							}
							else if (controllo == -2)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - Visita non prenotata"));
								return;
							}
							else if (controllo == -3)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - Non hai i permessi per eliminare questa visita"));
								return;
							}
							else if (controllo == -4)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - errore generale procedura"));
								return;
							}
						}

					}
					else if(azione.equals("PrenotazioniCliente") == true)
					{
						ArrayList<Prenotazione> result = new ArrayList<Prenotazione>();

						for(GestoreClinica gc : db.getClinicheRegistrate())
						{
							for(Prenotazione p : gc.getPrenotazioni())
							{
								if(p.getCodiceFiscale().equals(c.getCodiceFiscale()) == true)
								{
									result.add(p);
								}
							}
						}

						Gson gson = new Gson();
						String jsonString = gson.toJson(result);
						out.println(jsonString);
						return;
					}
					else if(azione.equals("informazionePrenotazione") == true)
					{
						String idClinica = request.getParameter("idClinica");
						String idOrdine = request.getParameter("idOrdine");

						Prenotazione res = null;

						for(GestoreClinica gf : db.getClinicheRegistrate())
						{
							if(gf.getIdClinica().equals(idClinica) == true)
							{
								for(Prenotazione p : gf.getPrenotazioni())
								{
									if(p.getCodiceFiscale().equals(c.getCodiceFiscale()) == true && p.getIdPrenotazione().equals(idOrdine) == true)
									{
										res = p;
									}
								}
							}

							Gson gson = new Gson();
							String jsonString = gson.toJson(res);
							out.println(jsonString);
							return;

						}

					}



				}

			}
		}
		else if (modalita.equals("gestore") == true)
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
					out.println(Utils.returnJsonResponse(true,false,7, "Auth token non valido -- nessun gestore farmacia con quell'auth token"));
					return;
				}
				else
				{
					if(azione.equals("mostraCatalogo") == true)
					{
						out.println(gc.getC().getCatalogojson());
						this.getServletContext().setAttribute("database", db);

						return;

					}
					else if(azione.equals("aggiungiVisita") == true)
					{
						String tipologia = request.getParameter("tipologia");
						String medico = request.getParameter("medico");
						String dataOra = request.getParameter("dataOra");

						boolean controllo = gc.getC().aggiungiVisita(tipologia, medico, LocalDateTime.parse(dataOra));

						this.getServletContext().setAttribute("database", db);

						if(controllo == true)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Aggiunta Visita con successo"));
							return;
						}
						else
						{
							out.println(Utils.returnJsonResponse(true,false,17, "Auth token non valido -- Errore inserimento visita"));
							return;
						}

					}
					else if(azione.equals("rimuoviVisita") == true)
					{
						String idVisita = request.getParameter("idVisita");

						boolean controllo = gc.getC().rimuoviVisita(idVisita);

						this.getServletContext().setAttribute("database", db);

						if(controllo == true)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Rimozione visita con successo"));
							return;
						}
						else
						{
							out.println(Utils.returnJsonResponse(true,false,17, "Auth token non valido -- Errore inserimento visita"));
							return;
						}



					}
					else if(azione.equals("visualizzaPrenotazioni") == true)
					{
						out.println(gc.PrenotazioniToJson());
						this.getServletContext().setAttribute("database", db);
						return;

					}
					else if(azione.equals("completaVisita") == true)
					{
						String idPrenotazione = request.getParameter("idPrenotazione");
						int controllo = gc.completaVisita(idPrenotazione);

						if(controllo == 1)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Visita Completata con successo"));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else if (controllo == -1)
						{
							out.println(Utils.returnJsonResponse(true,false,12, "Errore - visita gia completata..."));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else if (controllo == -2)
						{
							out.println(Utils.returnJsonResponse(true,false,12, "Errore - Impossibile trovare la visita"));
							this.getServletContext().setAttribute("database", db);
							return;
						}

					}
					else if(azione.equals("filtraVisiteByCV") == true)
					{
						String cf = request.getParameter("cf");
						Gson gson = new Gson();
						String jsonString = gson.toJson(gc.prenotazioniByCF(cf));
						out.println(jsonString);

					}

				}
			}
		}

	}

}
