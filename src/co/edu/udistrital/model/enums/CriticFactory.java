package co.edu.udistrital.model.enums;


public class CriticFactory {

	
	public CriticLevel generateCriticLevel(String critictLevel) {
		return switch (critictLevel.replaceAll("\\s+", "").toLowerCase()) {
		case "baja" -> CriticLevel.LOW;
		case "media" -> CriticLevel.MEDIUM;
		case "alta" -> CriticLevel.HIGH;
		default -> throw new IllegalArgumentException("Tipo de riesgo no soportado: " + critictLevel);
		};
	}
}
