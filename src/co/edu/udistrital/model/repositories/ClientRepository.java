package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class ClientRepository {

	
	private SimpleList<Client> clientList;

	
	public ClientRepository() {
		this.clientList = new SimpleList<Client>();
	}

	
	public boolean saveClient(Client client) {
		if (clientList.contains(client)) {
			return false;
		}

		clientList.add(client);

		return true;
	}

	
	public boolean update(Client actualClient, Client newClient) {
		return clientList.update(actualClient, newClient);
	}

	
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

	
	public SimpleList<Client> getAllClients() {
		SimpleList<Client> copyList = new SimpleList<Client>();
		Iterator<Client> iterator = this.clientList.iterador();
		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	
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

	
	public SimpleList<Client> getClientList() {
		return clientList;
	}

	
	public void setClientList(SimpleList<Client> clientList) {
		this.clientList = clientList;
	}
}
