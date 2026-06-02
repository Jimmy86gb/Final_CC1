package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo de riesgo a usar en la
 * logica del sistema con base en la seleccion de la vista
 *
 * @author Juan David Diaz Perez
 */
public class CriticFactory {

	/**
	 * Metodo que recibe el tipo de riesgo en string y retorna su equivalente en
	 * enum
	 * 
	 * @param critictLevel Nivel de riesgo del pedido en string
	 * @return Nivel de riesgo en enum para usar en la logica
	 */
	public CriticLevel generateCriticLevel(String critictLevel) {
		return switch (critictLevel.replaceAll("\\s+", "").toLowerCase()) {
		case "bajo" -> CriticLevel.LOW;
		case "medio" -> CriticLevel.MEDIUM;
		case "alto" -> CriticLevel.HIGH;
		default -> throw new IllegalArgumentException("Tipo de riesgo no soportado: " + critictLevel);
		};
	}
}
