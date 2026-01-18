package main.java.Model;

public class TransakcjaZPotwierdzeniem extends DekoratorTransakcji {

	private int stanKontaPrzed;
	private int stanKontaPo;
	private String daneKlienta;

	public TransakcjaZPotwierdzeniem(ITransakcja transakcja, int stanPrzed, int stanPo, String klient) {
		super(transakcja);
		this.stanKontaPrzed = stanPrzed;
		this.stanKontaPo = stanPo;
		this.daneKlienta = klient;
	}

	@Override
	public String pobranieDanych() {
		String danePodstawowe = super.pobranieDanych();
		return danePodstawowe + "[Klient: " + daneKlienta
				+ ", SaldoPrzed: " + stanKontaPrzed + ", SaldoPo: " + stanKontaPo + "]";
	}

}