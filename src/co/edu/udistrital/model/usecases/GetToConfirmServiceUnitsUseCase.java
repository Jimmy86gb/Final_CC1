package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista de unidades
 * de servicio con cambios de estado con opcion de editar y eliminar para solo
 * el primer elemento
 *
 * @author Juan David Diaz Perez
 */
public class GetToConfirmServiceUnitsUseCase {

	/**
	 * Instancia privada del reporitorio de unidades de servicio
	 */
	private final ServiceUnitRepository serviceUnitRepository;

	/**
	 * Constructor que inyecta al caso de uso el repositorio de unidades de servicio
	 * 
	 * @param serviceUnitRepository repositorio de unidades de servicio
	 */
	public GetToConfirmServiceUnitsUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	/**
	 * Metodo encargado de retornar la lista de la pila a la vista dedicada a
	 * deshacer cambios de estado, solo el elemento del tope puede ser confirmado o
	 * deshecho
	 * 
	 * @return Lista basada en el DTO de unidades de servicio
	 */
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

			// El primer DTO recibe 'true', los siguientes recibirán 'false'
			ServiceUnitDTO dto = new ServiceUnitDTO(serviceUnit.getId(), type, status, zone, isTopElement);
			resultList.add(dto);

			// Apagamos la bandera para el resto de los elementos
			isTopElement = false;
		}

		return resultList;
	}

}
