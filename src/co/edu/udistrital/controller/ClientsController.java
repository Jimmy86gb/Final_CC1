package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.ClientsView;

/**
 * Sub-controlador encargado de la gestion y administracion del modulo de Clientes.
 * Actua como mediador especifico entre la vista del directorio de clientes (ClientsView) 
 * y los casos de uso correspondientes a las operaciones de registro, actualizacion 
 * y consulta de clientes en el sistema.
 * * @author Jimmy86gb
 */
public class ClientsController {

	private final AppController appController;
	private final ClientsView view;

	private final RegisterClientUseCase registerClientUseCase;
	private final UpdateClientUseCase updateClientUseCase;
	private final GetSortedAndFilteredClientsUseCase getSortedClientsUseCase;
	private final GetClientTypeLabelsUseCase getClientTypeLabelsUseCase;

	/**
	 * Constructor de la clase ClientsController.
	 * Inicializa la vista correspondiente y configura los casos de uso necesarios 
	 * para la logica de negocio, inyectando el repositorio de clientes.
	 * * @param appController Controlador principal para el manejo global de respuestas y refresco de vistas.
	 * @param role Rol del usuario actual para definir los permisos visuales en la interfaz.
	 * @param clientRepo Repositorio en memoria que contiene los datos de los clientes.
	 */
	public ClientsController(AppController appController, String role, ClientRepository clientRepo) {
		this.appController = appController;
		this.view = new ClientsView(role);
		this.view.setController(this);

		this.registerClientUseCase = new RegisterClientUseCase(clientRepo);
		this.updateClientUseCase = new UpdateClientUseCase(clientRepo);
		this.getSortedClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepo);
		this.getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();
	}

	/**
	 * Obtiene la vista asociada a este controlador sub-modular.
	 * * @return Objeto ClientsView con la interfaz grafica del directorio de clientes.
	 */
	public ClientsView getView() { return view; }

	/**
	 * Refresca la tabla visual de clientes.
	 * Limpia los registros actuales en la interfaz y solicita al caso de uso la lista 
	 * actualizada y ordenada para volver a dibujarlos mediante iteracion nativa.
	 */
	public void refreshView() {
		view.clearTable();
		SimpleList.Iterator<ClientDTO> cIt = getSortedClientsUseCase.execute().iterador();
		while (cIt.hasNext()) {
			view.addClient(cIt.Next());
		}
	}

	/**
	 * Ejecuta el proceso de registro de un nuevo cliente en el sistema.
	 * Delega la validacion y notificacion del resultado al controlador principal (AppController).
	 * * @param id Identificacion (Cedula o NIT) del cliente.
	 * @param name Nombre completo o razon social.
	 * @param type Tipo de cliente (Ej. Particular, Seguros, Empresarial).
	 * @param contact Informacion de contacto (Telefono o correo electronico).
	 */
	public void registerClient(String id, String name, String type, String contact) {
		appController.handleResponse(registerClientUseCase.ResponseDTO(id, name, type, contact), appController::refreshAllViews);
	}

	/**
	 * Ejecuta el proceso de actualizacion de datos para un cliente existente.
	 * Posterior a la operacion, notifica al controlador principal para refrescar el sistema.
	 * * @param id Identificacion inmutable del cliente a editar.
	 * @param name Nuevo nombre o razon social.
	 * @param type Nuevo tipo de categorizacion del cliente.
	 * @param contact Nueva informacion de contacto.
	 */
	public void updateClient(String id, String name, String type, String contact) {
		appController.handleResponse(updateClientUseCase.execute(id, name, type, contact), appController::refreshAllViews);
	}

	/**
	 * Obtiene el listado de etiquetas validas para los tipos de clientes.
	 * Utilizado principalmente para poblar dinamicamente los ComboBox de la vista.
	 * * @return Estructura SimpleList conteniendo los nombres de los tipos de cliente.
	 */
	public SimpleList<String> getClientTypeLabels() {
		return getClientTypeLabelsUseCase.execute();
	}

	/**
	 * Despliega el cuadro de dialogo nativo de la vista para registrar un nuevo cliente.
	 * Este metodo es util para llamadas cruzadas, por ejemplo, cuando se busca un cliente
	 * en el modulo de despacho y no existe.
	 * * @param clientId ID que se precargara en el campo de texto del formulario (puede ser vacio).
	 */
	public void showAddClientDialog(String clientId) {
		view.showAddClientDialog(clientId); // Asegurate de que este metodo sea public en ClientsView
	}
}