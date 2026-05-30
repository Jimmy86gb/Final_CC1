package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo kit usar en la logica
 * del sistema con base en la seleccion de la vista
 *
 * @author Juan David Diaz Perez
 */
public class KitFactory {

	/**
	 * Metodo que recibe el tipo de kit en string y retorna su equivalente en enum
	 * 
	 * @param kitType Tipo de kit seleccionado en String
	 * @return Enum kit a usar en la logica
	 */
	public KitType generateKitType(String kitType) {
		return switch (kitType.toLowerCase().trim()) {
		case "kitdegrua" -> KitType.CRANE_KIT;
		case "kitdeelectricidad" -> KitType.ELECTRICITY_KIT;
		case "kitgeneral" -> KitType.GENERAL_KIT;
		case "kitdecerrajeria" -> KitType.LOCKSMITH_KIT;
		case "kitdemontallantas" -> KitType.PLUMBING_KIT;
		default -> throw new IllegalArgumentException("Tipo de kit no soportado: " + kitType);
		};
	}
}
