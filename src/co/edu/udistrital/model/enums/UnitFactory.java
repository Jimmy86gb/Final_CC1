package co.edu.udistrital.model.enums;

/**
 * Clase encargada de retornar que tipo de enum de tipo unidad usar en la logica
 * del sistema con base en la seleccion de la vista
 *
 * @author Juan David Diaz Perez
 */
public class UnitFactory {

	/**
	 * Metodo que recibe el tipo de unidad en string y retorna su equivalente en
	 * enum
	 * 
	 * @param unitType Unidad en string
	 * @return Zona en enum
	 */
	public UnitType generateUnitType(String unitType) {
		return switch (unitType.toLowerCase().trim()) {
		case "grua" -> UnitType.CRANE;
		case "moto" -> UnitType.MOTORCYCLE;
		case "camioneta" -> UnitType.TRUCK;
		case "carro" -> UnitType.CAR;
		default -> throw new IllegalArgumentException("Tipo de unidad no soportado: " + unitType);
		};
	}
}
