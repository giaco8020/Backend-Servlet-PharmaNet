package gestoreClinica;

import java.time.LocalDateTime;

public class Visita {
	
	private String idVisita; //Numero progressivo 'VisitaNUM'
	private String tipologia;
	private String medico;
	private LocalDateTime dataOra;
	private boolean prenotata;
	
	public Visita(String idVisita, String tipologia, String medico, LocalDateTime dataOra) {
		this.idVisita = idVisita;
		this.tipologia = tipologia;
		this.medico = medico;
		this.dataOra = dataOra;
		this.prenotata = false;
	}

	/*---------------------------------METODI GET---------------------------*/
	
	public boolean isPrenotata() {
		return prenotata;
	}

	public String getIdVisita() {
		return idVisita;
	}

	public String getTipologia() {
		return tipologia;
	}

	public String getMedico() {
		return medico;
	}

	public LocalDateTime getDataOra() {
		return dataOra;
	}
	
	
	/*---------------------------------METODI SET------------------------------*/

	public void setPrenotata(boolean prenotata) {
		this.prenotata = prenotata;
	}
	
	
	
}
