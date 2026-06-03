package co.edu.udistrital.model.enums;

/**
 * Enum que representa los diferentes tipos de clientes
 * que pueden registrarse en el sistema.
 * Cada tipo posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum ClientType {

	PRIVATE("Particular"),
	TRANSPORT_COMPANY("Empresarial"),
	INSURANCE("Seguros");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado al tipo de cliente.
	 */
	private ClientType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del tipo de cliente.
	 *
	 * @return El nombre para visualización del tipo de cliente.
	 */
	public String getDisplayName() {
		return displayName;
	}
}