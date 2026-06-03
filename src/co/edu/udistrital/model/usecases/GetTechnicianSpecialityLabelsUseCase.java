package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.structures.SimpleList;


public class GetTechnicianSpecialityLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> technicianSpecialityLabels = new SimpleList<>();

		TechnicianSpecialty[] technicianSpecialties = TechnicianSpecialty.values();

		for (int i = 0; i < technicianSpecialties.length; i++) {
			technicianSpecialityLabels.add(technicianSpecialties[i].getDisplayName());
		}

		return technicianSpecialityLabels;
	}

}
