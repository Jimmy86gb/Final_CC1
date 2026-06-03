package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.structures.SimpleList;


public class GetServiceUnitsTypeLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> serviceUnitsTypeLabels = new SimpleList<>();

		UnitType[] unitTypes = UnitType.values();

		for (int i = 0; i < unitTypes.length; i++) {
			serviceUnitsTypeLabels.add(unitTypes[i].getDisplayName());
		}

		return serviceUnitsTypeLabels;
	}
}
