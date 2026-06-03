package co.edu.udistrital.model.enums;


public class TechnicianStatusFactory {

	
	public TechnicianStatus generaTechnicianStatus(String technicianStatus) {
		return switch (technicianStatus.replaceAll("\\s+", "").toLowerCase()) {
		case "disponible" -> TechnicianStatus.AVAILABLE;
		case "ocupado" -> TechnicianStatus.BUSY;
		case "inactivo" -> TechnicianStatus.INACTIVE;
		default -> throw new IllegalArgumentException("Tipo de status no soportado: " + technicianStatus);
		};
	}
}
