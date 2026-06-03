package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.structures.SimpleList;


public class GetTechnicianStatusLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> technicianStatusLabels = new SimpleList<>();

		TechnicianStatus[] technicianStatus = { TechnicianStatus.AVAILABLE, TechnicianStatus.INACTIVE };

		for (int i = 0; i < technicianStatus.length; i++) {
			technicianStatusLabels.add(technicianStatus[i].getDisplayName());
		}

		return technicianStatusLabels;
	}
}
