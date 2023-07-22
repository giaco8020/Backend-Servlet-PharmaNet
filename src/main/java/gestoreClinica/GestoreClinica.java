package gestoreClinica;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;


public class GestoreClinica {
	
	private String username;
	private String Hash256_pw;
	
	private String idClinica;
	private String nomeClinica;
	private String indirizzo;
	private int CAP;
	private String[] orarioApertura;
	private Catalogo c;
	
	private boolean disabilitato;
	private int tentativiLogin;
	private String tipo_account;
	
	private String tokenAUTH;
	
	private ArrayList<Prenotazione> prenotazioni;

	public GestoreClinica(String username, String hash256_pw, String nomeClinica, String indirizzo,int cAP, String[] orarioApertura) 
	{
		this.username = username;
		this.Hash256_pw = hash256_pw;
		this.idClinica = null;
		this.nomeClinica = nomeClinica;
		this.indirizzo = indirizzo;
		this.CAP = cAP;
		this.orarioApertura = orarioApertura;
		this.c = new Catalogo();
		
		this.disabilitato = false;
		this.tentativiLogin = 0;
		this.tipo_account = "clinica";
		
		this.tokenAUTH = null;
		
		this.prenotazioni = new ArrayList<Prenotazione>();
	}

	/*------------------------------------------METODI GET----------------------------------*/
	
	public String getIdClinica() {
		return idClinica;
	}

	public String[] getOrarioApertura() {
		return orarioApertura;
	}

	public boolean isDisabilitato() {
		return disabilitato;
	}

	public int getTentativiLogin() {
		return tentativiLogin;
	}

	public String getUsername() {
		return username;
	}

	public String getHash256_pw() {
		return Hash256_pw;
	}

	public String getNomeClinica() {
		return nomeClinica;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public int getCAP() {
		return CAP;
	}

	public Catalogo getC() {
		return c;
	}

	public String getTipo_account() {
		return tipo_account;
	}

	public String getTokenAUTH() {
		return tokenAUTH;
	}

	public ArrayList<Prenotazione> getPrenotazioni() {
		return prenotazioni;
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
	
	private boolean contrallaIdVisita(String idVisita)
	{
		for(Visita p : this.getC().getCatalogo())
		{
			if(p.getIdVisita().equals(idVisita) == true)
			{
				return true;
			}
		}
		return false;
	}
	
	public int prenotaVisita(String idVisita, String codiceFiscale)
	{
		//Controllo idVisita esistente 
		if(this.contrallaIdVisita(idVisita) == false)
		{
			return -1;
		}
		
		for(Visita p : this.getC().getCatalogo())
		{
			if(p.getIdVisita().equals(idVisita) == true)
			{
				if(p.isPrenotata() == true)
				{
					//Visita gia prenotata
					return -2;
				}
				else
				{
					//Prenoto visita
					p.setPrenotata(true);
					
					//Inserire prenotazione nei sistemi
					String s = "Prenotazione" + this.getPrenotazioni().size() +1;
					Prenotazione pr = new Prenotazione(s, codiceFiscale, idVisita);
					this.getPrenotazioni().add(pr);
					return 1;
				}
			}
		}
		return -3;
	}
	
	public int annullaVisita(String idPrenotazione, String idVisita, String codiceFiscale)
	{
		//Controllo idVisita esistente 
		if(this.contrallaIdVisita(idVisita) == false)
		{
			return -1;
		}
		
		for(int i = 0; i < this.getC().getCatalogo().size(); i++)
		{
			if(this.getC().getCatalogo().get(i).getIdVisita().equals(idVisita) == true)
			{
				if(this.getC().getCatalogo().get(i).isPrenotata() == false)
				{
					//Errore -- visita NON prenotata
					return -2;
				}
				else
				{
					//Elimino prenotazione
					this.getC().getCatalogo().get(i).setPrenotata(false);
					
					for(int j = 0; j < this.getPrenotazioni().size(); j++)
					{
						if(this.getPrenotazioni().get(j).getIdPrenotazione().equals(idPrenotazione) == true)
						{
							if(this.getPrenotazioni().get(j).getCodiceFiscale().equals(codiceFiscale) == true)
							{
								//Rimuovo
								this.getPrenotazioni().remove(j);
								return 1;
							}
							else
							{
								//NON HAI I PERMESSI PER RIMUOVERE
								return -3;
							}
						}
					}
					
				}
			}
		}
		
		return -4;
		
	}
	
	public int completaVisita(String idPrenotazione)
	{
		for(Prenotazione p : this.getPrenotazioni())
		{
			if(p.getIdPrenotazione().equals(idPrenotazione) == true)
			{
				if(p.isDefinitivo() == true)
				{
					return -1;
				}
				else
				{
					p.setDefinitivo(true);
					return 1;
				}
			}
		}
		return -2;
	}
	
	public ArrayList<Prenotazione> prenotazioniByCF(String cf)
	{
		ArrayList<Prenotazione> result = new ArrayList<Prenotazione>();
		
		for(Prenotazione p : this.getPrenotazioni())
		{
			if(p.getCodiceFiscale().equals(cf) == true)
			{
				result.add(p);
			}
		}
		
		return result;
	}
	
	/*------------------------------------------METODI SET----------------------------------*/
	
	public void setIdClinica(String idClinica) {
		this.idClinica = idClinica;
	}
	
	public void setOrarioApertura(String[] orarioApertura) {
		this.orarioApertura = orarioApertura;
	}
	
	/*------------------------------ToString------------------------------------------------*/
	
	
	public String PrenotazioniToJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getPrenotazioni());
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
		return "GestoreClinica [username=" + username + ", Hash256_pw=" + Hash256_pw + ", idClinica=" + idClinica
				+ ", nomeClinica=" + nomeClinica + ", indirizzo=" + indirizzo + ", CAP=" + CAP + ", orarioApertura="
				+ Arrays.toString(orarioApertura) + ", c=" + c + ", disabilitato=" + disabilitato + ", tentativiLogin="
				+ tentativiLogin + ", tipo_account=" + tipo_account + ", tokenAUTH=" + tokenAUTH + ", prenotazioni="
				+ prenotazioni + "]";
	}
	
	
}
