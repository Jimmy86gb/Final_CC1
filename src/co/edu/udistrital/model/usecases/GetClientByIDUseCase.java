package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.repositories.ClientRepository;


public class GetClientByIDUseCase {

	
	private final ClientRepository clientRepository;

	
	public GetClientByIDUseCase(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	
	public ClientDTO execute(String id) {

		Client client = clientRepository.getClientByID(id);

		if (client == null) {
			return null;
		} else {
			return new ClientDTO(id, client.getName(), client.getType().getDisplayName(), client.getContactInfo());
		}
	}
}
