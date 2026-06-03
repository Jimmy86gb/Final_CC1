package co.edu.udistrital.model.entities;

import java.io.Serializable;

import co.edu.udistrital.model.enums.ActionType;

public class ActionRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String description; 
	private final ActionType type; 
	private final String targetId; 
	private final String[] previousState; 

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