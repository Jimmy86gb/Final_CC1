package co.edu.udistrital.model.enums;

public enum ReportStatus {
	PENDING("Pendiente"), ON_GOING("En progreso"), DONE("Atendida"), CANCELLED("Cancelada");

	private final String displayName;

	private ReportStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
}
