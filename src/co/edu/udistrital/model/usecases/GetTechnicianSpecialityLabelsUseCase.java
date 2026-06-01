package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.structures.SimpleList;

/**
 * Caso de uso encargado de retornar los tipos de especialidades de los tecnicos
 * en formato amigable para usar en la vista
 * 
 * @author Juan David Diaz Perez
 */
public class GetTechnicianSpecialityLabelsUseCase {

	/**
	 * Metodo que retorna la lista en strings para mostrar en la vista
	 * 
	 * @return La lista en strings para mostrar en la vista
	 */
	public SimpleList<String> execute() {

		SimpleList<String> technicianSpecialityLabels = new SimpleList<>();

		TechnicianSpecialty[] technicianSpecialties = TechnicianSpecialty.values();

		for (int i = 0; i < technicianSpecialties.length; i++) {
			technicianSpecialityLabels.add(technicianSpecialties[i].getDisplayName());
		}

		return technicianSpecialityLabels;
	}

}
