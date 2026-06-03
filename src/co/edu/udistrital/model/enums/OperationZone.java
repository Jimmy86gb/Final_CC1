package co.edu.udistrital.model.enums;

/**
 * Enum que representa las diferentes zonas de operación
 * disponibles dentro del sistema. Corresponden a las localidades
 * del Distrito Capital de Bogotá donde pueden prestarse servicios.
 * Cada zona posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum OperationZone {

	USAQUEN("Usaquen"),
	CHAPINERO("Chapinero"),
	SANTA_FE("Santa Fe"),
	SAN_CRISTOBAL("San Cristobal"),
	USME("Usme"),
	TUNJUELITO("Tunjuelito"),
	BOSA("Bosa"),
	KENNEDY("Kennedy"),
	FONTIBON("Fontibon"),
	ENGATIVA("Engativa"),
	SUBA("Suba"),
	BARRIOS_UNIDOS("Barrios Unidos"),
	TEUSAQUILLO("Teusaquillo"),
	LOS_MARTIRES("Los Martires"),
	ANTONIO_NARIÑO("Antonio Nariño"),
	PUENTE_ARANDA("Puente Aranda"),
	LA_CANDELARIA("La Candelaria"),
	RAFAEL_URIBE_URIBE("Rafael Uribe Uribe"),
	CIUDAD_BOLIVAR("Ciudad Bolivar"),
	SUMAPAZ("Sumapaz");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado a la zona de operación.
	 */
	private OperationZone(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo de la zona de operación.
	 *
	 * @return El nombre para visualización de la zona.
	 */
	public String getDisplayName() {
		return displayName;
	}
}