package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.KitsView;

/**
 * Sub-controlador encargado de la gestion del inventario y ciclo de vida de los Kits.
 * Actua como mediador entre la vista de kits (KitsView) y los casos de uso 
 * correspondientes a la creacion, asignacion y mantenimiento de estos recursos.
 * Especialmente, orquesta la logica de pilas (LIFO) para el envio y retiro de kits 
 * en estado de reparacion.
 * * @author Jimmy86gb
 */
public class KitsController {

	private final AppController appController;
	private final KitsView view;

	private final RegisterKitUseCase registerKitUseCase;
	private final UpdateKitUseCase updateKitUseCase;
	private final GetSortedAndFilteredKitsUseCase getSortedKitsUseCase;
	private final RetireKitFromMaintenanceUseCase retireKitFromMaintenanceUseCase;
	private final ReturnKitToServiceUseCase returnKitToServiceUseCase;
	private final GetKitTypeLabelsUseCase getKitTypeLabelsUseCase;
	private final GetMaintenanceKitsUseCase getMaintenanceKitsUseCase;

	/**
	 * Constructor de la clase KitsController.
	 * Inicializa la vista de inventario y configura los casos de uso necesarios 
	 * para la logica de negocio, inyectando el repositorio especifico de kits.
	 * * @param appController Controlador principal para delegar respuestas y refrescos globales.
	 * @param role Rol del usuario actual para definir permisos visuales sobre el inventario.
	 * @param kitRepo Repositorio en memoria que almacena y gestiona los objetos Kit.
	 */
	public KitsController(AppController appController, String role, KitRepository kitRepo) {
		this.appController = appController;
		this.view = new KitsView(role);
		this.view.setController(this);

		this.registerKitUseCase = new RegisterKitUseCase(kitRepo);
		this.updateKitUseCase = new UpdateKitUseCase(kitRepo);
		this.getSortedKitsUseCase = new GetSortedAndFilteredKitsUseCase(kitRepo);
		this.retireKitFromMaintenanceUseCase = new RetireKitFromMaintenanceUseCase(kitRepo);
		this.returnKitToServiceUseCase = new ReturnKitToServiceUseCase(kitRepo);
		this.getKitTypeLabelsUseCase = new GetKitTypeLabelsUseCase();
		this.getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepo);
	}

	/**
	 * Obtiene la vista asociada a este sub-controlador.
	 * * @return Objeto KitsView que representa la interfaz grafica del inventario.
	 */
	public KitsView getView() { return view; }

	/**
	 * Refresca los paneles visuales de la vista de kits.
	 * Limpia los contenedores y carga de forma separada los kits que se encuentran 
	 * en la pila de mantenimiento y los kits que estan disponibles o inactivos.
	 */
	public void refreshView() {
		view.clearTable();
        
		// Se itera primero la pila de mantenimiento para dibujarlos en su columna respectiva
		SimpleList.Iterator<KitDTO> mIt = getMaintenanceKitsUseCase.execute().iterador();
		while (mIt.hasNext()) {
			view.addKit(mIt.Next());
		}
        
		// Se itera el resto del inventario, filtrando los que ya estan en mantenimiento
		SimpleList.Iterator<KitDTO> kIt = getSortedKitsUseCase.execute().iterador();
		while (kIt.hasNext()) {
			KitDTO kit = kIt.Next();
			if (!kit.getStatus().equalsIgnoreCase("En mantenimiento") && !kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
				view.addKit(kit);
			}
		}
	}

	/**
	 * Registra un nuevo lote de kits en el sistema.
	 * * @param type Categoria o tipo de kit a crear.
	 * @param quantity Cantidad de kits a instanciar en este lote.
	 */
	public void registerKit(String type, int quantity) {
		appController.handleResponse(registerKitUseCase.execute(type, quantity), appController::refreshAllViews);
	}

	/**
	 * Cambia el estado de un kit a "Mantenimiento", insertandolo logicamente 
	 * en la pila de reparacion del sistema.
	 * * @param id Identificador unico del kit.
	 * @param type Tipo de kit.
	 */
	public void updateKitToMaintenance(String id, String type) {
		appController.handleResponse(updateKitUseCase.execute(id, type, "Mantenimiento"), appController::refreshAllViews);
	}

	/**
	 * Cambia de manera directa el estado de un kit, utilizado principalmente 
	 * por el administrador para alternar entre "Disponible" e "Inactivo".
	 * * @param id Identificador unico del kit.
	 * @param type Tipo de kit.
	 * @param newStatus Nuevo estado a asignar.
	 */
	public void updateKitStatus(String id, String type, String newStatus) {
		appController.handleResponse(updateKitUseCase.execute(id, type, newStatus), appController::refreshAllViews);
	}

	/**
	 * Extrae el kit que se encuentra en el tope de la pila de mantenimiento (LIFO) 
	 * y lo retorna a servicio activo ("Disponible").
	 */
	public void returnKitToService() {
		appController.handleResponse(returnKitToServiceUseCase.execute(), appController::refreshAllViews);
	}

	/**
	 * Retira definitivamente del sistema (baja logica) el kit que se encuentra 
	 * en el tope de la pila de mantenimiento.
	 */
	public void retireKitFromMaintenance() {
		appController.handleResponse(retireKitFromMaintenanceUseCase.execute(), appController::refreshAllViews);
	}

	/**
	 * Obtiene el listado de etiquetas validas para los tipos de kit.
	 * * @return Estructura SimpleList con los nombres de las categorias de kits.
	 */
	public SimpleList<String> getKitTypeLabels() {
		return getKitTypeLabelsUseCase.execute();
	}
}