package main.java.Kontroler;
import main.java.Komunikacja.Widok;
public class DrukowaniePotwierdzenia {

	private String dataIGodzina;
	private int nrKonta;
	private String daneKlienta;
	private int stanKontaPrzed;
	private int stanKontaPo;
	private String rodzajOperacji;
	private String tytulPrzelewu;
	private int nrKontaAdresata;
	private String nazwaAdresata;
	private String typOperacji;


	public DrukowaniePotwierdzenia(String data, int nrKonta, String daneKlienta,
								   int stanPrzed, int stanPo, String rodzaj,
								   String tytul, int nrAdresata, String nazwaAdresata,
								   String typ) {
		this.dataIGodzina = data;
		this.nrKonta = nrKonta;
		this.daneKlienta = daneKlienta;
		this.stanKontaPrzed = stanPrzed;
		this.stanKontaPo = stanPo;
		this.rodzajOperacji = rodzaj;
		this.tytulPrzelewu = tytul;
		this.nrKontaAdresata = nrAdresata;
		this.nazwaAdresata = nazwaAdresata;
		this.typOperacji = typ;
	}

	public void drukowanie() {
		Widok.wyswietlanie("DrukowaniePotwierdzenia", "drukowanie", true, "Generowanie potwierdzenia");
		System.out.println("POTWIERDZENIE");
		System.out.println("Data: " + dataIGodzina);
		System.out.println("Typ: " + rodzajOperacji);
		System.out.println("Klient: " + daneKlienta);
		System.out.println("Konto: " + nrKonta);
		if (nrKontaAdresata != 0) {
			System.out.println("Odbiorca: " + nazwaAdresata);
			System.out.println("Rachunek: " + nrKontaAdresata);
			System.out.println("Tytuł: " + tytulPrzelewu);
		}
		System.out.println("Saldo przed: " + stanKontaPrzed + " PLN");
		int kwotaOperacji = Math.abs(stanKontaPo - stanKontaPrzed);
		System.out.println("KWOTA: " + kwotaOperacji + " PLN");
		System.out.println("Saldo po: " + stanKontaPo + " PLN");
	}
}

