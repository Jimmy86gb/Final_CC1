package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar el estado a etidar en los tecnicos en
 * formato amigable para usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetTechnicianStatusLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> technicianStatusLabels = new SimpleList<>();

		TechnicianStatus[] technicianStatus = { TechnicianStatus.AVAILABLE, TechnicianStatus.INACTIVE };

		for (int i = 0; i < technicianStatus.length; i++) {
			technicianStatusLabels.add(technicianStatus[i].getDisplayName());
		}

		return technicianStatusLabels;
	}
}
