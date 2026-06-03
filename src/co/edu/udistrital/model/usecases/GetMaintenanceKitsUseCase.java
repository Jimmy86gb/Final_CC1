package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetMaintenanceKitsUseCase {

	
	private final KitRepository kitRepository;

	
	public GetMaintenanceKitsUseCase(KitRepository kitRepository) {
		this.kitRepository = kitRepository;
	}

	
	public SimpleList<KitDTO> execute() {

		SimpleList<KitDTO> resultList = new SimpleList<KitDTO>();
		SimpleList<Kit> stackList = kitRepository.getMaintenanceKits();
		Iterator<Kit> iterator = stackList.iterador();

		boolean isTopElement = true;

		while (iterator.hasNext()) {
			Kit kit = iterator.Next();

			String type = kit.getType().getDisplayName();
			String status = kit.getStatus().getDisplayName();

			
			KitDTO dto = new KitDTO(kit.getId().toString(), type, status, isTopElement);
			resultList.add(dto);

			
			isTopElement = false;
		}

		return resultList;
	}
}
