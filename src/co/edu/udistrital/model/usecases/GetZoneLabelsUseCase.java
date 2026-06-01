package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar las zonas de operacion en un formato de
 * texto amigable para poblar los componentes de seleccion en la vista.
 * 
 * @author Juan David Diaz Perez
 */
public class GetZoneLabelsUseCase {

	/**
	 * Metodo que extrae los nombres amigables de las zonas de operacion.
	 * 
	 * @return Lista de Strings con los nombres listos para la UI
	 */
	public SimpleList<String> execute() {
		SimpleList<String> zoneLabels = new SimpleList<>();

		OperationZone[] zones = OperationZone.values();

		for (int i = 0; i < zones.length; i++) {
			zoneLabels.add(zones[i].getDisplayName());
		}

		return zoneLabels;
	}
}