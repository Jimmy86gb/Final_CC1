package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista de kits en
 * mantenimiento con opcion de editar y eliminar para solo el primer elemento
 *
 * @author Juan David Diaz Perez
 */
public class GetMaintenanceKitsUseCase {

	/**
	 * Instaciacion privada del repositorio de memoria de kits
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta el repositorio de kits al caso de uso
	 * 
	 * @param kitRepository Repositorio de memoria de kits
	 */
	public GetMaintenanceKitsUseCase(KitRepository kitRepository) {
		this.kitRepository = kitRepository;
	}

	/**
	 * Metodo encargado de retornar la lista de la pila a la vista. Solo el kit en
	 * el tope de la pila (primer elemento) será editable.
	 * 
	 * @return Lista basada en KitDTO
	 */
	public SimpleList<KitDTO> execute() {

		SimpleList<KitDTO> resultList = new SimpleList<KitDTO>();
		SimpleList<Kit> stackList = kitRepository.getMaintenanceKits();
		Iterator<Kit> iterator = stackList.iterador();

		boolean isTopElement = true;

		while (iterator.hasNext()) {
			Kit kit = iterator.Next();

			String type = kit.getType().getDisplayName();
			String status = kit.getStatus().getDisplayName();

			// El primer DTO recibe 'true', los siguientes recibirán 'false'
			KitDTO dto = new KitDTO(kit.getId(), type, status, isTopElement);
			resultList.add(dto);

			// Apagamos la bandera para el resto de los elementos
			isTopElement = false;
		}

		return resultList;
	}
}
