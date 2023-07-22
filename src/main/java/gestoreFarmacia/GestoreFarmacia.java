package gestoreFarmacia;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;


public class GestoreFarmacia {
	
	private String username;
	private String Hash256_pw;
	
	private String idFarmacia;
	private String nomeFarmacia;
	private String indirizzo;
	private int CAP;
	private String[] orarioApertura;
	private Catalogo c;
	
	private boolean disabilitato;
	private int tentativiLogin;
	private String tipo_account;
	
	private String tokenAUTH;
	
	private ArrayList<Ordine> ordini;

	public GestoreFarmacia(String username, String hash256_pw, String nomeFarmacia, String indirizzo, String[] orarioApertura, int cap) 
	{
		this.username = username;
		this.Hash256_pw = hash256_pw;
		this.idFarmacia = null;
		this.nomeFarmacia = nomeFarmacia;
		this.indirizzo = indirizzo;
		this.orarioApertura = orarioApertura;
		this.c = new Catalogo();
		this.CAP = cap;
		
		this.disabilitato = false;
		this.tentativiLogin = 0;
		this.tipo_account = "farmacia";
		
		this.tokenAUTH = null;
		ordini = new ArrayList<Ordine>();
	}
	
	
	/*------------------------------METODI GET------------------------------*/
	
	
	
	public String[] getOrarioApertura() {
		return orarioApertura;
	}

	public ArrayList<Ordine> getOrdini() {
		return ordini;
	}

	public String getUsername() {
		return username;
	}

	public String getHash256_pw() {
		return Hash256_pw;
	}

	public String getIdFarmacia() {
		return idFarmacia;
	}
	
	
	public int getTentativiLogin() {
		return tentativiLogin;
	}

	public String getTipo_account() {
		return tipo_account;
	}

