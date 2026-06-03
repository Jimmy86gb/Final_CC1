package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.repositories.ClientRepository;

/**
 * Clase que representa un caso de uso de retorno del DTO de un cliente en
 * especifico
 *
 * @author Juan David Diaz Perez
 */
public class GetClientByIDUseCase {

	/**
	 * Instanciacion privada del repositorio de memoria de clientes
	 */
	private final ClientRepository clientRepository;

	/**
	 * Constructor que inyecta el repositorio de clientes al caso de uso
	 * 
	 * @param clientRepository Respositorio de memoria de clientes
	 */
	public GetClientByIDUseCase(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	/**
	 * Metodo encargado de retornar el DTO de un cliente en especifico
	 * 
	 * @return DTO del cliente actual
	 */
	public ClientDTO execute(String id) {

		Client client = clientRepository.getClientByID(id);

		if (client == null) {
			return null;
		} else {
			return new ClientDTO(id, client.getName(), client.getType().getDisplayName(), client.getContactInfo());
		}
	}
}
