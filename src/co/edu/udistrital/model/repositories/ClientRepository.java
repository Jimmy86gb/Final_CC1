package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase que representa la memoria y administracion referente a los clientes en
 * el sistema
 *
 * @author Juan David Diaz Perez
 */
public class ClientRepository {

	/**
	 * Lista de clientes que almacenara a todos en memoria
	 */
	private SimpleList<Client> clientList;

	/**
	 * Constructor que inicializa la lista de datos de tipo cliente
	 */
	public ClientRepository() {
		this.clientList = new SimpleList<Client>();
	}

	/**
	 * Metodo que guarda un nuevo cliente en la memoria de clientes
	 * 
	 * @param client Objeto de cliente
	 * @return si la operacion fue un exito o no
	 */
	public boolean saveClient(Client client) {
		if (clientList.contains(client)) {
			return false;
		}

		clientList.add(client);

		return true;
	}

	/**
	 * Metodo que actualiza la informacion de un cliente selecionado
	 * 
	 * @param actualClient Objeto cliente con la informacion actual
	 * @param newClient    Objeto cliente con la informacion nueva
	 * @return Si la operacion fue exitosa
	 */
	public boolean update(Client actualClient, Client newClient) {
		return clientList.update(actualClient, newClient);
	}

	/**
	 * Metodo que retorna si exite un elemento cliente de la lista segun una
	 * busqueda por ID
	 * 
	 * @param id Id para buscar la lista
	 * @return Elemento cliente con la id dada
	 */
	public Client getClientByID(String id) {
		Iterator<Client> iterator = this.clientList.iterador();

		while (iterator.hasNext()) {
			Client currentClient = iterator.Next();

			if (currentClient.getId().equals(id)) {
				return currentClient;
			}
		}

		return null;
	}

	/**
	 * Metodo que retorna toda la lista de clientes
	 * 
	 * @return La lista de clientes
	 */
	public SimpleList<Client> getAllClients() {
		SimpleList<Client> copyList = new SimpleList<Client>();
		Iterator<Client> iterator = this.clientList.iterador();
		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	/**
	 * Metodo que retorna la lista de clientes segun el tipo que sea
	 * 
	 * @param targetType El tipo de cliente a retornar
	 * @return La lista especifica de ese tipo de clientes
	 */
	public SimpleList<Client> getClientsByType(ClientType targetType) {
		SimpleList<Client> filteredList = new SimpleList<Client>();
		Iterator<Client> clientIterator = clientList.iterador();

		while (clientIterator.hasNext()) {
			Client actualClient = clientIterator.Next();
			if (actualClient.getType() == targetType) {
				filteredList.add(actualClient);
			}
		}
		return filteredList;
	}

	/**
	 * @return Los clientes actuales del sistema
	 */
	public SimpleList<Client> getClientList() {
		return clientList;
	}

	/**
	 * @param clientList Los clientes a inicializar en el sistema
	 */
	public void setClientList(SimpleList<Client> clientList) {
		this.clientList = clientList;
	}
}
