package Model;

public class Klient {

	private int nrKarty;
	private int pin;
	private String imie;
	private String nazwisko;
	private int saldo;

	public Klient(int nrKarty, int pin, String imie, String nazwisko, int saldo) {
		this.nrKarty = nrKarty;
		this.pin = pin;
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.saldo = saldo;
	}

	public String pobranieDanych() {
		return "Klient: " + imie + " " + nazwisko +
				" [Karta: " + nrKarty +
				", PIN: " + pin +
				", Saldo: " + saldo + " PLN]";
	}
}

