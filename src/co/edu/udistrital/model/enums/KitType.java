package co.edu.udistrital.model.enums;

/**
 * Enum que representa los diferentes tipos de kits
 * disponibles para la atención de reportes dentro del sistema.
 * Cada tipo posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum KitType {

	PLUMBING_KIT("Kit de Montallantas"),
	ELECTRICITY_KIT("Kit de Electricidad"),
	LOCKSMITH_KIT("Kit de Cerrajeria"),
	CRANE_KIT("Kit de Grua"),
	GENERAL_KIT("Kit General");

	private final String displayName;

	/**
	 * Constructor de la enum.
	 *
	 * @param displayName Nombre descriptivo asociado al tipo de kit.
	 */
	private KitType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del tipo de kit.
	 *
	 * @return El nombre para visualización del tipo de kit.
	 */
	public String getDisplayName() {
		return displayName;
	}
}