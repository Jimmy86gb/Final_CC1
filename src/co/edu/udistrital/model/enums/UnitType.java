package co.edu.udistrital.model.enums;


public enum UnitType {

	CRANE("Grua"),
	MOTORCYCLE("Moto"),
	CAR("Carro"),
	TRUCK("Camioneta");

	private final String displayName;

	
	private UnitType(String displayName) {
		this.displayName = displayName;
	}

	
	public String getDisplayName() {
		return displayName;
	}
}