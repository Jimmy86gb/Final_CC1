package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista completa
 * ordenada con opciones de eliminar segun especificos de clientes
 *
 * @author Juan David Diaz Perez
 */
public class GetSortedAndFilteredClientsUseCase {

	/**
	 * Instanciacion privada del repositorio de memoria de clientes
	 */
	private final ClientRepository clientRepository;

	/**
	 * Constructor que inyecta el repositorio de clientes al caso de uso
	 * 
	 * @param clientRepository Respositorio de memoria de clientes
	 */
	public GetSortedAndFilteredClientsUseCase(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	/**
	 * Metodo de apoyo que genera la lista ordenada final con base en el criterio
	 * del repositorio
	 * 
	 * @return Lista ordenada
	 */
	private SimpleList<Client> buildOrderedList() {
		SimpleList<Client> orderedList = new SimpleList<Client>();

		SimpleList<Client> privateList = clientRepository.getClientsByType(ClientType.PRIVATE);
		SimpleList<Client> insuranceList = clientRepository.getClientsByType(ClientType.INSURANCE);
		SimpleList<Client> companyList = clientRepository.getClientsByType(ClientType.TRANSPORT_COMPANY);

		Iterator<Client> privateIterator = privateList.iterador();
		Iterator<Client> insuranceIterator = insuranceList.iterador();
		Iterator<Client> companyIterator = companyList.iterador();

		while (insuranceIterator.hasNext()) {
			orderedList.add(insuranceIterator.Next());
		}

		while (companyIterator.hasNext()) {
			orderedList.add(companyIterator.Next());
		}

		while (privateIterator.hasNext()) {
			orderedList.add(privateIterator.Next());
		}

		return orderedList;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la vista ya ordenada
	 * 
	 * @return Lista basada en kitDTO
	 */
	public SimpleList<ClientDTO> execute() {

		SimpleList<ClientDTO> resultList = new SimpleList<ClientDTO>();

		SimpleList<Client> orderedList = buildOrderedList();

		Iterator<Client> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Client client = iterator.Next();

			String type = client.getType().getDisplayName();

			ClientDTO dto = new ClientDTO(client.getId(), client.getName(), type, client.getContactInfo());

			resultList.add(dto);
		}

		return resultList;
	}
}
