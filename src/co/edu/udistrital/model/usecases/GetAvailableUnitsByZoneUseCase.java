package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 *
 *
 * @author Juan David Diaz Perez
 */
public class GetAvailableUnitsByZoneUseCase {

	/**
	 * Instancia privada del factory de zonas
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();
	/**
	 * Instancia privada del reporitorio de memoria de unidades de servicio
	 */
	private final ServiceUnitRepository serviceUnitRepository;

	/**
	 * Constructor que inyecta dentro del caso de uso el repositorio de unidades de
	 * servicio
	 * 
	 * @param serviceUnitRepository Repositorio de unidades de servicio
	 */
	public GetAvailableUnitsByZoneUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la vista ya filtrada
	 * 
	 * @return Lista basada en Service Unit
	 */
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
