package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.structures.SimpleList;


public class GetClientTypeLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> typeClientLabelsList = new SimpleList<>();

		ClientType[] clientTypes = ClientType.values();

		for (int i = 0; i < clientTypes.length; i++) {
			typeClientLabelsList.add(clientTypes[i].getDisplayName());
		}

		return typeClientLabelsList;
	}
}
