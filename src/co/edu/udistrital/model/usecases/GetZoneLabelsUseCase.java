package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.structures.SimpleList;


public class GetZoneLabelsUseCase {

	
	public SimpleList<String> execute() {
		SimpleList<String> zoneLabels = new SimpleList<>();

		OperationZone[] zones = OperationZone.values();

		for (int i = 0; i < zones.length; i++) {
			zoneLabels.add(zones[i].getDisplayName());
		}

		return zoneLabels;
	}
}