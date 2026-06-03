package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientFactory;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.repositories.ClientRepository;


public class RegisterClientUseCase {

	
	private final ClientFactory clientFactory = new ClientFactory();

	
	private final ClientRepository clientRepository;

	
	public RegisterClientUseCase(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	
	public ResponseDTO ResponseDTO(String id, String name, String typeClient, String contact) {
		try {
			ClientType clientType = clientFactory.generateClientType(typeClient);

			Client newClient = new Client(id, name, clientType, contact);

			boolean isSaved = clientRepository.saveClient(newClient);

			if (isSaved) {
				return new ResponseDTO(true, "El cliente " + name + " fue registrado exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo registrar. El cliente ya existe en el sistema.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al registrar el técnico.");
		}
	}

}
