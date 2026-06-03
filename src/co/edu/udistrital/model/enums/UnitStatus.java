package co.edu.udistrital.model.enums;


public enum UnitStatus {

	AVAILABLE("Disponible"),
	ASSIGNED("Asignada"),
	MAINTENANCE("En mantenimiento"),
	INACTIVE("Inactivo");

	private final String displayName;

	
	private UnitStatus(String displayName) {
		this.displayName = displayName;
	}

	
	public String getDisplayName() {
		return displayName;
	}
}