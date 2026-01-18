package main.java.Model;

import main.java.Komunikacja.Widok;

public class DAO implements IDAO {

	public DAO() {
		// TODO - implement DAO.DAO
	}


	public boolean uwierzytelnienieKlienta(int nrKarty, int pin) {
		boolean stanUwierzytelnienia = (nrKarty == 111111 && pin == 1234);
		Widok.wyswietlanie("DAO", "uwierzytelnienieKlienta", stanUwierzytelnienia, "Weryfikacja w banku: " + (stanUwierzytelnienia ? "Uwierzytelniono" : "Odmowa"));
		return stanUwierzytelnienia;

	}

	public boolean uwierzytelnieniePracownika(int nrKarty, int pin) {
		boolean stanUwierzytelnienia = ((nrKarty == 111111 || nrKarty == 999999) && pin == 1234);
		String status = stanUwierzytelnienia ? "Uwierzytelniono" : "Odmowa";
		Widok.wyswietlanie("DAO", "uwierzytelnieniePracownika", stanUwierzytelnienia, "Weryfikacja pracownika: " + status);
		return stanUwierzytelnienia;
	}

	public int pobranieStanuGotowki() {
		return 0;
	}

	public void aktualizacjaStanuGotowki(int kwota) {

	}

	public boolean zaksiegowanieOperacji(String dane) {
		return true;
	}

	public String[] pobranieLogow() {
		return new String[0];
	}

	public void zapisanieLogow(String informacje) {
		Widok.wyswietlanie("DAO", "zapisanieLogow", true, "LOG: " + informacje);
	}
	public boolean weryfikacjaTransakcjiWBanku(int kwota, int nrKarty) {
		boolean ok = (kwota > 0 && nrKarty > 0); // prosta reguła „bank akceptuje”
		Widok.wyswietlanie("DAO", "weryfikacjaTransakcjiWBanku", ok,
				"Autoryzacja transakcji: kwota=" + kwota + ", karta=" + nrKarty);
		return ok;
	}

}