package co.edu.udistrital.model.enums;


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

	
	private OperationZone(String displayName) {
		this.displayName = displayName;
	}

	
	public String getDisplayName() {
		return displayName;
	}
}