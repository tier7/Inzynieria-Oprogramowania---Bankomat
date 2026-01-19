package Model;

public abstract class DekoratorTransakcji implements ITransakcja {

	protected ITransakcja transakcja;

	public DekoratorTransakcji(ITransakcja transakcja) {
		this.transakcja = transakcja;
	}

	public String pobranieDanych() {
		return transakcja.pobranieDanych();
	}

}