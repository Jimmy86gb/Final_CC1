package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar los tipos de cliente en formato amigable
 * para usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetClientTypeLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> typeClientLabelsList = new SimpleList<>();

		ClientType[] clientTypes = ClientType.values();

		for (int i = 0; i < clientTypes.length; i++) {
			typeClientLabelsList.add(clientTypes[i].getDisplayName());
		}

		return typeClientLabelsList;
	}
}
