package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.KitDTO;
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
	 * Metodo de apoyo optimizado que genera la lista ordenada final con base en el
	 * criterio del repositorio para Kits.
	 * 
	 * @return Lista ordenada
	 */
	private SimpleList<Kit> buildOrderedList() {
		SimpleList<Kit> orderedList = new SimpleList<Kit>();

		UnitStatus[] statusOrder = { UnitStatus.AVAILABLE, UnitStatus.ASSIGNED, UnitStatus.MAINTENANCE };

		for (int i = 0; i < statusOrder.length; i++) {

			SimpleList<Kit> subList = kitRepository.getKitsByStatus(statusOrder[i]);
			Iterator<Kit> iterator = subList.iterador();

			while (iterator.hasNext()) {
				orderedList.add(iterator.Next());
			}
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
