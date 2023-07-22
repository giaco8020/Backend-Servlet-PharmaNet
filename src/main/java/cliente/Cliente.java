package cliente;

import com.google.gson.Gson;

public class Cliente {
	
	private String email;
	private String nome;
	private String cognome;
	private char sesso;
	private String dataNascita;
	private String codiceFiscale;
	private String url_immagine;
	private boolean disabilitato;
	private String Hash256_pw;
	private int tentativiLogin; //Dopo 5 tentativi l'account viene bloccato
	private String tokenAUTH; //Stringa che identifica login 
	private String tipo_account; 
	
	public Cliente(String email, String nome, String cognome, char sesso, String dataNascita, String codiceFiscale, String hashPw) 
	{
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		//if(sesso != 'M' && sesso != 'F' )
		
		this.sesso = sesso;
		this.dataNascita = dataNascita;
		this.codiceFiscale = codiceFiscale;
		this.url_immagine = null;
		this.disabilitato = false;
		this.Hash256_pw = hashPw;
		this.tentativiLogin = 0;
		this.tokenAUTH = null;
		this.tipo_account = null;
	}
	
	/* ----------------------------    Metodi GET   ------------------------------------ */
	
	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public char getSesso() {
		return sesso;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public String getHashPw()
	{
		return this.Hash256_pw;
	}
	
	public boolean isDisabilitato() {
		return disabilitato;
	}

	public int getTentativiLogin() {
		return tentativiLogin;
	}
	
	

	public void setTipo_account(String tipo_account) {
		this.tipo_account = tipo_account;
	}

	public String getTipo_account() {
		return tipo_account;
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
	
	public String getAuthToken()
	{
		return this.tokenAUTH;
	}
	
	public void logout()
	{
		this.tokenAUTH = null;
	}
	
	public void loginCorretto(String token)
	{
		this.tentativiLogin = 0;
		//Generiamo TOKEN AUTH che sarà valido esclusivamente per intera sessione
		this.tokenAUTH = token;
		
	}
	
	public void RegistrazioneCompletata()
	{
		this.url_immagine = null;
		this.disabilitato = false;
		this.tentativiLogin = 0;
		this.tokenAUTH = null;
		this.setTipo_account("utente");
	}
	
	
	/* ----------------------------    Metodi SET   ------------------------------------ */
	
	public void setUrl_immagine(String url_immagine) {
		this.url_immagine = url_immagine;
	}
	
	public void setDisabilitato(boolean b)
	{
		this.disabilitato = b;
	}

	
	/* ----------------------------    Metodi toString   ------------------------------------ */
	
	public String toJson()
	{
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		return jsonString;
	}

	@Override
	public String toString() {
		return "Cliente [email=" + email + ", nome=" + nome + ", cognome=" + cognome + ", sesso=" + sesso
				+ ", dataNascita=" + dataNascita + ", codiceFiscale=" + codiceFiscale + ", url_immagine=" + url_immagine
				+ ", disabilitato=" + disabilitato + ", Hash256_pw=" + Hash256_pw + ", tentativiLogin=" + tentativiLogin
				+ ", tokenAUTH=" + tokenAUTH + ", tipo_account=" + tipo_account + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
}
