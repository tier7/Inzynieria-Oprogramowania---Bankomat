package Model;

import Komunikacja.Widok;

import java.util.Map;

public class Model implements IModel {

	private IDAO dao;
	private RejestrTransakcji rejestr;
	private Sejf sejf;

	public Model(IDAO dao, RejestrTransakcji rejestr, Sejf sejf) {
		this.dao = dao;
		this.rejestr = rejestr;
		this.sejf = sejf;
	}

	@Override
	public boolean logowanieKlient(int nrKarty, int pin) {
		boolean czyZalogowano = dao.uwierzytelnienieKlienta(nrKarty, pin);
		return czyZalogowano;
	}

	@Override
	public boolean logowaniePracownik(int nrKarty, int pin) {
		boolean czyZalogowano = dao.uwierzytelnieniePracownika(nrKarty, pin);
		return czyZalogowano;
	}

	@Override
	public boolean sprawdzenieMiejscaNaGotowke(Map<Integer, Integer> banknoty) {
		int iloscNowych = 0;
		if (banknoty != null) {
			for (int szt : banknoty.values()) {
				iloscNowych = dodajSzt(iloscNowych, szt);
			}
		}
		return sejf.czyMiejsce(iloscNowych);
	}



	@Override
	public boolean weryfikacjaTransakcjiWBanku(int kwota, int nrKarty) {
		Widok.wyswietlanie("Model", "weryfikacjaTransakcjiWBanku", true,
				"Deleguję autoryzację do DAO");
		boolean czyZweryfikowano = dao.weryfikacjaTransakcjiWBanku(kwota, nrKarty);
		return czyZweryfikowano;
	}



	public void ksiegowanieWplaty(int kwota, int nrKarty, boolean potwierdzenie,
								  Map<Integer, Integer> banknoty) {
		sejf.aktualizacjaStanu(banknoty);
		String daneBazowe = "Data=2025-12-14;Typ=WPLATA;Kwota=" + kwota
				+ ";Nadawca=" + nrKarty + ";Adresat=0;NazwaAdresata=Wlasny"
				+ ";Tytul=Wplata_wlasna";
		rejestr.zapisTransakcji(daneBazowe, potwierdzenie);
		this.rejestracjaZdarzenia("Zaksięgowano wpłatę " + kwota + " dla karty " + nrKarty);
	}

	@Override
	public String[] pobranieWszystkichTransakcji() {
		return rejestr.pobranieWszystkichTransakcji();
	}

	@Override
	public void rejestracjaZdarzenia(String informacje) {
		dao.zapisanieLogow(informacje);
	}

	private int dodajSzt(int suma, int szt) {
		return suma + szt;
	}

}