package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.ClientsView;


public class ClientsController {

	private final AppController appController;
	private final ClientsView view;

	private final RegisterClientUseCase registerClientUseCase;
	private final UpdateClientUseCase updateClientUseCase;
	private final GetSortedAndFilteredClientsUseCase getSortedClientsUseCase;
	private final GetClientTypeLabelsUseCase getClientTypeLabelsUseCase;

	
	public ClientsController(AppController appController, String role, ClientRepository clientRepo) {
		this.appController = appController;
		this.view = new ClientsView(role);
		this.view.setController(this);

		this.registerClientUseCase = new RegisterClientUseCase(clientRepo);
		this.updateClientUseCase = new UpdateClientUseCase(clientRepo);
		this.getSortedClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepo);
		this.getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();
	}

	
	public ClientsView getView() { return view; }

	
	public void refreshView() {
		view.clearTable();
		SimpleList.Iterator<ClientDTO> cIt = getSortedClientsUseCase.execute().iterador();
		while (cIt.hasNext()) {
			view.addClient(cIt.Next());
		}
	}

	
	public void registerClient(String id, String name, String type, String contact) {
		appController.handleResponse(registerClientUseCase.ResponseDTO(id, name, type, contact), appController::refreshAllViews);
	}

	
	public void updateClient(String id, String name, String type, String contact) {
		appController.handleResponse(updateClientUseCase.execute(id, name, type, contact), appController::refreshAllViews);
	}

	
	public SimpleList<String> getClientTypeLabels() {
		return getClientTypeLabelsUseCase.execute();
	}

	
	public void showAddClientDialog(String clientId) {
		view.showAddClientDialog(clientId); 
	}
}