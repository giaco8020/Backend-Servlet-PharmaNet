package gestoreFarmacia;

public class Farmaco {
	
	private String idFarmaco; // idFarmaco = progressivo 'FarmacoNUM'
	private String nome;
	private double prezzo;
	private int quantitaDisponibile;
	
	public Farmaco(String idFarmaco, String nome, double prezzo, int quantitaDisponibile) 
	{
		this.idFarmaco = idFarmaco;
		this.nome = nome;
		this.prezzo = prezzo;
		this.quantitaDisponibile = quantitaDisponibile;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public int getQuantitaDisponibile() {
		return quantitaDisponibile;
	}

	public String getIdFarmaco() {
		return idFarmaco;
	}

	public String getNome() {
		return nome;
	}
	
	
	/*			FUNZIONI SET MODIFICA FARMACO 					*/
	
	public void setQuantitaDisponibile(int quantitaDisponibile) {
		this.quantitaDisponibile = quantitaDisponibile;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}
	
	
}
