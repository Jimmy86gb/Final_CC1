package co.edu.udistrital.usecases;

import co.edu.udistrital.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientFactory;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.repositories.ClientRepository;

/**
 * Clase que representa el caso de uso de registrar un nuevo cliente en el
 * sistema.
 *
 * @author Juan David Diaz Perez
 */
public class RegisterClientUseCase {

	/**
	 * Intancia privada del factory del tipo de cliente
	 */
	private final ClientFactory clientFactory = new ClientFactory();

	/**
	 * Instancia privada del repositorio de manejo de memoria de los clientes
	 */
	private final ClientRepository clientRepository;

	/**
	 * Constructor que inyecta el repositorio de cliente al caso de uso
	 * 
	 * @param clientRepository Repositorio de cliente caso de uso
	 */
	public RegisterClientUseCase(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	/**
	 * Ejecuta el caso de uso para registrar nuevo cliente
	 * 
	 * @param id         ID del cliente a registrar
	 * @param name       Nombre del cliente a registar
	 * @param typeClient Tipo de cliente a registrar
	 * @param contact    Contacto del clietne a registrar
	 * @return Objeto ResponseDTO con el estado de la operacion y el mensaje para la
	 *         UI
	 */
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
