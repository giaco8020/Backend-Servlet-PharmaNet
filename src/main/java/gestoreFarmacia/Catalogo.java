package gestoreFarmacia;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Catalogo {
	
	private ArrayList<Farmaco> catalogo;

	public Catalogo() {
		catalogo = new ArrayList<Farmaco>();
	}

	public ArrayList<Farmaco> getCatalogo() {
		return catalogo;
	}
	
	
	//Aggiunge un farmaco al catalogo
	
	public boolean aggiungiFarmaco(String nome, double prezzo, int quantitaDisponibile)
	{
		String idF = "F" + this.catalogo.size();
		Farmaco nuovof = new Farmaco(idF, nome, prezzo, quantitaDisponibile);
		
		this.catalogo.add(nuovof);
		return true;
		
	}
	
	
	//Elimina un farmaco dal catalogo con lo stesso id 
	//True = se eliminazione con successo
	//False = se non esiste nessun idFarmaco associato
	public boolean removeFarmaco(String idFarmacoRimuovere)
	{
		for(int i = 0; i < this.catalogo.size(); i++)
		{
			if(this.catalogo.get(i).getIdFarmaco().equals(idFarmacoRimuovere) == true)
			{
				this.catalogo.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	public int aggiornaQuantita(String idFarmaco, int nuovaQuantita)
	{
		if(nuovaQuantita < 0)
		{
			return -1;
		}
		
		for(Farmaco f : this.getCatalogo())
		{
			if(f.getIdFarmaco().equals(idFarmaco) == true)
			{
				f.setQuantitaDisponibile(nuovaQuantita);
				return 1;
			}
		}
		
		return -2;
	}
	
	public String getCatalogojson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getCatalogo());
		return jsonString;
	}
	
	
	
	
	
}
