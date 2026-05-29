package co.edu.udistrital.model.enums;

/**
 * Clase encargada de recibir que tipo de enum de tipo zona usar en la logica
 * del sistema
 *
 * @author Juan David Diaz Peres
 */
public class ZoneFactory {

	/**
	 * Metodo que recibe la zona de operacion en string y retorna su equivalente en
	 * enum
	 * 
	 * @param zone Zona en sting
	 * @return Zona en enum
	 */
	public OperationZone generateOperationZone(String zone) {
		return switch (zone.toLowerCase().trim()) {
		case "usaquen" -> OperationZone.USAQUEN;
		case "chapinero" -> OperationZone.CHAPINERO;
		case "santafe" -> OperationZone.SANTA_FE;
		case "sancristobal" -> OperationZone.SAN_CRISTOBAL;
		case "usme" -> OperationZone.USME;
		case "tunjuelito" -> OperationZone.TUNJUELITO;
		case "bosa" -> OperationZone.BOSA;
		case "kennedy" -> OperationZone.KENNEDY;
		case "fontibon" -> OperationZone.FONTIBON;
		case "engativa" -> OperationZone.ENGATIVA;
		case "suba" -> OperationZone.SUBA;
		case "barriosunidos" -> OperationZone.BARRIOS_UNIDOS;
		case "teusaquillo" -> OperationZone.TEUSAQUILLO;
		case "losmartires" -> OperationZone.LOS_MARTIRES;
		case "antonionariño" -> OperationZone.ANTONIO_NARIÑO;
		case "puentearanda" -> OperationZone.PUENTE_ARANDA;
		case "lacandelaria" -> OperationZone.LA_CANDELARIA;
		case "rafaeluribeuribe" -> OperationZone.RAFAEL_URIBE_URIBE;
		case "ciudadbolivar" -> OperationZone.CIUDAD_BOLIVAR;
		case "sumapaz" -> OperationZone.SUMAPAZ;
		default -> throw new IllegalArgumentException("Tipo de apuesta no soportado: " + zone);
		};
	}
}
