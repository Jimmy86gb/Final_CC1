package co.edu.udistrital.model.enums;


public enum KitType {

	PLUMBING_KIT("Kit de Montallantas"),
	ELECTRICITY_KIT("Kit de Electricidad"),
	LOCKSMITH_KIT("Kit de Cerrajeria"),
	CRANE_KIT("Kit de Grua"),
	GENERAL_KIT("Kit General");

	private final String displayName;

	
	private KitType(String displayName) {
		this.displayName = displayName;
	}

	
	public String getDisplayName() {
		return displayName;
	}
}