package Kontroler;

public interface IKontrolerPracownika {
	void aktualizacjaZawartosciGotowki();
	void pobranieDanychOTransakcjach(int nrKarty, int pin);
	void uwierzytelnianiePracownika(int nrKarty, int pin);
}
