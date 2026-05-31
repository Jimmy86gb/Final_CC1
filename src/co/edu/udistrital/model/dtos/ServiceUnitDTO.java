package co.edu.udistrital.model.dtos;

import java.util.UUID;

/**
 * Clase de transmision de datos de logica a vista de la entidad unidad de
 * servicio
 *
 * @author Juan David Diaz Perez
 */
public class ServiceUnitDTO {

	private final UUID id;
	private final String type;
	private final String status;
	private final String zone;
	private final boolean isEditable;

	/**
	 * Contructor de la entidad DTO de las unidades de servicio
	 * 
	 * @param id         Id de la unidad de servicio actual
	 * @param type       Tipo de la unidad de servicio actual
	 * @param status     Status de la unidad de servicio actual
	 * @param zone       Zona de la unidad de sercivio actual
	 * @param isEditable Si se muestran los botones de elimiar y editar en la vista
	 */
	public ServiceUnitDTO(UUID id, String type, String status, String zone, boolean isEditable) {
		super();
		this.id = id;
		this.type = type;
		this.status = status;
		this.zone = zone;
		this.isEditable = isEditable;
	}

	/**
	 * @return Id de la unidad de servicio actual
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @return Tipo de la unidad de servicio actual
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Status de la unidad de servicio actual
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return Zona de la unidad de sercivio actual
	 */
	public String getZone() {
		return zone;
	}

	/**
	 * @return Si se muestran los botones de elimiar y editar en la vista
	 */
	public boolean isEditable() {
		return isEditable;
	}
}
