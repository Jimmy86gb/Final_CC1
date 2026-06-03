package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetAvailableUnitsByZoneUseCase {

	
	private final ZoneFactory zoneFactory = new ZoneFactory();
	
	private final ServiceUnitRepository serviceUnitRepository;

	
	public GetAvailableUnitsByZoneUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	
	public SimpleList<ServiceUnitDTO> execute(String zone) {

		OperationZone operationZone = zoneFactory.generateOperationZone(zone);

		SimpleList<ServiceUnitDTO> resultList = new SimpleList<>();

		SimpleList<ServiceUnit> orderedList = serviceUnitRepository.getAvailableUnitsByZone(operationZone);

		Iterator<ServiceUnit> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			ServiceUnit serviceUnit = iterator.Next();

			String type = serviceUnit.getType().getDisplayName();
			String status = serviceUnit.getStatus().getDisplayName();

			ServiceUnitDTO dto = new ServiceUnitDTO(serviceUnit.getId().toString(), type, status, zone, false);

			resultList.add(dto);
		}

		return resultList;
	}
}
