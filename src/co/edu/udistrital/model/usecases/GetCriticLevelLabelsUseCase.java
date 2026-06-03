package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.structures.SimpleList;


public class GetCriticLevelLabelsUseCase {

	
	public SimpleList<String> execute() {

		SimpleList<String> criticLevelLabels = new SimpleList<>();

		CriticLevel[] criticLevels = { CriticLevel.LOW, CriticLevel.HIGH };

		for (int i = 0; i < criticLevels.length; i++) {
			criticLevelLabels.add(criticLevels[i].getDisplayName());
		}

		return criticLevelLabels;
	}
}
