package Kontroler;

import Komunikacja.Widok;
public class PrzekazywanieInformacjiUzytkownikowi {

	public void przekazanieInformacji(String komunikat) {
		Widok.wyswietlanie("PrzekazywanieInformacjiUzytkownikowi", "przekazanieInformacji", true, komunikat);

	}

}