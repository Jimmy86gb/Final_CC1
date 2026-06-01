package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo de status del tecnico a
 * usar en la logica del sistema con base en la seleccion de la vista
 *
 * @author Juan David Diaz Perez
 */
public class TechnicianStatusFactory {

	/**
	 * Metodo que recibe el tipo de especialidad del tecnico en string y retorna su
	 * equivalente en enum
	 * 
	 * @param technicianStatus Especialidad en string del tenico
	 * @return Especialidad en enum del tecnico
	 */
	public TechnicianStatus generaTechnicianStatus(String technicianStatus) {
		return switch (technicianStatus.toLowerCase()) {
		case "disponible" -> TechnicianStatus.AVAILABLE;
		case "ocupado" -> TechnicianStatus.BUSY;
		case "inactivo" -> TechnicianStatus.INACTIVE;
		default -> throw new IllegalArgumentException("Tipo de status no soportado: " + technicianStatus);
		};
	}
}
