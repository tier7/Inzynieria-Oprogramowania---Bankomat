package main.java.Model;

import java.util.Map;

public interface IModel {

	boolean logowanieKlient(int nrKarty, int pin);

	boolean logowaniePracownik(int nrKarty, int pin);

	boolean weryfikacjaTransakcjiWBanku(int kwota, int nrKarty);

	void ksiegowanieWplaty(int kwota, int nrKarty, boolean potwierdzenie,
						   Map<Integer, Integer> banknoty);

	String[] pobranieWszystkichTransakcji();

	void rejestracjaZdarzenia(String informacje);
	boolean sprawdzenieMiejscaNaGotowke(Map<Integer, Integer> banknoty);
}