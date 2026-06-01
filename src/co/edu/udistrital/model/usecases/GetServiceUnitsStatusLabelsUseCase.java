package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar el estado a editar en las unidades de
 * servicio en formato amigable para usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetServiceUnitsStatusLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> serviceUnitsStatusLabels = new SimpleList<>();

		UnitStatus[] unitStatus = { UnitStatus.AVAILABLE, UnitStatus.MAINTENANCE, UnitStatus.INACTIVE };

		for (int i = 0; i < unitStatus.length; i++) {
			serviceUnitsStatusLabels.add(unitStatus[i].getDisplayName());
		}

		return serviceUnitsStatusLabels;
	}
}
