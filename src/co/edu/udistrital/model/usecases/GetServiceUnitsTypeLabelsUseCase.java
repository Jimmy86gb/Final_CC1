package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar el tipo de unidad a editar de unidades de
 * servicio en formato amigable para usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetServiceUnitsTypeLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> serviceUnitsTypeLabels = new SimpleList<>();

		UnitType[] unitTypes = UnitType.values();

		for (int i = 0; i < unitTypes.length; i++) {
			serviceUnitsTypeLabels.add(unitTypes[i].getDisplayName());
		}

		return serviceUnitsTypeLabels;
	}
}
