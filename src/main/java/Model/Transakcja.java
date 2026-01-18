package main.java.Model;

public class Transakcja implements ITransakcja {

	private String dataIGodzina;
	private String typOperacji;
	private int kwota;
	private int nrKontaNadawcy;
	private String tytulPrzelewu;
	private int nrKontaAdresata;
	private String nazwaAdresata;

	public Transakcja(String data, String typ, int kwota, int nadawca, int adresat, String nazwaAdresata, String tytul) {
		this.dataIGodzina = data;
		this.typOperacji = typ;
		this.kwota = kwota;
		this.nrKontaNadawcy = nadawca;
		this.nrKontaAdresata = adresat;
		this.nazwaAdresata = nazwaAdresata;
		this.tytulPrzelewu = tytul;
	}

	public String pobranieDanych() {
		return "[Data=" + dataIGodzina + ", Typ=" + typOperacji + ", Kwota=" + kwota
				+ " PLN, Konto:" + nrKontaNadawcy + " -> Konto:" + nrKontaAdresata + "]";
	}

}