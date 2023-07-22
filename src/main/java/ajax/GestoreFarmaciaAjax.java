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
import gestoreFarmacia.Ordine;


@WebServlet("/GestoreFarmaciaAjax")
public class GestoreFarmaciaAjax extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	public GestoreFarmaciaAjax() {
		super();

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

					//OK
					if(azione.equals("farmacieDisponibili") == true)
					{
						out.println(Utils.filterJson(db.getFarmacieRegistrateJson()));
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("filtraFarmacieDisponibili") == true) //OK
					{
						//Altra modalita con permessi utente
						String capFiltro = request.getParameter("cap");
						out.println(Utils.filterJson(db.getFarmacieByCAPJson(Integer.parseInt(capFiltro))));
						this.getServletContext().setAttribute("database", db);
						return;
					}
					else if(azione.equals("ordinaFarmaco") == true) 
					{
						String idFarmacia = request.getParameter("idFarmacia");
						String idFarmaco = request.getParameter("idFarmaco");
						int quantitaRichiesta = Integer.parseInt(request.getParameter("quantitaRichiesta"));
						long timestampRitiroFarmaco = Long.parseLong(request.getParameter("timestampRitiroFarmaco"));

						GestoreFarmacia gf = db.getFarmaciaById(idFarmacia);

						if(gf == null)
						{
							//Errore -- id farmacia non valido... 
							out.println(Utils.returnJsonResponse(true,false,10, "id farmacia non valido"));
							return;
						}
						else
						{
							int controllo = gf.aggiungiOrdine(c.getCodiceFiscale(), idFarmaco, quantitaRichiesta, timestampRitiroFarmaco );
							if(controllo == 1)
							{
								out.println(Utils.returnJsonResponse(false,false,0, "Ordine Effettuato con successo"));
								this.getServletContext().setAttribute("database", db);
								return;
							}
							else if (controllo == -1)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - cliente ha gia 5 ordini in sospeso"));
								this.getServletContext().setAttribute("database", db);

								return;
							}
							else if (controllo == -2)
							{
								out.println(Utils.returnJsonResponse(true,false,11, "Errore - id farmaco non esistente"));
								this.getServletContext().setAttribute("database", db);
								return;
							}
						}

					}
					else if(azione.equals("annullaOrdineFarmaco") == true)
					{
						String idFarmacia = request.getParameter("idFarmacia");
						String idOrdine = request.getParameter("idOrdine");

						GestoreFarmacia gf = db.getFarmaciaById(idFarmacia);

						int controllo = gf.rimuoviOrdine(idOrdine, c.getCodiceFiscale());

						if(controllo == 1)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Ordine Effettuato con successo"));
							this.getServletContext().setAttribute("database", db);

							return;
						}
						else if (controllo == -1)
						{
							out.println(Utils.returnJsonResponse(true,false,11, "Errore - cf non corrispondente all'ordine -- non hai i permessi"));
							this.getServletContext().setAttribute("database", db);

							return;
						}
						else if (controllo == -2)
						{
							out.println(Utils.returnJsonResponse(true,false,11, "Errore - ordine non esistente"));
							this.getServletContext().setAttribute("database", db);

							return;
						}

					}
					else if(azione.equals("OrdiniCliente") == true)
					{
						ArrayList<Ordine> result = new ArrayList<Ordine>();

						for(GestoreFarmacia gf : db.getFarmacieRegistrate())
						{
							for(Ordine o : gf.getOrdini())
							{
								if(o.getCodiceFiscale().equals(c.getCodiceFiscale()) == true)
								{
									result.add(o);
								}
							}
						}

						Gson gson = new Gson();
						String jsonString = gson.toJson(result);
						out.println(jsonString);
						return;
					}
					else if(azione.equals("informazioneOrdine") == true)
					{
						String idFarmacia = request.getParameter("idFarmacia");
						String idOrdine = request.getParameter("idOrdine");

						Ordine res = null;

						for(GestoreFarmacia gf : db.getFarmacieRegistrate())
						{
							if(gf.getIdFarmacia().equals(idFarmacia) == true)
							{
								for(Ordine o : gf.getOrdini())
								{
									if(o.getCodiceFiscale().equals(c.getCodiceFiscale()) == true && o.getIdOrdine().equals(idOrdine) == true)
									{
										res = o;
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
				GestoreFarmacia gf = db.getFarmaciaByAuth(authToken);

				if(gf == null)
				{
					//Errore -- non risulta nessun account con quel auth -- qualcosa è andato storto
					out.println(Utils.returnJsonResponse(true,false,7, "Auth token non valido -- nessun gestore farmacia con quell'auth token"));
					return;
				}
				else
				{
					//OK

					if(azione.equals("mostraCatalogo") == true)
					{
						out.println(gf.getC().getCatalogojson());
						this.getServletContext().setAttribute("database", db);
						return;

					}
					else if(azione.equals("modificaQuantitaFarmaco") == true)
					{
						String idFarmaco = request.getParameter("idFarmaco");
						int nuovaQuantita = Integer.parseInt(request.getParameter("nuovaQuantita"));

						int controllo = gf.getC().aggiornaQuantita(idFarmaco, nuovaQuantita);

						if(controllo == 1)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Quantita Modificata con successo"));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else if (controllo == -1)
						{
							out.println(Utils.returnJsonResponse(true,false,11, "Errore - quantità minore di 0 "));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else if (controllo == -2)
						{
							out.println(Utils.returnJsonResponse(true,false,11, "Errore - idFarmaco non esistente"));
							this.getServletContext().setAttribute("database", db);
							return;
						}


					}
					else if(azione.equals("aggiungiFarmaco") == true)
					{
						String nome = request.getParameter("nome");
						double prezzo = Double.parseDouble(request.getParameter("prezzo"));
						int quantitaDisponibile = Integer.parseInt(request.getParameter("quantitaDisponibile"));

						boolean controllo = gf.getC().aggiungiFarmaco(nome,prezzo,quantitaDisponibile);

						if(controllo == true)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Farmaco aggiunto con successo"));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else
						{
							out.println(Utils.returnJsonResponse(true,false,11, "Errore - durante inserimento farmaci in catalogo "));
							this.getServletContext().setAttribute("database", db);
							return;
						}

					}
					else if(azione.equals("rimuoviFarmaco") == true)
					{
						String idFarmaco = request.getParameter("idFarmaco");

						boolean controllo = gf.getC().removeFarmaco(idFarmaco);

						if(controllo == true)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Farmaco rimosso con successo"));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else
						{
							out.println(Utils.returnJsonResponse(true,false,11, "Errore - farmaco non trovato -- controlla idFarmaco"));
							this.getServletContext().setAttribute("database", db);
							return;
						}

					}
					else if(azione.equals("visualizzaOrdini") == true)
					{
						out.println(gf.OrdiniToJson());
						this.getServletContext().setAttribute("database", db);
						return;

					}
					else if(azione.equals("completaOrdine") == true)
					{
						String idOrdine = request.getParameter("idOrdine");
						int controllo = gf.completaOrdine(idOrdine);

						if(controllo == 1)
						{
							out.println(Utils.returnJsonResponse(false,false,0, "Ordine completato con successo"));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else if (controllo == -1)
						{
							out.println(Utils.returnJsonResponse(true,false,12, "Errore - impossibile trovare numero ordine "));
							this.getServletContext().setAttribute("database", db);
							return;
						}
						else if (controllo == -2)
						{
							out.println(Utils.returnJsonResponse(true,false,12, "Errore - Ordine gia completato..."));
							this.getServletContext().setAttribute("database", db);
							return;
						}

					}
					else if(azione.equals("filtraOrdineByCV") == true)
					{
						String cf = request.getParameter("cf");
						Gson gson = new Gson();
						String jsonString = gson.toJson(gf.ordiniByCF(cf));
						out.println(jsonString);

					}


				}





			}



		}

	}

}