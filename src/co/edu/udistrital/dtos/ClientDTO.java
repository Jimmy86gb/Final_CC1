package co.edu.udistrital.dtos;

/**
 * Clase de transmision de datos de logica a vista de la entidad cliente
 *
 * @author Juan David Diaz Perez
 */
public class ClientDTO {
	private final String id;
	private final String name;
	private final String type;
	private final String contactInfo;

	/**
	 * Constructor que contiene los datos de cliente
	 * 
	 * @param id          Id del cliente actual
	 * @param name        Nombre del cliente actual
	 * @param type        Tipo de cliente para mostrar en la vista
	 * @param contactInfo Informacion de contracto del cliente actual
	 */
	public ClientDTO(String id, String name, String type, String contactInfo) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.contactInfo = contactInfo;
	}

	/**
	 * @return La ID del cliente actual
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return El nombre del cliente actual
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return El tipo de cliente para la vista del cliente actual
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return La informacion de contacto del cliente actual
	 */
	public String getContactInfo() {
		return contactInfo;
	}
}
