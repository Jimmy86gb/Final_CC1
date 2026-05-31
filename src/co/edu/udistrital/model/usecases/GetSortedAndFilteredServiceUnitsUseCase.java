package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista completa
 * ordenada con opciones de eliminar segun especificos de unidades de servicio
 *
 * @author Juan David Diaz Perez
 */
public class GetSortedAndFilteredServiceUnitsUseCase {

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
	public GetSortedAndFilteredServiceUnitsUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	/**
	 * Metodo de apoyo que genera la lista ordenada final con base en el criterio
	 * del repositorio
	 * 
	 * @return Lista ordenada
	 */
	private SimpleList<ServiceUnit> buildOrderedList() {
		SimpleList<ServiceUnit> orderedList = new SimpleList<ServiceUnit>();

		SimpleList<ServiceUnit> motorcicleAvailableList = serviceUnitRepository
				.getUnitsByStatusAndType(UnitStatus.AVAILABLE, UnitType.MOTORCYCLE);
		SimpleList<ServiceUnit> motorcicleMaintenanceList = serviceUnitRepository
				.getUnitsByStatusAndType(UnitStatus.MAINTENANCE, UnitType.MOTORCYCLE);
		SimpleList<ServiceUnit> motorcicleAssignedList = serviceUnitRepository
				.getUnitsByStatusAndType(UnitStatus.ASSIGNED, UnitType.MOTORCYCLE);
		SimpleList<ServiceUnit> carAvailableList = serviceUnitRepository.getUnitsByStatusAndType(UnitStatus.AVAILABLE,
				UnitType.CAR);
		SimpleList<ServiceUnit> carMaintenanceList = serviceUnitRepository
				.getUnitsByStatusAndType(UnitStatus.MAINTENANCE, UnitType.CAR);
		SimpleList<ServiceUnit> carAssignedList = serviceUnitRepository.getUnitsByStatusAndType(UnitStatus.ASSIGNED,
				UnitType.CAR);
		SimpleList<ServiceUnit> truckAvailableList = serviceUnitRepository.getUnitsByStatusAndType(UnitStatus.AVAILABLE,
				UnitType.TRUCK);
		SimpleList<ServiceUnit> truckMaintenanceList = serviceUnitRepository
				.getUnitsByStatusAndType(UnitStatus.MAINTENANCE, UnitType.TRUCK);
		SimpleList<ServiceUnit> truckAssignedList = serviceUnitRepository.getUnitsByStatusAndType(UnitStatus.ASSIGNED,
				UnitType.TRUCK);
		SimpleList<ServiceUnit> craneAvailableList = serviceUnitRepository.getUnitsByStatusAndType(UnitStatus.AVAILABLE,
				UnitType.CRANE);
		SimpleList<ServiceUnit> craneMaintenanceList = serviceUnitRepository
				.getUnitsByStatusAndType(UnitStatus.MAINTENANCE, UnitType.CRANE);
		SimpleList<ServiceUnit> craneAssignedList = serviceUnitRepository.getUnitsByStatusAndType(UnitStatus.ASSIGNED,
				UnitType.CRANE);

		Iterator<ServiceUnit> motorcicleAvalilableIterator = motorcicleAvailableList.iterador();
		Iterator<ServiceUnit> motorcicleMaintenanceIterator = motorcicleMaintenanceList.iterador();
		Iterator<ServiceUnit> motorcicleAssignedIterator = motorcicleAssignedList.iterador();
		Iterator<ServiceUnit> carAvalilableIterator = carAvailableList.iterador();
		Iterator<ServiceUnit> carMaintenanceIterator = carMaintenanceList.iterador();
		Iterator<ServiceUnit> carAssignedIterator = carAssignedList.iterador();
		Iterator<ServiceUnit> truckAvalilableIterator = truckAvailableList.iterador();
		Iterator<ServiceUnit> truckMaintenanceIterator = truckMaintenanceList.iterador();
		Iterator<ServiceUnit> truckAssignedIterator = truckAssignedList.iterador();
		Iterator<ServiceUnit> craneAvalilableIterator = craneAvailableList.iterador();
		Iterator<ServiceUnit> craneMaintenanceIterator = craneMaintenanceList.iterador();
		Iterator<ServiceUnit> craneAssignedIterator = craneAssignedList.iterador();

		while (motorcicleAvalilableIterator.hasNext()) {
			orderedList.add(motorcicleAvalilableIterator.Next());
		}

		while (motorcicleMaintenanceIterator.hasNext()) {
			orderedList.add(motorcicleMaintenanceIterator.Next());
		}

		while (motorcicleAssignedIterator.hasNext()) {
			orderedList.add(motorcicleAssignedIterator.Next());
		}

		while (carAvalilableIterator.hasNext()) {
			orderedList.add(carAvalilableIterator.Next());
		}

		while (carMaintenanceIterator.hasNext()) {
			orderedList.add(carMaintenanceIterator.Next());
		}

		while (carAssignedIterator.hasNext()) {
			orderedList.add(carAssignedIterator.Next());
		}

		while (truckAvalilableIterator.hasNext()) {
			orderedList.add(truckAvalilableIterator.Next());
		}

		while (truckMaintenanceIterator.hasNext()) {
			orderedList.add(truckMaintenanceIterator.Next());
		}

		while (truckAssignedIterator.hasNext()) {
			orderedList.add(truckAssignedIterator.Next());
		}
		while (craneAvalilableIterator.hasNext()) {
			orderedList.add(craneAvalilableIterator.Next());
		}

		while (craneMaintenanceIterator.hasNext()) {
			orderedList.add(craneMaintenanceIterator.Next());
		}

		while (craneAssignedIterator.hasNext()) {
			orderedList.add(craneAssignedIterator.Next());
		}

		return orderedList;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la vista ya ordenada
	 * 
	 * @return Lista basada en kitDTO
	 */
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

			ServiceUnitDTO dto = new ServiceUnitDTO(serviceUnit.getId(), type, status, zone, canEdit);

			resultList.add(dto);
		}

		return resultList;
	}
}
