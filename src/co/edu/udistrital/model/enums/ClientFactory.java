package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo de cliente a usar en la
 * logica del sistema con base en la seleccion de la vista
 *
 * @author Juan David Diaz Perez
 */
public class ClientFactory {

	/**
	 * Metodo que recibe el tipo de cliente en string y retorna su equivalente en
	 * enum
	 * 
	 * @param clientType Tipo de cliente en string
	 * @return Tipo de cliente equivalente en enum
	 */
	public ClientType generateClientType(String clientType) {
		return switch (clientType.toLowerCase().trim()) {
		case "particular" -> ClientType.PRIVATE;
		case "seguros" -> ClientType.INSURANCE;
		case "empresarial" -> ClientType.TRANSPORT_COMPANY;
		default -> throw new IllegalArgumentException("Tipo de cliente no soportado: " + clientType);
		};
	}
}
