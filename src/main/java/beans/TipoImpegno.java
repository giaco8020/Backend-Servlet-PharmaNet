package beans;

public enum TipoImpegno 
{
    ORDINE("Farmaco"),
    PRENOTAZIONE("Visita");

    private final String descrizione;

    TipoImpegno(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}