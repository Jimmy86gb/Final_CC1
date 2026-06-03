package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.structures.SimpleList;


public class GetKitTypeLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> kitTypeLabels = new SimpleList<>();

		KitType[] kitTypes = KitType.values();

		for (int i = 0; i < kitTypes.length; i++) {
			kitTypeLabels.add(kitTypes[i].getDisplayName());
		}

		return kitTypeLabels;
	}
}
