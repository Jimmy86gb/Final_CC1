package co.edu.udistrital.model.dtos;

import java.util.UUID;

/**
 * Clase de transmision de datos de logica a vista del tecnico
 *
 * @author Juan David Diaz Perez
 */
public class TechnicianDTO {

	private final UUID id;
	private final String name;
	private final String specialty;
	private final String zone;
	private final String status;
	private final boolean isEditable;

	/**
	 * Metodo constructor de el DTO de tecnico
	 * 
	 * @param id        La ID del tecnico actual
	 * @param name      El nombre del tecnico actual
	 * @param specialty La especialidad del tecnico actual
	 * @param zone      La zona del tecnico actual
	 * @param status    El status del tecnico actual
	 */
	public TechnicianDTO(UUID id, String name, String specialty, String zone, String status, boolean isEditable) {
		super();
		this.id = id;
		this.name = name;
		this.specialty = specialty;
		this.zone = zone;
		this.status = status;
		this.isEditable = isEditable;
	}

	/**
	 * @return La ID del tecnico actual
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @return El nombre del tecnico actual
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return La especialidad del tecnico actual
	 */
	public String getSpecialty() {
		return specialty;
	}

	/**
	 * @return La zona del tecnico actual
	 */
	public String getZone() {
		return zone;
	}

	/**
	 * @return El status del tecnico actual
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
