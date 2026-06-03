package co.edu.udistrital.model.enums;


public class KitFactory {

	
	public KitType generateKitType(String kitType) {
		return switch (kitType.replaceAll("\\s+", "").toLowerCase()) {
		case "kitdegrua" -> KitType.CRANE_KIT;
		case "kitdeelectricidad" -> KitType.ELECTRICITY_KIT;
		case "kitgeneral" -> KitType.GENERAL_KIT;
		case "kitdecerrajeria" -> KitType.LOCKSMITH_KIT;
		case "kitdemontallantas" -> KitType.PLUMBING_KIT;
		default -> throw new IllegalArgumentException("Tipo de kit no soportado: " + kitType);
		};
	}
}
