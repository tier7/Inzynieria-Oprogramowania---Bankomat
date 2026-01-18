package main.java.Kontroler;

import main.java.Model.IModel;
import main.java.Komunikacja.Widok;


public class KontrolerKlienta implements IKontrolerKlienta {

	private final IModel model;

	public KontrolerKlienta(IModel model) {
		this.model = model;
	}

	@Override
	public void wplataPieniedzy(int nrKarty, int pin) {
		Widok.wyswietlanie("KontrolerKlienta", "wplataPieniedzy", true, "Wybrano opcję 'Wpłata pieniędzy'.");
		new WplataPieniedzy(this.model, nrKarty, pin);
	}

	@Override
	public void uwierzytelnianieKlienta(int nrKarty, int pin) {
		Widok.wyswietlanie("KontrolerKlienta", "uwierzytelnianieKlienta", true, "Próba logowania ");
		model.logowanieKlient(nrKarty, pin);
	}

	public void wykonaniePrzelewu() {}
	public void sprawdzenieStanuKonta() {}
	public void wyplataPieniedzy() {}
}
