package Model;

public interface IDAO {

	boolean uwierzytelnienieKlienta(int nrKarty, int pin);

	boolean uwierzytelnieniePracownika(int nrKarty, int pin);

	int pobranieStanuGotowki();

	void aktualizacjaStanuGotowki(int kwota);

	boolean zaksiegowanieOperacji(String dane);

	String[] pobranieLogow();

	void zapisanieLogow(String informacje);
	boolean weryfikacjaTransakcjiWBanku(int kwota, int nrKarty);

}