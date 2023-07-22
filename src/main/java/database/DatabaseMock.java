package database;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.Gson;

import cliente.Cliente;
import gestoreClinica.GestoreClinica;
import gestoreFarmacia.GestoreFarmacia;


public class DatabaseMock 
{
	private ArrayList<Cliente> clientiRegistrati;
	private ArrayList<GestoreFarmacia> farmacieRegistrate;
	private ArrayList<GestoreClinica> clinicheRegistrate;
	
	public DatabaseMock() 
	{
		
		String[] arr = {"09-19","09-19","09-19","01-19","09-19","09-19","09-19"};
		String[] arr1 = {"01-12","01-12","01-12","01-12","01-12","01-12","01-12"};
		
		/*-----------------------------FARMACIE-----------------------------------*/
			
			this.farmacieRegistrate = new ArrayList<GestoreFarmacia>();
		
			
			GestoreFarmacia g1 = new GestoreFarmacia("Farmacia1", "a280ed8eaea640bdfef2612ab561d183d51514349b342fe47ba05aefa2a2878a", "Farmacia1", "Via fratelli cervi 7", arr , 40051);
			GestoreFarmacia g2 = new GestoreFarmacia("Farmacia2", "213a2cbfcff10154354eab9de26a38234733edbb5abb53edbb25f2aa87a3cbac", "Farmacia2", "Via 4 novembre 4", arr1 , 40016);
			GestoreFarmacia g3 = new GestoreFarmacia("Farmacia3", "88517d94fb44dedf122dcf85fed03bb5c7a5b315ae86f8bb975086880bfca5ea", "Farmacia3", "Via luigi gardi 10B", arr , 40051);

			
			g1.getC().aggiungiFarmaco("Tachipirina",6.99,5);
			g1.getC().aggiungiFarmaco("Brufen 400",3.99,15);
			g1.getC().aggiungiFarmaco("Moment",3.99,0);
			g1.getC().aggiungiFarmaco("Benagol",3.99,0);
			
			g3.getC().aggiungiFarmaco("Tachipirina",6.99,5);
			g3.getC().aggiungiFarmaco("Imodium",4.59,0);
			
			g2.getC().aggiungiFarmaco("Benagol",7.00,0);
			
			this.aggiungiGestoreFarmacia(g1);
			this.aggiungiGestoreFarmacia(g2);
			this.aggiungiGestoreFarmacia(g3);
		
		/*-----------------------------CLINICHE------------------------------------*/
			
			this.clinicheRegistrate = new ArrayList<GestoreClinica>();
			
			GestoreClinica c1 = new GestoreClinica("Clinica1", "a280ed8eaea640bdfef2612ab561d183d51514349b342fe47ba05aefa2a2878a", "Clinica1","Via talamini 4" ,40051, arr);
			GestoreClinica c2 = new GestoreClinica("Clinica2", "213a2cbfcff10154354eab9de26a38234733edbb5abb53edbb25f2aa87a3cbac", "Clinica2","Via piero 3" ,40016, arr);
			
			c1.getC().aggiungiVisita("Controllo Generale", "Pino D'angelo", LocalDateTime.of(2023, 6, 23, 15, 0));
			c1.getC().aggiungiVisita("Visita Medico Sportiva", "Vasco Rossi", LocalDateTime.of(2023, 6, 27, 10, 45));
			
			c2.getC().aggiungiVisita("Magnetoterapia", "Vasco Rossi", LocalDateTime.of(2023, 6, 28, 16, 45));

			
			this.aggiungiGestoreClinica(c1);
			this.aggiungiGestoreClinica(c2);
			
		
		
		this.clientiRegistrati = new ArrayList<Cliente>();
	}
	
	/*-----------------------------------Metodi CLIENTE---------------------------------------*/
	
