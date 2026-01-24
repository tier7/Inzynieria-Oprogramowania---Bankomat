package Model;

import java.util.*;

public class RejestrTransakcji {

	private IDAO dao;
	private IFabrykaTransakcji fabryka;
	private Collection<ITransakcja> transakcje;
	private Sejf sejf;
	private Collection<Klient> klienci;

	public RejestrTransakcji(IDAO dao) {
		this.dao = dao;
		this.fabryka = new FabrykaTransakcji();
		this.transakcje = new ArrayList<>();

		transakcje.add(new Transakcja("2025-12-10", "WPLATA", 500, 111111, 0, "Bankomat", "Wplata wlasna"));
		transakcje.add(new Transakcja("2025-12-11", "WYPLATA", 200, 111111, 0, "Bankomat", "Wyplata wlasna"));
		transakcje.add(new Transakcja("2025-12-12", "WPLATA", 1200, 222222, 0, "Bankomat", "Wplata wlasna"));
		transakcje.add(new Transakcja("2025-12-14", "PRZELEW", 300, 111111, 333333, "Zygmunt Nowak", "Za zakupy"));
	}

	public String[] pobranieWszystkichTransakcji() {
		String[] wyniki = new String[transakcje.size()];
		int i = 0;
		for (ITransakcja t : transakcje) {
			wyniki[i] = t.pobranieDanych();
			i++;
		}
		return wyniki;
	}

	public void zapisTransakcji(String daneBazowe, boolean zPotwierdzeniem) {

		ITransakcja transakcja = fabryka.utworzenieTransakcji(daneBazowe);

		String dane;

		if (zPotwierdzeniem) {
			int kwota = wyciagnijKwote(daneBazowe);
			int saldoPrzed = 1000;
			int saldoPo = saldoPrzed + kwota;

			transakcja = new TransakcjaZPotwierdzeniem(transakcja, saldoPrzed, saldoPo, "Jan Kowalski");
			dane = transakcja.pobranieDanych();
		} else {
			dane = transakcja.pobranieDanych();
		}

		transakcje.add(transakcja);
		dao.zaksiegowanieOperacji(dane);
	}

	/**
	 * Metody pomocnicze dodane na potrzeby testów akceptacyjnych (FitNesse).
	 * Pozwalają sprawdzić stan warstwy encji (rejestru) przed i po wykonaniu PU.
	 */
	public int dajLiczbeTransakcji() {
		return this.transakcje.size();
	}

	public String dajDaneOstatniejTransakcji() {
		String last = "";
		for (ITransakcja t : this.transakcje) {
			last = t.pobranieDanych();
		}
		return last;
	}

	private int wyciagnijKwote(String daneBazowe) {

		try {
			String[] parts = daneBazowe.split(";");
			for (String p : parts) {
				if (p.startsWith("Kwota=")) {
					return Integer.parseInt(p.split("=")[1]);
				}
			}
		} catch (Exception ignored) {}
		return 0;
	}
}
