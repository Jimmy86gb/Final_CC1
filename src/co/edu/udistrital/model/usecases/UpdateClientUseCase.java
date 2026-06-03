package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientFactory;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.repositories.ClientRepository;


public class UpdateClientUseCase {

	
	private final ClientFactory clientFactory = new ClientFactory();

	
	private final ClientRepository clientRepository;

	
	public UpdateClientUseCase(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	
	public ResponseDTO execute(String id, String name, String typeClient, String contact) {
		try {
			ClientType clientType = clientFactory.generateClientType(typeClient);

			Client actualClient = clientRepository.getClientByID(id);

			if (actualClient == null) {
				return new ResponseDTO(false,
						"El cliente no se encontró en el sistema. Es posible que haya sido eliminado.");
			}

			Client newClient = new Client(id, name, clientType, contact);

			boolean isUpdated = clientRepository.update(actualClient, newClient);

			if (isUpdated) {
				return new ResponseDTO(true, "La información del cliente " + name + " fue actualizada exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo actualizar la información del cliente.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al actualizar los datos del cliente.");
		}
	}
}
