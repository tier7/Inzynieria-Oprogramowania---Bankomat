package main.java.Kontroler;

import main.java.Komunikacja.Widok;
public class PrzekazywanieInformacjiUzytkownikowi {

	public void przekazanieInformacji(String komunikat) {
		Widok.wyswietlanie("PrzekazywanieInformacjiUzytkownikowi", "przekazanieInformacji", true, komunikat);

	}

}