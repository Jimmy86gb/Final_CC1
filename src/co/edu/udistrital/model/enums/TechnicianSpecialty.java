package co.edu.udistrital.model.enums;

/**
 * Enum que representa las diferentes especialidades
 * que pueden tener los técnicos dentro del sistema.
 * Cada especialidad posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum TechnicianSpecialty {

	GENERAL_MECHANICS("Mecanico General"),
	AUTOMOTIVE_ELECTRICITY("Electrico Automotriz"),
	VEHICULAR_LOCKSMITH("Cerrajero de Vehiculos"),
	CRANE_OPERATION("Operador de Grua"),
	PLUMBING("Operario Montallantas");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado a la especialidad técnica.
	 */
	private TechnicianSpecialty(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo de la especialidad técnica.
	 *
	 * @return El nombre para visualización de la especialidad.
	 */
	public String getDisplayName() {
		return displayName;
	}
}