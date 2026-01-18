package Model;
import Komunikacja.Widok;

public class FabrykaTransakcji implements IFabrykaTransakcji {

	public ITransakcja utworzenieTransakcji(String dane) {
		java.util.Map<String, String> m = new java.util.HashMap<>();
		for (String part : dane.split(";")) {
			String[] kv = part.split("=", 2);
			if (kv.length == 2) m.put(kv[0].trim(), kv[1].trim());
		}

		String data = m.getOrDefault("Data", "");
		String typ = m.getOrDefault("Typ", "");
		int kwota = Integer.parseInt(m.getOrDefault("Kwota", "0"));
		int nadawca = Integer.parseInt(m.getOrDefault("Nadawca", "0"));
		int adresat = Integer.parseInt(m.getOrDefault("Adresat", "0"));
		String nazwaAdresata = m.getOrDefault("NazwaAdresata", "");
		String tytul = m.getOrDefault("Tytul", "");

		if (kwota <= 0 || nadawca <= 0) {
			throw new IllegalArgumentException("Błędne dane transakcji: " + dane);
		}

		return new Transakcja(data, typ, kwota, nadawca, adresat, nazwaAdresata, tytul);
	}



}