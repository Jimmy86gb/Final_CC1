package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;

/**
 * Entidad que representa una unidad de servicio dentro del sistema.
 * Permite almacenar y gestionar la información relacionada con su
 * identificación, tipo, estado operativo y zona de operación.
 * 
 * @author ChrZ
 */
public class ServiceUnit implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private UnitType type;
	private UnitStatus status;
	private OperationZone zone;

	/**
	 * Constructor principal para crear una instancia de ServiceUnit.
	 * Genera automáticamente un identificador único para la unidad.
	 * 
	 * @param type El tipo de unidad de servicio.
	 * @param status El estado inicial de la unidad.
	 * @param zone La zona de operación asignada.
	 */
	public ServiceUnit(UnitType type, UnitStatus status, OperationZone zone) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.status = status;
		this.zone = zone;
	}

	/**
	 * Define o cambia el tipo de unidad de servicio.
	 * 
	 * @param type El nuevo tipo de unidad.
	 */
	public void setType(UnitType type) {
		this.type = type;
	}

	/**
	 * Actualiza el estado operativo de la unidad.
	 * 
	 * @param status El nuevo estado de la unidad.
	 */
	public void setStatus(UnitStatus status) {
		this.status = status;
	}

	/**
	 * Actualiza la zona de operación de la unidad.
	 * 
	 * @param zone La nueva zona de operación.
	 */
	public void setZone(OperationZone zone) {
		this.zone = zone;
	}

	/**
	 * Asigna o actualiza el identificador único de la unidad.
	 * 
	 * @param id El nuevo ID de la unidad.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Obtiene el identificador único de la unidad.
	 * 
	 * @return El ID de la unidad.
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Obtiene el tipo de unidad de servicio.
	 * 
	 * @return El tipo de unidad.
	 */
	public UnitType getType() {
		return type;
	}

	/**
	 * Obtiene el estado actual de la unidad.
	 * 
	 * @return El estado de la unidad.
	 */
	public UnitStatus getStatus() {
		return status;
	}

	/**
	 * Obtiene la zona de operación asignada a la unidad.
	 * 
	 * @return La zona de operación.
	 */
	public OperationZone getZone() {
		return zone;
	}

	/**
	 * Compara esta unidad de servicio con otro objeto. La igualdad se determina
	 * exclusivamente mediante la coincidencia de sus identificadores (ID).
	 * 
	 * @param obj Objeto a comparar con la instancia actual.
	 * @return true si ambas unidades tienen el mismo ID,
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

		ServiceUnit serviceUnit = (ServiceUnit) obj;
		return Objects.equals(serviceUnit.id, id);
	}
}