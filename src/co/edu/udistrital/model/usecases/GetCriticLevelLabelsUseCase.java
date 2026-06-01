package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar los tipos de riegos de reporte en formato
 * amigable para usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetCriticLevelLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> criticLevelLabels = new SimpleList<>();

		CriticLevel[] criticLevels = CriticLevel.values();

		for (int i = 0; i < criticLevels.length; i++) {
			criticLevelLabels.add(criticLevels[i].getDisplayName());
		}

		return criticLevelLabels;
	}
}
