package Kontroler;

import Model.IModel;
import Komunikacja.Widok;

public class PobranieDanychOTransakcjach {

	private final IModel model;
	private final int nrKarty;
	private final int pin;

	private String zakresDat;
	private String typOperacji;
	private String[] listaWynikow;
	private boolean czyZalogowano;

	private IStrategiaEksportu strategiaEksportu;

	public PobranieDanychOTransakcjach(IModel model) {
		this(model, 0, 0);
	}

	public PobranieDanychOTransakcjach(IModel model, int nrKarty, int pin) {
		this.model = model;
		this.nrKarty = nrKarty;
		this.pin = pin;

		this.zakresDat = "";
		this.typOperacji = "";
		this.listaWynikow = new String[0];
		this.strategiaEksportu = null;
		this.czyZalogowano = false;

		if (!uwierzytelnieniePracownika()) return;

		this.wprowadzenieZakresuDat();
		this.wprowadzenieTypuOperacji();

		this.pobranieListyOperacji();
		this.filtrowanieDanych();

		int opcja = wyborOpcjiEksportu();
		eksportDanych(opcja);
	}

	private boolean uwierzytelnieniePracownika() {
		czyZalogowano = model.logowaniePracownik(nrKarty, pin);
		Widok.wyswietlanie("PobranieDanychOTransakcjach", "uwierzytelnieniePracownika", czyZalogowano,
				czyZalogowano ? "Uwierzytelniono pracownika" : "Błąd uwierzytelnienia");
		return czyZalogowano;
	}

	public void wprowadzenieZakresuDat() {
		this.zakresDat = "2025-01-01..2025-12-31";
		Widok.wyswietlanie("PobranieDanychOTransakcjach", "wprowadzenieZakresuDat", true,
				"Ustawiono zakres dat: " + zakresDat);
	}

	public void wprowadzenieTypuOperacji() {
		this.typOperacji = "WPLATA";
		Widok.wyswietlanie("PobranieDanychOTransakcjach", "wprowadzenieTypuOperacji", true,
				"Ustawiono typ operacji: " + typOperacji);
	}

	public String[] pobranieListyOperacji() {
		if (!czyZalogowano) {
			listaWynikow = new String[0];
			return listaWynikow;
		}

		String[] wszystkie = model.pobranieWszystkichTransakcji();
		if (wszystkie == null) wszystkie = new String[0];

		this.listaWynikow = wszystkie;

		Widok.wyswietlanie("PobranieDanychOTransakcjach", "pobranieListyOperacji", true,
				"Pobrano rekordów: " + listaWynikow.length);

		return listaWynikow;
	}

	public void filtrowanieDanych() {
		String[] zakres = parsujZakresDat();
		String od = zakres[0];
		String doDaty = zakres[1];

		int licznik = 0;
		for (String rekord : listaWynikow) {
			if (rekord == null) continue;
			if (czyRekordPasuje(rekord, typOperacji, od, doDaty)) {
				licznik++;
			}
		}

		String[] wynik = new String[licznik];

		int idx = 0;
		for (String rekord : listaWynikow) {
			if (rekord == null) continue;
			if (czyRekordPasuje(rekord, typOperacji, od, doDaty)) {
				idx = dodanieDoListyWynikowej(wynik, idx, rekord);
			}
		}

		this.listaWynikow = wynik;

		Widok.wyswietlanie("PobranieDanychOTransakcjach", "filtrowanieDanych", true,
				"Po filtrze (" + typOperacji + ", " + zakresDat + "): " + listaWynikow.length);
	}

	private int dodanieDoListyWynikowej(String[] wynik, int idx, String rekord) {
		wynik[idx] = rekord;
		return idx + 1;
	}

	private boolean czyRekordPasuje(String rekord, String typ, String od, String doDaty) {
		boolean pasujeTyp = rekord.contains("Typ=" + typ);

		String data = wyciagnijDate(rekord);
		boolean pasujeData = !data.isEmpty()
				&& data.compareTo(od) >= 0
				&& data.compareTo(doDaty) <= 0;

		return pasujeTyp && pasujeData;
	}

	private int wyborOpcjiEksportu() {
		int opcja = 1;
		Widok.wyswietlanie("PobranieDanychOTransakcjach", "wyborOpcjiEksportu", true,
				"Wybrano opcję eksportu: " + (opcja == 1 ? "EKRAN" : "PLIK"));
		return opcja;
	}

	public void eksportDanych(int opcja) {
		if (!czyZalogowano) {
			return;
		}

		if (opcja == 1) {
			strategiaEksportu = new WyswietlenieNaEkranie(model);
			Widok.wyswietlanie("PobranieDanychOTransakcjach", "eksportDanych", true,
					"Wybrano strategię: WyswietlenieNaEkranie");
		} else if (opcja == 2) {
			strategiaEksportu = new EksportDoPliku(model);
			Widok.wyswietlanie("PobranieDanychOTransakcjach", "eksportDanych", true,
					"Wybrano strategię: EksportDoPliku");
		} else {
			strategiaEksportu = null;
			Widok.wyswietlanie("PobranieDanychOTransakcjach", "eksportDanych", false,
					"Niepoprawna opcja eksportu: " + opcja);
			return;
		}

		strategiaEksportu.eksportDanych(listaWynikow);
	}

	private String[] parsujZakresDat() {
		String[] parts = zakresDat.split("\\.\\.");
		if (parts.length != 2) return new String[]{"0000-01-01", "9999-12-31"};
		return new String[]{parts[0].trim(), parts[1].trim()};
	}

	private String wyciagnijDate(String rekord) {
		int i = rekord.indexOf("Data=");
		if (i < 0) return "";

		int start = i + "Data=".length();
		int end = rekord.indexOf(",", start);
		if (end < 0) end = rekord.length();

		return rekord.substring(start, end).trim();
	}
}
