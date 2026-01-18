package Kontroler;

import Komunikacja.Widok;
import Model.IModel;

public class KontrolerPracownika implements IKontrolerPracownika {

	private final IModel model;

	public KontrolerPracownika(IModel model) {
		this.model = model;
	}

	@Override
	public void aktualizacjaZawartosciGotowki() {
		Widok.wyswietlanie("KontrolerPracownika", "aktualizacjaZawartosciGotowki", true,
				"TODO: do implementacji (na razie bez crasha)");
	}


	@Override
	public void pobranieDanychOTransakcjach(int nrKarty, int pin) {
		Widok.wyswietlanie("KontrolerPracownika", "pobranieDanychOTransakcjach", true,
				"Wybrano opcję 'Pobranie danych o transakcjach'.");

		new PobranieDanychOTransakcjach(model, nrKarty, pin);
	}


	@Override
	public void uwierzytelnianiePracownika(int nrKarty, int pin) {
		Widok.wyswietlanie("KontrolerPracownika", "uwierzytelnianiePracownika", true,
				"Weryfikacja uprawnień pracownika");
		model.logowaniePracownik(nrKarty, pin);
	}
}
