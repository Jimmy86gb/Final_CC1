package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetSortedAndFilteredClientsUseCase {

	
	private final ClientRepository clientRepository;

	
	public GetSortedAndFilteredClientsUseCase(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	
	private SimpleList<Client> buildOrderedList() {

		SimpleList<Client> orderedList = new SimpleList<Client>();

		ClientType[] typeOrder = { ClientType.INSURANCE, ClientType.TRANSPORT_COMPANY, ClientType.PRIVATE };

		for (int i = 0; i < typeOrder.length; i++) {

			SimpleList<Client> subList = clientRepository.getClientsByType(typeOrder[i]);
			Iterator<Client> iterator = subList.iterador();

			while (iterator.hasNext()) {
				orderedList.add(iterator.Next());
			}
		}

		return orderedList;
	}

	
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
