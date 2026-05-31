package co.edu.udistrital.usecases;

import co.edu.udistrital.dtos.KitDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista completa
 * ordenada con opciones de eliminar segun especificos
 *
 * @author Juan David Diaz Perez
 */
public class GetSortedAndFilteredKitsUseCase {

	/**
	 * Instaciacion privada del repositorio de memoria de kits
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta el repositorio de kits al caso de uso
	 * 
	 * @param kitRepository Repositorio de memoria de kits
	 */
	public GetSortedAndFilteredKitsUseCase(KitRepository kitRepository) {

		this.kitRepository = kitRepository;
	}

	/**
	 * Metodo de apoyo que genera la lista ordenada final con base en el criterio
	 * del repositorio
	 * 
	 * @return Lista ordenada
	 */
	private SimpleList<Kit> buildOrderedList() {
		SimpleList<Kit> orderedList = new SimpleList<Kit>();

		SimpleList<Kit> availableList = kitRepository.getKitsByStatus(UnitStatus.AVAILABLE);
		SimpleList<Kit> assignedList = kitRepository.getKitsByStatus(UnitStatus.ASSIGNED);
		SimpleList<Kit> maintenanceList = kitRepository.getKitsByStatus(UnitStatus.MAINTENANCE);

		Iterator<Kit> availableIterator = availableList.iterador();
		Iterator<Kit> assignedIterator = assignedList.iterador();
		Iterator<Kit> maintenanceIterator = maintenanceList.iterador();

		while (availableIterator.hasNext()) {
			orderedList.add(availableIterator.Next());
		}

		while (assignedIterator.hasNext()) {
			orderedList.add(assignedIterator.Next());
		}

		while (maintenanceIterator.hasNext()) {
			orderedList.add(maintenanceIterator.Next());
		}

		return orderedList;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la vista ya ordenada
	 * 
	 * @return Lista basada en kitDTO
	 */
	public SimpleList<KitDTO> execute() {
		SimpleList<KitDTO> resultList = new SimpleList<KitDTO>();

		SimpleList<Kit> orderedList = buildOrderedList();

		Iterator<Kit> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Kit kit = iterator.Next();

			boolean canEdit = (kit.getStatus() == UnitStatus.AVAILABLE);

			String type = kit.getType().getDisplayName();
			String status = kit.getStatus().getDisplayName();

			KitDTO dto = new KitDTO(kit.getId(), type, status, canEdit);

			resultList.add(dto);
		}

		return resultList;
	}
}
