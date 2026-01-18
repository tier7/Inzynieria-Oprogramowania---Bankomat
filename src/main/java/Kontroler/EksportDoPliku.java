package main.java.Kontroler;

import main.java.Model.IModel;
import main.java.Komunikacja.Widok;

public class EksportDoPliku extends IStrategiaEksportu {

	private String nazwaPliku;

	public EksportDoPliku(IModel model) {
		super(model);
		this.nazwaPliku = "transakcje.txt";
	}

	@Override
	public void eksportDanych(String[] dane) {
		int n = (dane == null) ? 0 : dane.length;

		Widok.wyswietlanie("EksportDoPliku", "eksportDanych", true,
				"Generuję plik: " + nazwaPliku + " (rekordów: " + n + ")");

		if (dane == null || dane.length == 0) {
			Widok.wyswietlanie("EksportDoPliku", "eksportDanych", true,
					"Brak danych do zapisania.");
			return;
		}

		Widok.wyswietlanie("EksportDoPliku", "eksportDanych", true,
				"Zapis zakończony (symulacja).");
	}
}