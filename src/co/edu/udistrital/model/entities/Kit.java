package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;

/**
 * Entidad que representa un kit dentro del sistema.
 * Permite almacenar y gestionar la información relacionada con
 * su identificación, tipo y estado actual.
 * 
 * @author ChrZ
 */
public class Kit implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private KitType type;
	private UnitStatus status;

	/**
	 * Constructor principal para crear una instancia de Kit.
	 * Se genera automáticamente un identificador único para el kit.
	 * 
	 * @param type El tipo de kit asignado.
	 * @param status El estado inicial del kit.
	 */
	public Kit(KitType type, UnitStatus status) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.status = status;
	}

	/**
	 * Define o cambia el tipo de kit.
	 * 
	 * @param type El nuevo tipo de kit.
	 */
	public void setType(KitType type) {
		this.type = type;
	}

	/**
	 * Actualiza el estado actual del kit.
	 * 
	 * @param status El nuevo estado del kit.
	 */
	public void setStatus(UnitStatus status) {
		this.status = status;
	}

	/**
	 * Asigna o actualiza el identificador único del kit.
	 * 
	 * @param id El nuevo identificador del kit.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Obtiene el identificador único del kit.
	 * 
	 * @return El ID del kit.
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Obtiene el tipo de kit registrado.
	 * 
	 * @return El tipo de kit.
	 */
	public KitType getType() {
		return type;
	}

	/**
	 * Obtiene el estado actual del kit.
	 * 
	 * @return El estado del kit.
	 */
	public UnitStatus getStatus() {
		return status;
	}

	/**
	 * Compara este kit con otro objeto. La igualdad se determina
	 * exclusivamente mediante la coincidencia de sus identificadores (ID).
	 * 
	 * @param obj Objeto a comparar con la instancia actual.
	 * @return true si ambos kits tienen el mismo ID, false en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Kit kit = (Kit) obj;
		return Objects.equals(kit.id, id);
	}
}