package gestoreClinica;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.Gson;


public class Catalogo {
	
	private ArrayList<Visita> catalogo;

	public Catalogo() 
	{
		this.catalogo = new ArrayList<Visita>();
	}

	public ArrayList<Visita> getCatalogo() {
		return catalogo;
	}
	
	//Aggiunge una visita al catalogo
	
	public boolean aggiungiVisita(String tipologia, String medico, LocalDateTime dataOra)
	{
		String idV = "Visita" + this.catalogo.size();
		Visita nuovaV = new Visita(idV, tipologia, medico, dataOra);
		
		this.catalogo.add(nuovaV);
		return true;
		
	}
	
	
	//Elimina una visita dal catalogo con lo stesso id 
	//True = se eliminazione con successo
	//False = se non esiste nessun idVisita associata
	
	public boolean rimuoviVisita(String idVisitaRimuovere)
	{
		for(int i = 0; i < this.getCatalogo().size(); i++)
		{
			if(this.getCatalogo().get(i).getIdVisita().equals(idVisitaRimuovere) == true)
			{
				if(this.getCatalogo().get(i).isPrenotata() == true)
				{
					//DEVO AVVISARE UTENTE CHE LA VISITA E' CANCELLATA
				}
				
				//Elimino dal catalogo
				this.getCatalogo().remove(i);
				return true;
			}
		}
		return false;
	}
	
	
	
	public String getCatalogojson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getCatalogo());
		return jsonString;
	}
	
}
