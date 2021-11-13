package fr.iocean.arrosage;

public enum RelayStatusEnum {

	ON("ON   "),
	OFF("OFF  "),
	WAIT("WAIT ");
	
	private final String valueString;

	private RelayStatusEnum(String valueString) {
		this.valueString = valueString;
	}

	public String getValueString() {
		return valueString;
	}
}
