package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo de status de la unidad
 * de servicio a usar en la logica del sistema con base en la seleccion de la
 * vista
 *
 * @author Juan David Diaz Perez
 */
public class UnitStatusFactory {

	/**
	 * Metodo que recibe el status de la unidad de servicio en string y retorna su
	 * equivalente en enum
	 * 
	 * @param unitStatus Status en string
	 * @return Su equivalente en ENUM
	 */
	public UnitStatus generateUnitStatus(String unitStatus) {
		return switch (unitStatus.toLowerCase().trim()) {
		case "disponible" -> UnitStatus.AVAILABLE;
		case "asignada" -> UnitStatus.ASSIGNED;
		case "enmantenimiento" -> UnitStatus.MAINTENANCE;
		case "inactivo" -> UnitStatus.INACTIVE;
		default -> throw new IllegalArgumentException("Tipo de status no soportado: " + unitStatus);
		};
	}
}
