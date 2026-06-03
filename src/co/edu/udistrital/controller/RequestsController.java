package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.RequestsView;

/**
 * Sub-controlador core encargado de la gestion operativa de siniestros y despacho.
 * Actua como el motor del modulo de "Despacho y Control de Emergencias", coordinando 
 * el ciclo de vida de los reportes a traves de sus diferentes estados (Pendiente, 
 * En Progreso, Por Confirmar) y gestionando la asignacion de recursos (Tecnicos, 
 * Unidades y Kits).
 * * @author Jimmy86gb
 */
public class RequestsController {

	private final AppController appController;
	private final RequestsView view;

	private final RegisterReportUseCase registerReportUseCase;
	private final AssignResourcesReportUseCase assignResourcesReportUseCase;
	private final FinishReportInFieldUseCase finishReportInFieldUseCase;
	private final ApproveReportActionUseCase approveReportActionUseCase;
	private final RejectReportActionUseCase rejectReportActionUseCase;
	private final RequestReportCancellationUseCase requestReportCancellationUseCase;
	private final GetSortedAndFilteredReportsUseCase getSortedAndFilteredReportsUseCase;
	private final GetPendingReportsUseCase getPendingReportsUseCase;
	private final GetOnGoingReportsUseCase getOnGoingReportsUseCase;
	private final GetToConfirmReportsUseCase getToConfirmReportsUseCase;
	private final GetNextPendingReportUseCase getNextPendingReportUseCase;
	
	// Casos de uso compartidos necesarios para crear reportes
	private final GetClientByIDUseCase getClientByIDUseCase;
	private final GetAvailableTechnicianByZoneAndProblemUseCase getAvailableTechnicianUseCase;
	private final GetAvailableUnitsByZoneUseCase getAvailableUnitsUseCase;
	private final GetAvailableKitsByTypeUseCase getAvailableKitsUseCase;
	private final GetCriticLevelLabelsUseCase getCriticLevelLabelsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;
	private final GetTechnicianSpecialityLabelsUseCase getSpecialityLabelsUseCase;
	
	/**
	 * Constructor de la clase RequestsController.
	 * Ensambla el controlador inicializando su vista interactiva y todos los 
	 * casos de uso requeridos para la logica de colas, pilas y asignaciones cruzadas.
	 * * @param appController Controlador principal para delegacion de alertas y navegacion.
	 * @param role Rol del usuario actual para ajustar los permisos en el Kanban.
	 * @param reportRepo Repositorio central de siniestros.
	 * @param clientRepo Repositorio de clientes (para verificacion en registro).
	 * @param techRepo Repositorio de tecnicos (para despacho).
	 * @param unitRepo Repositorio de unidades (para despacho).
	 * @param kitRepo Repositorio de kits (para despacho).
	 */
	public RequestsController(AppController appController, String role, ReportRepository reportRepo, ClientRepository clientRepo, TechnicianRepository techRepo, ServiceUnitRepository unitRepo, KitRepository kitRepo) {
		this.appController = appController;
		this.view = new RequestsView(role);
		this.view.setController(this);

		this.registerReportUseCase = new RegisterReportUseCase(clientRepo, reportRepo);
		this.assignResourcesReportUseCase = new AssignResourcesReportUseCase(reportRepo, techRepo, unitRepo, kitRepo);
		this.finishReportInFieldUseCase = new FinishReportInFieldUseCase(reportRepo);
		this.approveReportActionUseCase = new ApproveReportActionUseCase(reportRepo, kitRepo);
		this.rejectReportActionUseCase = new RejectReportActionUseCase(reportRepo);
		this.requestReportCancellationUseCase = new RequestReportCancellationUseCase(reportRepo);
		this.getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepo);
		this.getPendingReportsUseCase = new GetPendingReportsUseCase(reportRepo);
		this.getOnGoingReportsUseCase = new GetOnGoingReportsUseCase(reportRepo);
		this.getToConfirmReportsUseCase = new GetToConfirmReportsUseCase(reportRepo);
		this.getNextPendingReportUseCase = new GetNextPendingReportUseCase(reportRepo);

