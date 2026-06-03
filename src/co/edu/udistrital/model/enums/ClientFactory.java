package co.edu.udistrital.model.enums;


public class ClientFactory {

	
	public ClientType generateClientType(String clientType) {
		return switch (clientType.replaceAll("\\s+", "").toLowerCase()) {
		case "particular" -> ClientType.PRIVATE;
		case "seguros" -> ClientType.INSURANCE;
		case "empresarial" -> ClientType.TRANSPORT_COMPANY;
		default -> throw new IllegalArgumentException("Tipo de cliente no soportado: " + clientType);
		};
	}
}