	public Cliente getClienteByEmail(String email)
	{
		for(Cliente c : this.getClientiRegistrati())
		{
			if(c.getEmail().equals(email) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public Cliente getClienteByCV(String cv)
	{
		for(Cliente c : this.getClientiRegistrati())
		{
			if(c.getCodiceFiscale().equals(cv) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public Cliente getClienteByAuth(String auth)
	{
		for(Cliente c : this.getClientiRegistrati())
		{
			if(c.getAuthToken() != null  && c.getAuthToken().equals(auth) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public ArrayList<Cliente> getClientiRegistrati() 
	{
		return this.clientiRegistrati;
	}
	
	public String getClientiRegistratiJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getClientiRegistrati());
		return jsonString;
	}
	
	
	/*-----------------------------------Metodi FARMACIE---------------------------------------*/
	
	public GestoreFarmacia getFarmaciaById(String id)
	{
		for(GestoreFarmacia c : this.getFarmacieRegistrate())
		{
			if(c.getIdFarmacia().equals(id) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public GestoreFarmacia getFarmaciaByAuth(String auth)
	{
		for(GestoreFarmacia c : this.getFarmacieRegistrate())
		{
			if(c.getTokenAUTH().equals(auth) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public GestoreFarmacia getFarmaciaByUsernameHashPw(String username, String hashPw)
	{
		for(GestoreFarmacia gf : this.getFarmacieRegistrate())
		{
			if(gf.getUsername().equals(username) && gf.getHash256_pw().equals(hashPw) == true)
			{
				return gf;
			}
		}
		return null;
	}
	
	public ArrayList<GestoreFarmacia> getFarmacieRegistrate()
	{
		return this.farmacieRegistrate;
	}
	
	public ArrayList<GestoreFarmacia> getFarmacieByCAP(int capRicerca)
	{
		ArrayList<GestoreFarmacia> result = new ArrayList<GestoreFarmacia>();
		
		for(GestoreFarmacia gf : this.getFarmacieRegistrate())
		{
			if(gf.getCAP() == capRicerca)
			{
				result.add(gf);
			}
		}
		
		return result;
	}


	public String getFarmacieByCAPJson(int capRicerca)
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getFarmacieByCAP(capRicerca));
		return jsonString;
	}
	
	public String getFarmacieRegistrateJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getFarmacieRegistrate());
		return jsonString;
	}
	
	public void aggiungiGestoreFarmacia(GestoreFarmacia gf)
	{
		String id = "GF" + this.farmacieRegistrate.size() + 1;
		gf.setIdFarmacia(id);
		this.farmacieRegistrate.add(gf);
	}
	
	/*-----------------------------------Metodi CLINICHE---------------------------------------*/
	
	public GestoreClinica getClinicaById(String id)
	{
		for(GestoreClinica c : this.getClinicheRegistrate())
		{
			if(c.getIdClinica().equals(id) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public GestoreClinica getClinicaByAuth(String auth)
	{
		for(GestoreClinica c : this.getClinicheRegistrate())
		{
			if(c.getTokenAUTH().equals(auth) == true)
			{
				return c;
			}
		}
		return null;
	}
	
	public GestoreClinica getClinicaByUsernameHashPw(String username, String hashPw)
	{
		for(GestoreClinica gc : this.getClinicheRegistrate())
		{
			if(gc.getUsername().equals(username) && gc.getHash256_pw().equals(hashPw) == true)
			{
				return gc;
			}
		}
		return null;
	}
	
	
	public ArrayList<GestoreClinica> getClinicheRegistrate()
	{
		//this.eliminaVisitePassate();
		return this.clinicheRegistrate;
	}
	
	public ArrayList<GestoreClinica> getClinicheByCAP(int capRicerca)
	{
		ArrayList<GestoreClinica> result = new ArrayList<GestoreClinica>();
		
		for(GestoreClinica gc : this.getClinicheRegistrate())
		{
			if(gc.getCAP() == capRicerca)
			{
				result.add(gc);
			}
		}
		
		return result;
	}


	public String getClinicheByCAPJson(int capRicerca)
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getClinicheByCAP(capRicerca));
		return jsonString;
	}
	
	public String getClinicheRegistrateJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.getClinicheRegistrate());
		return jsonString;
	}
	
	public void aggiungiGestoreClinica(GestoreClinica gc)
	{
		String id = "GC" + this.getClinicheRegistrate().size() + 1;
		gc.setIdClinica(id);
		this.clinicheRegistrate.add(gc);
	}
	
	
}
