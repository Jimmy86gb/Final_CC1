package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;

/**
 * Entidad que representa un técnico dentro del sistema.
 * Permite almacenar y gestionar la información relacionada con su
 * identificación, nombre, especialidad, zona de operación y estado laboral.
 * 
 * @author ChrZ
 */
public class Technician implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private String name;
	private TechnicianSpecialty specialty;
	private OperationZone zone;
	private TechnicianStatus status;

	/**
	 * Constructor principal para crear una instancia de Technician.
	 * Genera automáticamente un identificador único para el técnico.
	 * 
	 * @param name El nombre del técnico.
	 * @param specialty La especialidad técnica del profesional.
	 * @param zone La zona de operación asignada.
	 * @param status El estado inicial del técnico.
	 */
	public Technician(String name, TechnicianSpecialty specialty, OperationZone zone, TechnicianStatus status) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.specialty = specialty;
		this.zone = zone;
		this.status = status;
	}

	/**
	 * Actualiza el nombre del técnico.
	 * 
	 * @param name El nuevo nombre del técnico.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Define o cambia la especialidad técnica del profesional.
	 * 
	 * @param specialty La nueva especialidad.
	 */
	public void setSpecialty(TechnicianSpecialty specialty) {
		this.specialty = specialty;
	}

	/**
	 * Actualiza la zona de operación asignada al técnico.
	 * 
	 * @param zone La nueva zona de operación.
	 */
	public void setZone(OperationZone zone) {
		this.zone = zone;
	}

	/**
	 * Actualiza el estado actual del técnico.
	 * 
	 * @param status El nuevo estado del técnico.
	 */
	public void setStatus(TechnicianStatus status) {
		this.status = status;
	}

	/**
	 * Asigna o actualiza el identificador único del técnico.
	 * 
	 * @param id El nuevo ID del técnico.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Obtiene el identificador único del técnico.
	 * 
	 * @return El ID del técnico.
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Obtiene el nombre del técnico.
	 * 
	 * @return El nombre del técnico.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtiene la especialidad técnica del profesional.
	 * 
	 * @return La especialidad del técnico.
	 */
	public TechnicianSpecialty getSpecialty() {
		return specialty;
	}

	/**
	 * Obtiene la zona de operación asignada al técnico.
	 * 
	 * @return La zona de operación.
	 */
	public OperationZone getZone() {
		return zone;
	}

	/**
	 * Obtiene el estado actual del técnico.
	 * 
	 * @return El estado del técnico.
	 */
	public TechnicianStatus getStatus() {
		return status;
	}

	/**
	 * Compara este técnico con otro objeto. La igualdad se determina
	 * exclusivamente mediante la coincidencia de sus identificadores (ID).
	 * 
	 * @param obj Objeto a comparar con la instancia actual.
	 * @return true si ambos técnicos tienen el mismo ID,
	 *         false en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Technician technician = (Technician) obj;
		return Objects.equals(technician.id, id);
	}
}