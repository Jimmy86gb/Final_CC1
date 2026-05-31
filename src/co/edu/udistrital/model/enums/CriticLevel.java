package co.edu.udistrital.model.enums;

public enum CriticLevel {
	LOW("Bajo"), MEDIUM("Medio"), HIGH("Alto");

	private final String displayName;

	private CriticLevel(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
}