		this.getClientByIDUseCase = new GetClientByIDUseCase(clientRepo);
		this.getAvailableTechnicianUseCase = new GetAvailableTechnicianByZoneAndProblemUseCase(techRepo);
		this.getAvailableUnitsUseCase = new GetAvailableUnitsByZoneUseCase(unitRepo);
		this.getAvailableKitsUseCase = new GetAvailableKitsByTypeUseCase(kitRepo);
		this.getCriticLevelLabelsUseCase = new GetCriticLevelLabelsUseCase();
		this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();
		this.getSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
	}

	/**
	 * Obtiene la vista asociada a este sub-controlador.
	 * * @return Objeto RequestsView con la interfaz del tablero Kanban.
	 */
	public RequestsView getView() { return view; }

	/**
	 * Refresca completamente el tablero de control de emergencias.
	 * Limpia los paneles visuales y reconstruye las colas y pilas iterando 
	 * sobre las estructuras de datos, garantizando que no se generen duplicados ("fantasmas").
	 */
	public void refreshView() {
		view.clearPanels();

		// Carga de Cola de Espera (Exclusivo para reportes cancelables/pendientes)
		SimpleList.Iterator<ReportDTO> pendIt = getPendingReportsUseCase.execute().iterador();
		while (pendIt.hasNext()) {
			ReportDTO r = pendIt.Next();
			if (r.canCancel()) view.addReportCard(r, "PENDING");
		}

		// Carga de Pila LIFO de operaciones en terreno
		SimpleList.Iterator<ReportDTO> ongoIt = getOnGoingReportsUseCase.execute().iterador();
		while (ongoIt.hasNext()) {
			ReportDTO r = ongoIt.Next();
			if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
				view.addReportCard(r, "ONGOING");
			}
		}

		// Carga de Pila LIFO de confirmaciones para el Administrador
		SimpleList.Iterator<ReportDTO> confIt = getToConfirmReportsUseCase.execute().iterador();
		while (confIt.hasNext()) {
			ReportDTO r = confIt.Next();
			if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
				view.addReportCard(r, "CONFIRM");
			}
		}
		
		// Carga del historial general (si la vista lo implementa)
		SimpleList.Iterator<ReportDTO> histIt = getSortedAndFilteredReportsUseCase.execute().iterador();
		while (histIt.hasNext()) {
			view.addReportToSummary(histIt.Next());
		}
	}

	/**
	 * Inicia el flujo de creacion de un nuevo reporte.
	 * Verifica previamente la existencia del cliente; si no existe, delega el flujo 
	 * hacia el registro de clientes, de lo contrario abre el formulario de siniestro.
	 * * @param clientId Cedula o NIT ingresado en el dialogo inicial.
	 */
	public void processNewRequest(String clientId) {
		ClientDTO client = getClientByIDUseCase.execute(clientId);
		if (client == null) {
			appController.triggerAddClientDialog(clientId);
		} else {
			view.showCreateReportDialog(client);
		}
	}

	/**
	 * Ejecuta el registro formal de la emergencia, insertandola en la cola prioritaria.
	 * * @param cliId Identificacion del cliente afectado.
	 * @param desc Descripcion en texto libre del siniestro.
	 * @param type Especialidad tecnica requerida.
	 * @param prio Nivel de urgencia (Alta, Media, Baja).
	 * @param zone Zona geografica del incidente.
	 */
	public void submitReportCreation(String cliId, String desc, String type, String prio, String zone) {
		appController.handleResponse(registerReportUseCase.execute(cliId, desc, type, prio, zone), appController::refreshAllViews);
	}

	/**
	 * Asigna los recursos operativos a una emergencia y la traslada a la fase de ejecucion.
	 * * @param techId UUID del tecnico seleccionado.
	 * @param unitId UUID de la unidad de servicio movil.
	 * @param kitId UUID del kit asignado.
	 */
	public void assignReportResources(String techId, String unitId, String kitId) {
		appController.handleResponse(assignResourcesReportUseCase.execute(techId, unitId, kitId), appController::refreshAllViews);
	}

	/**
	 * Marca una atencion en terreno como finalizada, moviendola a la pila de confirmacion.
	 * * @param ticketId UUID del reporte.
	 */
	public void finishReport(String ticketId) {
		appController.handleResponse(finishReportInFieldUseCase.execute(ticketId), appController::refreshAllViews);
	}

	/**
	 * Aprueba la resolucion del siniestro actual en el tope de la pila de confirmacion,
	 * liberando los recursos asociados (tecnico, unidad, kit).
	 */
	public void approveReport() { 
		appController.handleResponse(approveReportActionUseCase.execute(), appController::refreshAllViews); 
	}

	/**
	 * Rechaza la accion pendiente en el tope de la pila de confirmacion y 
	 * devuelve el siniestro a su estado anterior.
	 */
	public void rejectReport() { 
		appController.handleResponse(rejectReportActionUseCase.execute(), appController::refreshAllViews); 
	}

	/**
	 * Solicita la cancelacion de un siniestro que aun se encuentra en la cola de espera.
	 * * @param ticketId UUID del reporte a cancelar.
	 */
	public void cancelReport(String ticketId) { 
		appController.handleResponse(requestReportCancellationUseCase.execute(ticketId), appController::refreshAllViews); 
	}

	/**
	 * Extrae visualmente (sin retirar) la emergencia mas urgente de la cola de espera.
	 * * @return Objeto ReportDTO con los datos del siguiente siniestro a atender.
	 */
	public ReportDTO getNextPendingReport() { return getNextPendingReportUseCase.execute(); }

	/**
	 * Obtiene una lista filtrada de tecnicos disponibles y capacitados para una zona y especialidad.
	 * Aplica normalizacion de cadenas para asegurar la busqueda.
	 * * @param zoneName Nombre de la zona del incidente.
	 * @param specialtyName Tipo de problema o especialidad requerida.
	 * @return Lista de entidades simplificadas para poblar ComboBoxes.
	 */
	public SimpleList<EntityItem> getSuggestedTechnicians(String zoneName, String specialtyName) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String zNorm = zoneName != null ? zoneName.replace(" ", "") : "";
		String sNorm = specialtyName != null ? specialtyName.replace(" ", "") : "";
		SimpleList<TechnicianDTO> dtos = getAvailableTechnicianUseCase.execute(zNorm, sNorm);
		if (dtos.getSize() == 0) dtos = getAvailableTechnicianUseCase.execute(zoneName, specialtyName);
		SimpleList.Iterator<TechnicianDTO> it = dtos.iterador();
		while (it.hasNext()) { TechnicianDTO t = it.Next(); list.add(new EntityItem(t.getId(), t.getName())); }
		return list;
	}

	/**
	 * Obtiene una lista filtrada de unidades fisicamente disponibles en una zona.
	 * * @param zoneName Nombre de la zona de busqueda.
	 * @return Lista de entidades simplificadas.
	 */
	public SimpleList<EntityItem> getSuggestedUnits(String zoneName) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String zNorm = zoneName != null ? zoneName.replace(" ", "") : "";
		SimpleList<ServiceUnitDTO> dtos = getAvailableUnitsUseCase.execute(zNorm);
		if (dtos.getSize() == 0) dtos = getAvailableUnitsUseCase.execute(zoneName);
		SimpleList.Iterator<ServiceUnitDTO> it = dtos.iterador();
		while (it.hasNext()) { ServiceUnitDTO u = it.Next(); list.add(new EntityItem(u.getId(), u.getType())); }
		return list;
	}

	/**
	 * Obtiene una lista de kits disponibles acordes a la especialidad solicitada.
	 * * @param problemType Categoria del incidente a atender.
	 * @return Lista de entidades simplificadas.
	 */
	public SimpleList<EntityItem> getSuggestedKits(String problemType) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String pNorm = problemType != null ? problemType.replace(" ", "") : "";
		SimpleList<KitDTO> dtos = getAvailableKitsUseCase.execute(pNorm);
		if (dtos.getSize() == 0) dtos = getAvailableKitsUseCase.execute(problemType);
		SimpleList.Iterator<KitDTO> it = dtos.iterador();
		while (it.hasNext()) { KitDTO k = it.Next(); list.add(new EntityItem(k.getId(), k.getType())); }
		return list;
	}

	// --- Metodos proveedores de etiquetas para UI ---
	
	public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
	public SimpleList<String> getCriticLevelLabels() { return getCriticLevelLabelsUseCase.execute(); }
	public SimpleList<String> getSpecialityLabels() { 
		return getSpecialityLabelsUseCase.execute(); 
	}
}