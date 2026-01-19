package Model;

import Komunikacja.Widok;

import java.util.Map;

public class Sejf {

	private Map<Integer, Integer> banknoty;
	private int iloscBanknotow;
	private int limitPojemnosci;


	public Sejf(Map<Integer, Integer> banknoty) {
		this.banknoty = banknoty;
		this.limitPojemnosci = 2500;
		this.iloscBanknotow = 0;
		for (int ilosc : banknoty.values()) this.iloscBanknotow += ilosc;
		Widok.wyswietlanie("Sejf", "Sejf", true, "Zainicjalizowano sejf, ilość banknotów w sejfie: " + this.iloscBanknotow);
	}


	public boolean czyMiejsce(int iloscNowychBanknotow) {
		int wolneMiejsce = limitPojemnosci - iloscBanknotow;
		boolean jestMiejsce = wolneMiejsce >= iloscNowychBanknotow;
		Widok.wyswietlanie("Sejf", "czyMiejsce", jestMiejsce, "Limit: " + limitPojemnosci + ", Wolne: " + wolneMiejsce + ", Wymagane: " + iloscNowychBanknotow);
		return jestMiejsce;
	}

	public boolean czySaNominaly(int kwota) {
		if (kwota <= 0) return false;

		java.util.List<Integer> nom = new java.util.ArrayList<>(banknoty.keySet());
		nom.sort(java.util.Collections.reverseOrder());

		int pozostalo = kwota;

		for (int n : nom) {
			int dost = banknoty.getOrDefault(n, 0);
			if (dost <= 0) continue;

			int ile = Math.min(dost, pozostalo / n);
			pozostalo -= ile * n;
			if (pozostalo == 0) break;
		}

		boolean ok = (pozostalo == 0);
		Widok.wyswietlanie("Sejf", "czySaNominaly", ok,
				"Kwota=" + kwota + ", wynik=" + (ok ? "MOŻNA" : "NIE MOŻNA"));
		return ok;
	}


	public void aktualizacjaStanu(Map<Integer, Integer> noweBanknoty) {
		if (noweBanknoty == null || noweBanknoty.isEmpty()) {
			Widok.wyswietlanie("Sejf", "aktualizacjaStanu", true, "Brak nowych banknotów do dodania");
			return;
		}

		int dodane = 0;
		for (Map.Entry<Integer, Integer> e : noweBanknoty.entrySet()) {
			int nominal = e.getKey();
			int szt = e.getValue();
			if (szt <= 0) continue;

			int stara = banknoty.getOrDefault(nominal, 0);
			banknoty.put(nominal, stara + szt);
			dodane += szt;
		}

		this.iloscBanknotow += dodane;

		Widok.wyswietlanie("Sejf", "aktualizacjaStanu", true,
				"Dodano banknotów: " + dodane + ", nowa ilość: " + this.iloscBanknotow);
	}
}
