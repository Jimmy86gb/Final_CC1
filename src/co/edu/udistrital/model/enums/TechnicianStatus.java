package co.edu.udistrital.model.enums;

public enum TechnicianStatus {
	AVAILABLE("Disponible"), BUSY("Ocupado"), INACTIVE("Inactivo");

	private final String displayName;

	private TechnicianStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
}
