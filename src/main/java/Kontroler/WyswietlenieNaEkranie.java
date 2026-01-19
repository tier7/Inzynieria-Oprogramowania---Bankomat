package Kontroler;

import Komunikacja.Widok;
import Model.IModel;

public class WyswietlenieNaEkranie extends IStrategiaEksportu {

	public WyswietlenieNaEkranie(IModel model) {
		super(model);
	}

	@Override
	public void eksportDanych(String[] dane) {
		int n = (dane == null) ? 0 : dane.length;
		Widok.wyswietlanie("WyswietlenieNaEkranie", "eksportDanych", true,
				"Wyświetlam dane na ekranie. Rekordów: " + n);

		if (dane == null) return;
		for (String wiersz : dane) {
			System.out.println("[EKRAN] " + wiersz);
		}
	}
}
