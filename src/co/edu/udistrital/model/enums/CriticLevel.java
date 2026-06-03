package co.edu.udistrital.model.enums;


public enum CriticLevel {

	LOW("Baja"),
	MEDIUM("Media"),
	HIGH("Alta");

	private final String displayName;

	
	private CriticLevel(String displayName) {
		this.displayName = displayName;
	}

	
	public String getDisplayName() {
		return displayName;
	}
}