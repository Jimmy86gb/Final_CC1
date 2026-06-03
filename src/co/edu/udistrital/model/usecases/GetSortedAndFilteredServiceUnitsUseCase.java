package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetSortedAndFilteredServiceUnitsUseCase {

	
	private final ServiceUnitRepository serviceUnitRepository;

	
	public GetSortedAndFilteredServiceUnitsUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	
	private SimpleList<ServiceUnit> buildOrderedList() {
		SimpleList<ServiceUnit> orderedList = new SimpleList<ServiceUnit>();

		UnitType[] typeOrder = { UnitType.MOTORCYCLE, UnitType.CAR, UnitType.TRUCK, UnitType.CRANE };

		UnitStatus[] statusOrder = { UnitStatus.AVAILABLE, UnitStatus.MAINTENANCE, UnitStatus.ASSIGNED, UnitStatus.INACTIVE };

		for (int i = 0; i < typeOrder.length; i++) {
			for (int j = 0; j < statusOrder.length; j++) {

				SimpleList<ServiceUnit> subList = serviceUnitRepository.getUnitsByStatusAndType(statusOrder[j],
						typeOrder[i]);

				Iterator<ServiceUnit> iterator = subList.iterador();

				while (iterator.hasNext()) {
					orderedList.add(iterator.Next());
				}
			}
		}

		return orderedList;
	}

	
	public SimpleList<ServiceUnitDTO> execute() {

		SimpleList<ServiceUnitDTO> resultList = new SimpleList<>();

		SimpleList<ServiceUnit> orderedList = buildOrderedList();

		Iterator<ServiceUnit> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			ServiceUnit serviceUnit = iterator.Next();

			boolean canEdit = (serviceUnit.getStatus() == UnitStatus.AVAILABLE
					|| serviceUnit.getStatus() == UnitStatus.MAINTENANCE);

			String type = serviceUnit.getType().getDisplayName();
			String status = serviceUnit.getStatus().getDisplayName();
			String zone = serviceUnit.getZone().getDisplayName();

			ServiceUnitDTO dto = new ServiceUnitDTO(serviceUnit.getId().toString(), type, status, zone, canEdit);

			resultList.add(dto);
		}

		return resultList;
	}
}
