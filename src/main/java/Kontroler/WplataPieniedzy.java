package Kontroler;

import Model.IModel;

import java.util.HashMap;
import java.util.Map;

public class WplataPieniedzy {

	private final IModel model;
	private final int nrKarty;
	private final int pin;

	private int deklarowanaKwota;
	private int wartoscBanknotow;
	private boolean potwierdzenie;

	private Map<Integer, Integer> banknoty;

	public WplataPieniedzy(IModel model, int nrKarty, int pin) {
		// Konstruktor domyślny – zachowuje stare zachowanie.
		this(model, nrKarty, pin, 1200, domyslneBanknoty(), true);
	}

	/**
	 * Konstruktor używany w testach akceptacyjnych (FitNesse).
	 * Pozwala parametryzować przebieg PU02. Wpłata pieniędzy (kwota, banknoty, potwierdzenie).
	 */
	public WplataPieniedzy(IModel model, int nrKarty, int pin,
						   int deklarowanaKwota,
						   Map<Integer, Integer> banknoty,
						   boolean potwierdzenie) {
		this.model = model;
		this.nrKarty = nrKarty;
		this.pin = pin;

		this.deklarowanaKwota = 0;
		this.wartoscBanknotow = 0;
		this.potwierdzenie = false;
		this.banknoty = new HashMap<>();

		if (!uwierzytelnienieKlienta()) return;

		this.wprowadzenieKwoty(deklarowanaKwota);
		this.wprowadzenieBanknotow(banknoty);

		// PU10 – weryfikacja zawartości gotówki bankomatu (miejsce na banknoty)
		if (!this.model.sprawdzenieMiejscaNaGotowke(this.banknoty)) {
			this.zwrotBanknotow();
			return;
		}

		// sprawdzenie spójności – czy suma banknotów = deklarowana kwota
		this.wartoscBanknotow = this.przeliczenieWartosciBanknotow();
		boolean czyRowne = (this.deklarowanaKwota == this.wartoscBanknotow);
		if (!czyRowne) {
			this.zwrotBanknotow();
			return;
		}

		// PU11 – weryfikacja operacji przez system bankowy
		boolean czyZweryfikowano = this.model.weryfikacjaTransakcjiWBanku(this.deklarowanaKwota, this.nrKarty);
		if (!czyZweryfikowano) {
			this.zwrotBanknotow();
			return;
		}

		this.potwierdzenie = potwierdzenie;
		this.model.ksiegowanieWplaty(this.deklarowanaKwota, this.nrKarty, this.potwierdzenie, this.banknoty);

		if (this.potwierdzenie) this.drukowaniePotwierdzenia();
	}

	private static Map<Integer, Integer> domyslneBanknoty() {
		Map<Integer, Integer> banknoty = new HashMap<>();
		banknoty.put(100, 12);
		return banknoty;
	}

	private boolean uwierzytelnienieKlienta() {
		boolean czyZalogowano = model.logowanieKlient(nrKarty, pin);
		return czyZalogowano;
	}

	private void wprowadzenieKwoty(int kwota) {
		this.deklarowanaKwota = Math.max(0, kwota);
	}

	private void wprowadzenieBanknotow(Map<Integer, Integer> banknotyWej) {
		this.banknoty.clear();
		if (banknotyWej == null) return;
		for (Map.Entry<Integer, Integer> e : banknotyWej.entrySet()) {
			Integer nominal = e.getKey();
			Integer szt = e.getValue();
			if (nominal == null || szt == null) continue;
			if (nominal <= 0 || szt <= 0) continue;
			this.banknoty.put(nominal, szt);
		}
	}

	private int przeliczenieWartosciBanknotow() {
		int suma = 0;
		for (Map.Entry<Integer, Integer> e : banknoty.entrySet()) {
			suma += e.getKey() * e.getValue();
		}
		return suma;
	}

	private void zwrotBanknotow() {
		// zwrot wprowadzonych banknotów do klienta (tu tylko symulacja)
	}

	private void drukowaniePotwierdzenia() {
		int stanPrzed = 1000;
		int stanPo = stanPrzed + deklarowanaKwota;

		DrukowaniePotwierdzenia druk = new DrukowaniePotwierdzenia(
				"2025-12-14 12:00", nrKarty, "Jan Kowalski",
				stanPrzed, stanPo, "Wplata Gotowki", "", 0, "", "WPLATA"
		);
		druk.drukowanie();
	}
}
