package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.structures.SimpleList;


public class GetKitStatusLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> serviceUnitsStatusLabels = new SimpleList<>();

		UnitStatus[] unitStatus = { UnitStatus.AVAILABLE, UnitStatus.INACTIVE };

		for (int i = 0; i < unitStatus.length; i++) {
			serviceUnitsStatusLabels.add(unitStatus[i].getDisplayName());
		}

		return serviceUnitsStatusLabels;
	}
}
