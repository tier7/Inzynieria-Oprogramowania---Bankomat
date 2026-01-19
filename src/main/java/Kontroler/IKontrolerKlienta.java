package Kontroler;

public interface IKontrolerKlienta {

	void wykonaniePrzelewu();

	void sprawdzenieStanuKonta();

	void wplataPieniedzy(int nrKarty, int pin);

	void wyplataPieniedzy();

	void uwierzytelnianieKlienta(int nrKarty, int pin);

}