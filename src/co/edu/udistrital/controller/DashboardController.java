package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.DashboardView;

/**
 * Sub-controlador encargado de la logica y actualizacion del panel principal (Dashboard).
 * Actua como el cerebro detras de la visualizacion de estadisticas en tiempo real y 
 * la exportacion de reportes diarios, coordinando la informacion global extraida 
 * de los diferentes repositorios a traves de sus respectivos casos de uso.
 * 
 * @author Jimmy86gb
 */
public class DashboardController {

	private final AppController appController;
	private final DashboardView view;
	private final ActionLogRepository actionLogRepository;
	private final UndoGlobalActionUseCase undoGlobalActionUseCase;

	private final GetSortedAndFilteredServiceUnitsUseCase getSortedAndFilteredServiceUnitsUseCase;
	private final GetMaintenanceKitsUseCase getMaintenanceKitsUseCase;
	private final GetSortedAndFilteredReportsUseCase getSortedAndFilteredReportsUseCase;
	private final GenerateDailyCSVUseCase generateDailyCSVUseCase;

	/**
	 * Constructor de la clase DashboardController.
	 * Inicializa la vista del resumen general y configura los casos de uso necesarios 
	 * para el calculo de metricas, inyectando los repositorios globales del sistema.
	 * 
	 * @param appController Controlador principal para delegar notificaciones y acciones globales.
	 * @param role Rol del usuario autenticado, usado para configurar los permisos de la vista.
	 * @param serviceUnitRepo Repositorio de unidades de servicio para consultar disponibilidad.
	 * @param kitRepo Repositorio de kits para consultar elementos en reparacion.
	 * @param reportRepo Repositorio de siniestros para calcular el volumen de operaciones.
	 */
	public DashboardController(AppController appController, String role, ServiceUnitRepository serviceUnitRepo,
		KitRepository kitRepo, ReportRepository reportRepo, ActionLogRepository actionLogRepository,
		UndoGlobalActionUseCase undoGlobalActionUseCase) {
		this.appController = appController;
		this.actionLogRepository = actionLogRepository;
		this.undoGlobalActionUseCase = undoGlobalActionUseCase;
		this.view = new DashboardView(role, this); // Asumiendo que espera un DashboardController, actualiza la vista

		this.getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(serviceUnitRepo);
		this.getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepo);
		this.getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepo);
		this.generateDailyCSVUseCase = new GenerateDailyCSVUseCase(reportRepo);
	}

	/**
	 * Obtiene la vista asociada a este sub-controlador.
	 * 
	 * @return Objeto DashboardView que representa la interfaz grafica del resumen general.
	 */
	public DashboardView getView() { return view; }

	/**
	 * Calcula y actualiza las metricas globales del sistema para reflejarlas en la interfaz.
	 * Extrae y consolida datos operativos vitales como el conteo de unidades activas, 
	 * siniestros en prioridad critica, kits en mantenimiento y el total de solicitudes.
	 */
	public void refreshView() {
		int activeUnits = 0, critical = 0, maint = 0, total = 0;
		
		SimpleList.Iterator<ServiceUnitDTO> uit = getSortedAndFilteredServiceUnitsUseCase.execute().iterador();
		while (uit.hasNext()) {
			if (!uit.Next().getStatus().equalsIgnoreCase("Mantenimiento")) {
				activeUnits++;
			}
		}
		
		maint = getMaintenanceKitsUseCase.execute().getSize();
		
		SimpleList.Iterator<ReportDTO> rit = getSortedAndFilteredReportsUseCase.execute().iterador();
		while (rit.hasNext()) {
			ReportDTO r = rit.Next();
			total++;
			if (r.getPriority() != null && r.getPriority().equalsIgnoreCase("Alta")) {
				critical++;
			}
		}
		
		view.updateStatistics(String.valueOf(activeUnits), String.valueOf(critical), String.valueOf(maint), String.valueOf(total));
		view.updateLogPanel(actionLogRepository.getLogDescriptions());
	}

	/**
	 * Ejecuta el proceso de exportacion del reporte diario en formato CSV.
	 * Determina el directorio de trabajo actual por defecto y delega la operacion 
	 * y notificacion final al controlador principal, omitiendo acciones posteriores de interfaz.
	 */
	public void exportDailyReport() {
		String defaultPath = System.getProperty("user.dir");
	    
	    appController.handleResponse(generateDailyCSVUseCase.execute(defaultPath), () -> {});
	}

	/**
	 * Ejecuta la operacion de deshacer la ultima accion global registrada.
	 * El metodo delega el resultado al controlador principal y, en caso de exito,
	 * refresca todas las vistas y el panel de log del dashboard.
	 */
	public void undoLastGlobalAction() {
		ResponseDTO response = undoGlobalActionUseCase.execute();
		appController.handleResponse(response, () -> {
			appController.refreshAllViews();
			this.refreshView();
		});
	}
}