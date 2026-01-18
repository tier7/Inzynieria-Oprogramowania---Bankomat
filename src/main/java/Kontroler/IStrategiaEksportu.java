package Kontroler;

import Model.IModel;

public abstract class IStrategiaEksportu {

	protected IModel model;

	public IStrategiaEksportu(IModel model) {
		this.model = model;
	}

	public abstract void eksportDanych(String[] dane);
}
