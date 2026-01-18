package Kontroler;

import Komunikacja.Widok;
import Model.*;

import java.util.HashMap;
import java.util.Map;

public class SystemZarządzaniaBankomatem {

	public static void main(String[] args) {

		int nrKartyKlienta = 111111;
		int pinKlienta = 1234;

		int nrKartyPracownika = 999999;
		int pinPracownika = 1234;

		Map<Integer, Integer> startowyStanGotowki = new HashMap<>();
		startowyStanGotowki.put(100, 50);
		startowyStanGotowki.put(50, 100);
		startowyStanGotowki.put(20, 10);

		IDAO dao = new DAO();
		Sejf sejf = new Sejf(startowyStanGotowki);
		RejestrTransakcji rejestr = new RejestrTransakcji(dao);
		IModel model = new Model(dao, rejestr, sejf);

		IKontrolerKlienta kontrolerKlienta = new KontrolerKlienta(model);
		IKontrolerPracownika kontrolerPracownika = new KontrolerPracownika(model);

		Widok.wyswietlanie("System", "main", true, "TEST WplataPieniedzy");
		kontrolerKlienta.wplataPieniedzy(nrKartyKlienta, pinKlienta);
		Widok.wyswietlanie("System", "main", true, "KONIEC TESTU WplataPieniedzy");

		Widok.wyswietlanie("System", "main", true, "TEST PobranieDanychOTransakcjach");
		kontrolerPracownika.pobranieDanychOTransakcjach(nrKartyPracownika, pinPracownika);
		Widok.wyswietlanie("System", "main", true, "KONIEC TESTU PobranieDanychOTransakcjach");

		Widok.wyswietlanie("System", "main", true, "Zakonczenie dzialania");
	}
}
