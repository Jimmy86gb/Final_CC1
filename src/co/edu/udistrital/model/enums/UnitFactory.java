package co.edu.udistrital.model.enums;


public class UnitFactory {

	
	public UnitType generateUnitType(String unitType) {
		return switch (unitType.replaceAll("\\s+", "").toLowerCase()) {
		case "grua" -> UnitType.CRANE;
		case "moto" -> UnitType.MOTORCYCLE;
		case "camioneta" -> UnitType.TRUCK;
		case "carro" -> UnitType.CAR;
		default -> throw new IllegalArgumentException("Tipo de unidad no soportado: " + unitType);
		};
	}
}
