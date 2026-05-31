package co.edu.udistrital.model.enums;

public enum TechnicianSpecialty {
	GENERAL_MECHANICS("Mecanico General"), AUTOMOTIVE_ELECTRICITY("Electrico Automotriz"),
	VEHICULAR_LOCKSMITH("Cerrajero de Vehiculos"), CRANE_OPERATION("Operador de Grua"),
	PLUMBING("Operario Montallantas");

	private final String displayName;

	private TechnicianSpecialty(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
}
