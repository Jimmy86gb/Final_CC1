package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ActionType;
import co.edu.udistrital.model.enums.ClientFactory;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.repositories.ActionLogRepository;
import co.edu.udistrital.model.repositories.ClientRepository;

/**
 * Clase de caso de uso que se encarga de actualizar la informacion de los
 * clientes recibida desde la vista
 *
 * @author Juan David Diaz Perez
 */
public class UpdateClientUseCase {

	/**
	 * Intancia privada del factory del tipo de cliente
	 */
	private final ClientFactory clientFactory = new ClientFactory();

	/**
	 * Instancia privada del repositorio de manejo de memoria de clientes
	 */
	private final ClientRepository clientRepository;

	/**
	 * Repositorio global de historial de acciones (Memento)
	 */
	private final ActionLogRepository logRepo;

	/**
	 * Constructor que inyecta el repositorio de clientes al caso de uso
	 * 
	 * @param clientRepository Repositorio de clientes
	 */
	public UpdateClientUseCase(ClientRepository clientRepository, ActionLogRepository logRepo) {
		this.clientRepository = clientRepository;
		this.logRepo = logRepo;
	}

	/**
	 * Ejecuta el caso de uso para actualizar el cliente actual
	 * 
	 * @param id         ID del cliente a actualizar
	 * @param name       Nombre del cliente a actualizar
	 * @param typeClient Tipo de cliente a actualizar
	 * @param contact    Contacto del cliente a actualizar
	 * @return Objeto ResponseDTO con el estado de la operacion y el mensaje para la
	 *         UI
	 */
	public ResponseDTO execute(String id, String name, String typeClient, String contact) {
		try {
			ClientType clientType = clientFactory.generateClientType(typeClient);

			Client actualClient = clientRepository.getClientByID(id);

			if (actualClient == null) {
				return new ResponseDTO(false,
						"El cliente no se encontró en el sistema. Es posible que haya sido eliminado.");
			}

			logRepo.logAction(new ActionRecord("Modificación de Cliente: " + actualClient.getName(),
				ActionType.UPDATE_CLIENT, actualClient.getId(), actualClient.getName(),
				actualClient.getType().name(), actualClient.getContactInfo()));

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
