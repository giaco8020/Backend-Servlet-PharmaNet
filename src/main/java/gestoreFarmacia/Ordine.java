package gestoreFarmacia;

import beans.Impegno;
import beans.TipoImpegno;

public class Ordine extends Impegno 
{
	private String idFarmacia;
	private String idOrdine;
    private String idFarmaco;
    private int quantitaRichiesta;
    private long timestampRitiroFarmaco;
    private boolean disponibilitaImmediata;

    public Ordine(String idFarmacia,String idOrdine, String codiceFiscale, String idFarmaco, int quantitaRichiesta, long timestampRitiroFarmaco, boolean disponibilitaImmediata ) 
    {
        super(TipoImpegno.ORDINE, codiceFiscale, false);
        this.idFarmacia = idFarmacia;
        this.idOrdine = idOrdine;
        this.idFarmaco = idFarmaco;
        this.quantitaRichiesta = quantitaRichiesta;
        this.timestampRitiroFarmaco = timestampRitiroFarmaco;
        this.disponibilitaImmediata = disponibilitaImmediata;
    }
    
    public String getIdFarmacia() {
		return idFarmacia;
	}

	public String getIdOrdine() {
		return idOrdine;
	}

	public String getIdFarmaco() {
        return idFarmaco;
    }

    public int getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public long getTimestampRitiroFarmaco() {
        return timestampRitiroFarmaco;
    }

	public boolean isDisponibilitaImmediata() {
		return disponibilitaImmediata;
	}

	public void setDisponibilitaImmediata(boolean disponibilitaImmediata) {
		this.disponibilitaImmediata = disponibilitaImmediata;
	}
    
}
