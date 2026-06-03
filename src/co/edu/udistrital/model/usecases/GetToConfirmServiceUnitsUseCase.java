package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetToConfirmServiceUnitsUseCase {

	
	private final ServiceUnitRepository serviceUnitRepository;

	
	public GetToConfirmServiceUnitsUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	
	public SimpleList<ServiceUnitDTO> execute() {

		SimpleList<ServiceUnitDTO> resultList = new SimpleList<>();
		SimpleList<ServiceUnit> stackList = serviceUnitRepository.getToConfirmUnits();
		Iterator<ServiceUnit> iterator = stackList.iterador();

		boolean isTopElement = true;

		while (iterator.hasNext()) {
			ServiceUnit serviceUnit = iterator.Next();

			String type = serviceUnit.getType().getDisplayName();
			String status = serviceUnit.getStatus().getDisplayName();
			String zone = serviceUnit.getZone().getDisplayName();

			
			ServiceUnitDTO dto = new ServiceUnitDTO(serviceUnit.getId().toString(), type, status, zone, isTopElement);
			resultList.add(dto);

			
			isTopElement = false;
		}

		return resultList;
	}

}
