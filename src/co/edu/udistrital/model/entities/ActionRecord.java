package co.edu.udistrital.model.entities;

import java.io.Serializable;

import co.edu.udistrital.model.enums.ActionType;

public class ActionRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String description; // Ej: "Se actualizó al técnico Carlos a Ocupado"
	private final ActionType type; // El tipo de acción
	private final String targetId; // A qué ID le hicimos el cambio
	private final String[] previousState; // El arreglo con los datos Viejos para poder restaurar

	public ActionRecord(String description, ActionType type, String targetId, String... previousState) {
		this.description = description;
		this.type = type;
		this.targetId = targetId;
		this.previousState = previousState;
	}

	public String getDescription() {
		return description;
	}

	public ActionType getType() {
		return type;
	}

	public String getTargetId() {
		return targetId;
	}

	public String[] getPreviousState() {
		return previousState;
	}
}