	public String getNomeFarmacia() {
		return nomeFarmacia;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public Catalogo getC() {
		return c;
	}

	public String getTokenAUTH() {
		return tokenAUTH;
	}
	
	public int getCAP() {
		return CAP;
	}
	
	public boolean isDisabilitato() {
		return disabilitato;
	}
	
	public int loginCredenzialiErrore()
	{
		this.tokenAUTH = null;
		
		if(this.tentativiLogin == 5)
		{
			this.disabilitato = true;
			return -1;
		}
		else
		{
			this.tentativiLogin ++;
			return this.tentativiLogin;
		}
	}
	
	public void loginCorretto(String token)
	{
		this.tentativiLogin = 0;
		//Generiamo TOKEN AUTH che sarà valido esclusivamente per intera sessione
		this.tokenAUTH = token;
		
	}
	
	public void logout()
	{
		this.tokenAUTH = null;
	}
	
	
	//Torna la lista di ordini cliente che non sono completati
	public ArrayList<Ordine> listaOrdiniCliente(String cf)
	{
		ArrayList<Ordine> result = new ArrayList<Ordine>();
		
		for(Ordine o : this.ordini)
		{
			if(o.getCodiceFiscale().equals(cf) == true && o.isDefinitivo() == false)
			{
				result.add(o);
			}
		}
		
		return result;
	}
	
	public boolean controllaIdOrdine(String idOrdine)
	{
		for(Ordine o : this.getOrdini())
		{
			if(o.getIdOrdine().equals(idOrdine) == true)
			{
				return true;
			}
		}
		return false;
	}
	
	//Dato un cf restituisce il numero di ordini che quel codice fiscale ha effettuato e SONO IN STATO 'CREATO'
	private int controllaNumeroOrdiniByCF(String cf)
	{
		int conta = 0;
		for(Ordine o : this.ordini)
		{
			if(o.getCodiceFiscale().equals(cf) == true && o.isDefinitivo() == false)
			{
				conta ++;
			}
		}
		return conta;
	}
	
	//Controllo se idFarmaco esiste
	private boolean checkIdFarmacoEsistente(String idFarmaco)
	{
		for(Farmaco f : this.getC().getCatalogo() )
		{
			if(f.getIdFarmaco().equals(idFarmaco) == true)
			{
				return true;
			}
		}
		return false;
	}
	
	public int completaOrdine(String idOrdine)
	{
		for(Ordine e : this.getOrdini())
		{
			if(e.getIdOrdine().equals(idOrdine) == true)
			{
				if(e.isDefinitivo() == true)
				{
					//Gia completato
					return -2;
				}
				else
				{
					e.setDefinitivo(true);
					return 0;
				}
			}
		}
		//Non trovo num ordine
		return -1;
	}
	
	public int aggiungiOrdine(String codiceFiscale, String idFarmaco, int quantitaRichiesta, long timestampRitiroFarmaco)
	{
		//Controllo se utente ha piu di 5 ordini creati simultaneamente
		if(this.controllaNumeroOrdiniByCF(codiceFiscale) >= 5)
		{
			return -1;
		}
		
		if(this.checkIdFarmacoEsistente(idFarmaco) == false)
		{
			return -2;
		}
		
		boolean dispImmediata = true;
		
		//Tolgo i farmaci dal catalogo
		for(Farmaco f : this.c.getCatalogo())
		{
			if(f.getIdFarmaco().equals(idFarmaco) == true)
			{
				if(f.getQuantitaDisponibile() >= quantitaRichiesta)
				{
					f.setQuantitaDisponibile(f.getQuantitaDisponibile() - quantitaRichiesta);
				}
				else
				{
					f.setQuantitaDisponibile(0);
					dispImmediata = false;
				}

			}
		}
		
		String idO = "Ordine" + (this.getOrdini().size() + 1);
		Ordine nuovoOrdine = new Ordine(this.getIdFarmacia(),idO, codiceFiscale, idFarmaco, quantitaRichiesta,timestampRitiroFarmaco, dispImmediata );
		this.ordini.add(nuovoOrdine);
		return 1;
	}
	
	public int rimuoviOrdine(String idOrdine, String codiceFiscale)
	{
		if(this.controllaIdOrdine(idOrdine) == true)
		{
			for(int i = 0; i < this.ordini.size(); i++)
			{
				if(this.ordini.get(i).getIdOrdine().equals(idOrdine) == true)
				{
					//Cf corrispondente
					if(this.ordini.get(i).getCodiceFiscale().equals(codiceFiscale) == true)
					{
						//Era in disponibilita immediata
						if(this.ordini.get(i).isDisponibilitaImmediata() == true)
						{
							//Rimetto lo stock in catalogo
							for(Farmaco f : this.getC().getCatalogo())
							{
								if(f.getIdFarmaco().equals(this.ordini.get(i).getIdFarmaco()) == true)
								{
									f.setQuantitaDisponibile(this.ordini.get(i).getQuantitaRichiesta());
								}
							}
						}
						
						//In ogni caso, rimuovo ordine
						this.ordini.remove(i);
						return 1;
					}
					else
					{
						//cf non corrispondente
						return -1;
					}
				}
			}

		}
		//Ordine NON esistente
		return -2;
	}
	
	public ArrayList<Ordine> ordiniByCF(String cf)
	{
		ArrayList<Ordine> result = new ArrayList<Ordine>();
		
		for(Ordine o : this.getOrdini())
		{
			if(o.getCodiceFiscale().equals(cf) == true)
			{
				result.add(o);
			}
		}
		
		return result;
	}
	
	/*------------------------------METODI SET------------------------------*/
	

	public void setOrarioApertura(String[] orarioApertura) {
		this.orarioApertura = orarioApertura;
	}
	
	public void setIdFarmacia(String idFarmacia) {
		this.idFarmacia = idFarmacia;
	}
	
	
	/*------------------------------ToString------------------------------*/
	
	public String OrdiniToJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getOrdini());
		return jsonString;
	}
	
	public String toJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		return jsonString;
	}

	
	@Override
	public String toString() {
		return "GestoreFarmacia [username=" + username + ", Hash256_pw=" + Hash256_pw + ", idFarmacia=" + idFarmacia
				+ ", nomeFarmacia=" + nomeFarmacia + ", indirizzo=" + indirizzo + ", orarioApertura="
				+ Arrays.toString(orarioApertura) + ", c=" + c + ", tokenAUTH=" + tokenAUTH + "]";
	}
	
	
}
