package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar los tipos de kits en formato amigable para
 * usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetKitTypeLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> kitTypeLabels = new SimpleList<>();

		KitType[] kitTypes = KitType.values();

		for (int i = 0; i < kitTypes.length; i++) {
			kitTypeLabels.add(kitTypes[i].getDisplayName());
		}

		return kitTypeLabels;
	}
}
