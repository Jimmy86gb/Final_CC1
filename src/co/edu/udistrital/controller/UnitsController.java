package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.UnitsView;

/**
 * Sub-controlador responsable de la gestion, seguimiento y control de las unidades de servicio.
 * Actua como el gestor del parque automotor (gruas, motos, camionetas), mediando entre la 
 * interfaz visual (UnitsView) y la logica de negocio. Implementa un patron de autorizacion 
 * en dos pasos, donde las solicitudes de cambio de estado o zona quedan en una pila 
 * pendiente de aprobacion por parte de un administrador.
 * * @author Jimmy86gb
 */
public class UnitsController {

	private final AppController appController;
	private final UnitsView view;

	private final RegisterServiceUnitUseCase registerServiceUnitUseCase;
	private final UpdateServiceUnitUseCase updateServiceUnitUseCase;
	private final ApproveUnitStatusUseCase approveUnitStatusUseCase;
	private final RejectUnitStatusUseCase rejectUnitStatusUseCase;
	private final GetToConfirmServiceUnitsUseCase getToConfirmServiceUnitsUseCase;
	private final GetServiceUnitsStatusLabelsUseCase getServiceUnitsStatusLabelsUseCase;
	private final GetServiceUnitsTypeLabelsUseCase getServiceUnitsTypeLabelsUseCase;
	private final GetSortedAndFilteredServiceUnitsUseCase getSortedAndFilteredServiceUnitsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;

	/**
	 * Constructor de la clase UnitsController.
	 * Ensambla el controlador inicializando la vista correspondiente y todos los casos 
	 * de uso requeridos para administrar el ciclo de vida y la logica de aprobacion 
	 * de las unidades, inyectando el repositorio especifico.
	 * * @param appController Controlador principal para sincronizacion de vistas y manejo de respuestas.
	 * @param role Rol del usuario actual para ajustar los permisos de aprobacion o edicion en la interfaz.
	 * @param unitRepo Repositorio en memoria que gestiona la persistencia de las unidades de servicio.
	 */
	public UnitsController(AppController appController, String role, ServiceUnitRepository unitRepo) {
		this.appController = appController;
		this.view = new UnitsView(role);
		this.view.setController(this);

		this.registerServiceUnitUseCase = new RegisterServiceUnitUseCase(unitRepo);
		this.updateServiceUnitUseCase = new UpdateServiceUnitUseCase(unitRepo);
		this.approveUnitStatusUseCase = new ApproveUnitStatusUseCase(unitRepo);
		this.rejectUnitStatusUseCase = new RejectUnitStatusUseCase(unitRepo);
		this.getToConfirmServiceUnitsUseCase = new GetToConfirmServiceUnitsUseCase(unitRepo);
		this.getServiceUnitsStatusLabelsUseCase = new GetServiceUnitsStatusLabelsUseCase();
		this.getServiceUnitsTypeLabelsUseCase = new GetServiceUnitsTypeLabelsUseCase();
		this.getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(unitRepo);
		this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();
	}

	/**
	 * Obtiene la vista asociada a este sub-controlador.
	 * * @return Objeto UnitsView con la interfaz grafica de gestion de unidades.
	 */
	public UnitsView getView() { return view; }

	/**
	 * Refresca los paneles visuales de las unidades de servicio.
	 * Segrega logicamente las unidades, dibujando en columnas separadas aquellas 
	 * que operan normalmente y aquellas que tienen una solicitud de cambio retenida 
	 * en la pila de aprobacion, evitando duplicidades visuales.
	 */
	public void refreshView() {
		view.clearTable();
        
		SimpleList<ServiceUnitDTO> pendingUnits = getToConfirmServiceUnitsUseCase.execute();
		SimpleList<ServiceUnitDTO> allUnits = getSortedAndFilteredServiceUnitsUseCase.execute();
        
		// Se itera sobre todas las unidades para dibujar solo las activas
		SimpleList.Iterator<ServiceUnitDTO> uIt = allUnits.iterador();
		while (uIt.hasNext()) {
			ServiceUnitDTO activeUnit = uIt.Next();
			boolean isPending = false;
            
			// Validacion cruzada: Si esta en la pila de pendientes, se ignora en este pase
			SimpleList.Iterator<ServiceUnitDTO> checkIt = pendingUnits.iterador();
			while (checkIt.hasNext()) {
				if (checkIt.Next().getId().equals(activeUnit.getId())) {
					isPending = true;
					break;
				}
			}
            
			if (!isPending) {
				view.addUnit(activeUnit, false);
			}
		}
        
		// Se dibuja la pila de unidades pendientes de aprobacion
		SimpleList.Iterator<ServiceUnitDTO> pIt = pendingUnits.iterador();
		while (pIt.hasNext()) {
			view.addUnit(pIt.Next(), true);
		}
	}

	/**
	 * Ejecuta el registro de un nuevo lote de unidades de servicio en el sistema.
	 * * @param type Tipo o categoria de la unidad (ej. Grua, Moto, Camioneta).
	 * @param zone Zona geografica base asignada a las unidades.
	 * @param quantity Numero de unidades a instanciar en este lote.
	 */
	public void registerUnit(String type, String zone, int quantity) {
		appController.handleResponse(registerServiceUnitUseCase.execute(type, zone, quantity), appController::refreshAllViews);
	}

	/**
	 * Solicita la actualizacion de los parametros operativos de una unidad.
	 * La operacion no es inmediata, sino que traslada la unidad a la pila de aprobacion
	 * a la espera del visto bueno de un administrador.
	 * * @param id Identificador inmutable de la unidad.
	 * @param type Tipo de unidad.
	 * @param status Nuevo estado operativo solicitado.
	 * @param zone Nueva zona geografica solicitada.
	 */
	public void updateServiceUnit(String id, String type, String status, String zone) {
		appController.handleResponse(updateServiceUnitUseCase.execute(id, type, status, zone), appController::refreshAllViews);
	}

	/**
	 * Aprueba y hace efectivo el cambio de estado de la unidad que se encuentra 
	 * en el tope de la pila de confirmacion (solo accesible por administradores).
	 */
	public void approveUnitStatus() {
		appController.handleResponse(approveUnitStatusUseCase.execute(), appController::refreshAllViews);
	}

	/**
	 * Rechaza la solicitud de cambio que se encuentra en el tope de la pila de confirmacion,
	 * restaurando la unidad a sus valores operativos previos.
	 */
	public void rejectUnitStatus() {
		appController.handleResponse(rejectUnitStatusUseCase.execute(), appController::refreshAllViews);
	}

	/**
	 * Obtiene el listado de etiquetas validas para los tipos de unidades vehiculares.
	 * @return Estructura SimpleList con los nombres de las categorias.
	 */
	public SimpleList<String> getUnitTypeLabels() { return getServiceUnitsTypeLabelsUseCase.execute(); }

	/**
	 * Obtiene el listado de etiquetas validas para los estados de operacion de una unidad.
	 * @return Estructura SimpleList con los nombres de los estados.
	 */
	public SimpleList<String> getUnitStatusLabels() { return getServiceUnitsStatusLabelsUseCase.execute(); }

	/**
	 * Obtiene el listado de etiquetas validas para las zonas geograficas de cobertura.
	 * @return Estructura SimpleList con los nombres de las zonas.
	 */
	public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
}