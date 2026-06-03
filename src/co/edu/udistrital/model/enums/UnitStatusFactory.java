package co.edu.udistrital.model.enums;


public class UnitStatusFactory {

	
	public UnitStatus generateUnitStatus(String unitStatus) {
		return switch (unitStatus.replaceAll("\\s+", "").toLowerCase()) {
		case "disponible" -> UnitStatus.AVAILABLE;
		case "asignada" -> UnitStatus.ASSIGNED;
		case "enmantenimiento" -> UnitStatus.MAINTENANCE;
		case "inactivo" -> UnitStatus.INACTIVE;
		default -> throw new IllegalArgumentException("Tipo de status no soportado: " + unitStatus);
		};
	}
}
