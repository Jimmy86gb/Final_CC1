package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetSortedAndFilteredKitsUseCase {

	
	private final KitRepository kitRepository;

	
	public GetSortedAndFilteredKitsUseCase(KitRepository kitRepository) {

		this.kitRepository = kitRepository;
	}

	
	private SimpleList<Kit> buildOrderedList() {
		SimpleList<Kit> orderedList = new SimpleList<Kit>();

		UnitStatus[] statusOrder = { UnitStatus.AVAILABLE, UnitStatus.ASSIGNED, UnitStatus.MAINTENANCE, UnitStatus.INACTIVE };

		for (int i = 0; i < statusOrder.length; i++) {

			SimpleList<Kit> subList = kitRepository.getKitsByStatus(statusOrder[i]);
			Iterator<Kit> iterator = subList.iterador();

			while (iterator.hasNext()) {
				orderedList.add(iterator.Next());
			}
		}

		return orderedList;
	}

	
	public SimpleList<KitDTO> execute() {
		SimpleList<KitDTO> resultList = new SimpleList<KitDTO>();

		SimpleList<Kit> orderedList = buildOrderedList();

		Iterator<Kit> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Kit kit = iterator.Next();

			boolean canEdit = (kit.getStatus() == UnitStatus.AVAILABLE);

			String type = kit.getType().getDisplayName();
			String status = kit.getStatus().getDisplayName();

			KitDTO dto = new KitDTO(kit.getId().toString(), type, status, canEdit);

			resultList.add(dto);
		}

		return resultList;
	}
}
