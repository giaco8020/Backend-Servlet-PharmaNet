package gestoreClinica;

import beans.Impegno;
import beans.TipoImpegno;

public class Prenotazione extends Impegno {
	
	private String idVisita;
	private String idPrenotazione;
	
    public Prenotazione(String idPrenotazione, String codiceFiscale, String idVisita) {
        super(TipoImpegno.PRENOTAZIONE, codiceFiscale, false);
        this.idVisita = idVisita;
        this.idPrenotazione = idPrenotazione;
    }
        
    public String getIdPrenotazione() {
		return idPrenotazione;
	}

	public String getIdVisita() {
        return idVisita;
    }
}
