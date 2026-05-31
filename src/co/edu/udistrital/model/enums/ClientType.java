package co.edu.udistrital.model.enums;

public enum ClientType {
	PRIVATE("Particular"), TRANSPORT_COMPANY("Empresarial"), INSURANCE("Seguros");

	private final String displayName;

	private ClientType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
}
