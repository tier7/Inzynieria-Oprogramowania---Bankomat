package main.java.Kontroler;

import main.java.Model.IModel;

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
		this.model = model;
		this.nrKarty = nrKarty;
		this.pin = pin;

		this.deklarowanaKwota = 0;
		this.wartoscBanknotow = 0;
		this.potwierdzenie = false;
		this.banknoty = new HashMap<>();

		if (!uwierzytelnienieKlienta()) return;

		this.wprowadzenieKwoty(1200);
		this.wprowadzenieBanknotow();

		boolean czyJestMiejsce = this.model.sprawdzenieMiejscaNaGotowke(this.banknoty);
		if (!czyJestMiejsce) {
			this.zwrotBanknotow();
			return;
		}

		this.wartoscBanknotow = this.przeliczenieWartosciBanknotow();
		boolean czyRowne = (this.deklarowanaKwota == this.wartoscBanknotow);
		if (!czyRowne) {
			this.zwrotBanknotow();
			return;
		}

		boolean czyZweryfikowano = this.model.weryfikacjaTransakcjiWBanku(this.deklarowanaKwota, this.nrKarty);
		if (!czyZweryfikowano) {
			this.zwrotBanknotow();
			return;
		}

		this.wyborPotwierdzenia();
		this.model.ksiegowanieWplaty(this.deklarowanaKwota, this.nrKarty, this.potwierdzenie, this.banknoty);

		if (this.potwierdzenie) this.drukowaniePotwierdzenia();
	}

	private boolean uwierzytelnienieKlienta() {
		boolean czyZalogowano = model.logowanieKlient(nrKarty, pin);
		return czyZalogowano;
	}

	private void wprowadzenieKwoty(int kwota) {
		this.deklarowanaKwota = Math.max(0, kwota);
	}

	private void wprowadzenieBanknotow() {
		banknoty.clear();
		banknoty.put(100, 12);
	}

	private int przeliczenieWartosciBanknotow() {
		int suma = 0;
		for (Map.Entry<Integer, Integer> e : banknoty.entrySet()) {
			suma += e.getKey() * e.getValue();
		}
		return suma;
	}

	private void wyborPotwierdzenia() {
		this.potwierdzenie = true;
	}

	private void zwrotBanknotow() {
		//zwrot baknotow
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
