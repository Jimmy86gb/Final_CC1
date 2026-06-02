package co.edu.udistrital.model.dtos;

/**
 * Clase de transmision de datos de logica a vista de la entidad Kit
 *
 * @author Juan David Diaz Perez
 */
public class KitDTO {

	private final String id;
	private final String type;
	private final String status;
	private final boolean isEditable;

	/**
	 * Constructor del DTO de kit con lo necesario para la vista
	 * 
	 * @param id         ID del kit actual
	 * @param type       Tipo del kit actual
	 * @param status     Estatus del kit actual
	 * @param isEditable Si se muestran los botones de elimiar y editar en la vista
	 */
	public KitDTO(String id, String type, String status, boolean isEditable) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.isEditable = isEditable;
	}

	/**
	 * @return La id del kit actual
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return El tipo del kit actual
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return El status del kit actual
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return Si se muestran los botones de elimiar y editar en la vista
	 */
	public boolean isEditable() {
		return isEditable;
	}
}
