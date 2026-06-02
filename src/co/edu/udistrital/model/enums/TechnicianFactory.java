package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo de especialidad del
 * tecnico a usar en la logica del sistema con base en la seleccion de la vista
 *
 * @author Juan David Diaz Perez
 */
public class TechnicianFactory {

	/**
	 * Metodo que recibe el tipo de especialidad del tecnico en string y retorna su
	 * equivalente en enum
	 * 
	 * @param technicianSpeciality Especialidad en string del tenico
	 * @return Especialidad en enum del tecnico
	 */
	public TechnicianSpecialty generaTechnicianSpecialty(String technicianSpeciality) {
		return switch (technicianSpeciality.replaceAll("\\s+", "").toLowerCase()) {
		case "electricoautomotriz" -> TechnicianSpecialty.AUTOMOTIVE_ELECTRICITY;
		case "operadordegrua" -> TechnicianSpecialty.CRANE_OPERATION;
		case "mecanicogeneral" -> TechnicianSpecialty.GENERAL_MECHANICS;
		case "operariomontallantas" -> TechnicianSpecialty.PLUMBING;
		case "cerrajerodevehiculos" -> TechnicianSpecialty.VEHICULAR_LOCKSMITH;
		default -> throw new IllegalArgumentException("Tipo de especialidad no soportado: " + technicianSpeciality);
		};
	}
}
