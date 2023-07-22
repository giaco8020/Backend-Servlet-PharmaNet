package beans;

public abstract class Impegno {
    
    private TipoImpegno tipo;
    private long timestampImpegno;
    private String codiceFiscale;
    private boolean definitivo;

    public Impegno(TipoImpegno tipo, String codiceFiscale, boolean definitivo) {
        this.tipo = tipo;
        this.codiceFiscale = codiceFiscale;
        this.definitivo = definitivo;
        this.timestampImpegno = Utils.getCurrentTimestamp();
    }

    public TipoImpegno getTipo() {
        return tipo;
    }

    public long getTimestampImpegno() {
        return timestampImpegno;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public boolean isDefinitivo() {
        return definitivo;
    }

    public void setDefinitivo(boolean definitivo) {
        this.definitivo = definitivo;
    }
}