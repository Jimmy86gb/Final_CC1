package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.dtos.EntityItem;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.dtos.SessionDTO;
import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ProfileRepository;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.ApproveReportActionUseCase;
import co.edu.udistrital.model.usecases.ApproveUnitStatusUseCase;
import co.edu.udistrital.model.usecases.AssignResourcesReportUseCase;
import co.edu.udistrital.model.usecases.FinishReportInFieldUseCase;
import co.edu.udistrital.model.usecases.GenerateDailyCSVUseCase;
import co.edu.udistrital.model.usecases.GetAvailableKitsByTypeUseCase;
import co.edu.udistrital.model.usecases.GetAvailableTechnicianByZoneAndProblemUseCase;
import co.edu.udistrital.model.usecases.GetAvailableUnitsByZoneUseCase;
import co.edu.udistrital.model.usecases.GetClientByIDUseCase;
import co.edu.udistrital.model.usecases.GetClientTypeLabelsUseCase;
import co.edu.udistrital.model.usecases.GetCriticLevelLabelsUseCase;
import co.edu.udistrital.model.usecases.GetKitStatusLabelsUseCase;
import co.edu.udistrital.model.usecases.GetKitTypeLabelsUseCase;
import co.edu.udistrital.model.usecases.GetMaintenanceKitsUseCase;
import co.edu.udistrital.model.usecases.GetNextPendingReportUseCase;
import co.edu.udistrital.model.usecases.GetOnGoingReportsUseCase;
import co.edu.udistrital.model.usecases.GetPendingReportsUseCase;
import co.edu.udistrital.model.usecases.GetServiceUnitsStatusLabelsUseCase;
import co.edu.udistrital.model.usecases.GetServiceUnitsTypeLabelsUseCase;
import co.edu.udistrital.model.usecases.GetSortedAndDFilteredTechniciansUseCase;
import co.edu.udistrital.model.usecases.GetSortedAndFilteredClientsUseCase;
import co.edu.udistrital.model.usecases.GetSortedAndFilteredKitsUseCase;
import co.edu.udistrital.model.usecases.GetSortedAndFilteredReportsUseCase;
import co.edu.udistrital.model.usecases.GetSortedAndFilteredServiceUnitsUseCase;
import co.edu.udistrital.model.usecases.GetTechnicianSpecialityLabelsUseCase;
import co.edu.udistrital.model.usecases.GetTechnicianStatusLabelsUseCase;
import co.edu.udistrital.model.usecases.GetToConfirmReportsUseCase;
import co.edu.udistrital.model.usecases.GetToConfirmServiceUnitsUseCase;
import co.edu.udistrital.model.usecases.GetZoneLabelsUseCase;
import co.edu.udistrital.model.usecases.LoadSystemDataUseCase;
import co.edu.udistrital.model.usecases.LoginUseCase;
import co.edu.udistrital.model.usecases.RegisterClientUseCase;
import co.edu.udistrital.model.usecases.RegisterKitUseCase;
import co.edu.udistrital.model.usecases.RegisterReportUseCase;
import co.edu.udistrital.model.usecases.RegisterServiceUnitUseCase;
import co.edu.udistrital.model.usecases.RegisterTechnicianUseCase;
import co.edu.udistrital.model.usecases.RejectReportActionUseCase;
import co.edu.udistrital.model.usecases.RejectUnitStatusUseCase;
import co.edu.udistrital.model.usecases.RequestReportCancellationUseCase;
import co.edu.udistrital.model.usecases.RetireKitFromMaintenanceUseCase;
import co.edu.udistrital.model.usecases.ReturnKitToServiceUseCase;
import co.edu.udistrital.model.usecases.SaveSystemDataUseCase;
import co.edu.udistrital.model.usecases.UndoReportResourcesUseCase;
import co.edu.udistrital.model.usecases.UpdateClientUseCase;
import co.edu.udistrital.model.usecases.UpdateKitUseCase;
import co.edu.udistrital.model.usecases.UpdateServiceUnitUseCase;
import co.edu.udistrital.model.usecases.UpdateTechnicianUseCase;
import co.edu.udistrital.view.ClientsView;
import co.edu.udistrital.view.DashboardView;
import co.edu.udistrital.view.KitsView;
import co.edu.udistrital.view.LoginView;
import co.edu.udistrital.view.MainView;
import co.edu.udistrital.view.RequestsView;
import co.edu.udistrital.view.TechniciansView;
import co.edu.udistrital.view.UnitsView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AppController {

	private MainView mainView;
	private DashboardView dashboardView;
	private RequestsView requestsView;
	private UnitsView unitsView;
	private TechniciansView techniciansView;
	private KitsView kitsView;
	private ClientsView clientsView;
	private LoginView loginView;

	private final ClientRepository clientRepository;
	private final KitRepository kitRepository;
	private final ReportRepository reportRepository;
	private final ServiceUnitRepository serviceUnitRepository;
	private final TechnicianRepository technicianRepository;
	private final ProfileRepository profileRepository;

	private final RegisterClientUseCase registerClientUseCase;
	private final UpdateClientUseCase updateClientUseCase;
	private final GetSortedAndFilteredClientsUseCase getSortedClientsUseCase;
	private final GetClientTypeLabelsUseCase getClientTypeLabelsUseCase;
	private final GetClientByIDUseCase getClientByIDUseCase;

	private final RegisterTechnicianUseCase registerTechnicianUseCase;
	private final UpdateTechnicianUseCase updateTechnicianUseCase;
	private final GetSortedAndDFilteredTechniciansUseCase getSortedAndDFilteredTechniciansUseCase;
	private final GetTechnicianStatusLabelsUseCase getTechnicianStatusLabelsUseCase;
	private final GetTechnicianSpecialityLabelsUseCase getTechnicianSpecialityLabelsUseCase;
	private final GetAvailableTechnicianByZoneAndProblemUseCase getAvailableTechnicianByZoneAndProblemUseCase;

	private final RegisterServiceUnitUseCase registerServiceUnitUseCase;
	private final UpdateServiceUnitUseCase updateServiceUnitUseCase;
	private final ApproveUnitStatusUseCase approveUnitStatusUseCase;
	private final RejectUnitStatusUseCase rejectUnitStatusUseCase;
	private final GetToConfirmServiceUnitsUseCase getToConfirmServiceUnitsUseCase;
	private final GetServiceUnitsStatusLabelsUseCase getServiceUnitsStatusLabelsUseCase;
	private final GetServiceUnitsTypeLabelsUseCase getServiceUnitsTypeLabelsUseCase;
	private final GetSortedAndFilteredServiceUnitsUseCase getSortedAndFilteredServiceUnitsUseCase;
	private final GetAvailableUnitsByZoneUseCase getAvailableUnitsByZoneUseCase;

	private final RegisterKitUseCase registerKitUseCase;
	private final UpdateKitUseCase updateKitUseCase;
	private final GetSortedAndFilteredKitsUseCase getSortedKitsUseCase;
	private final RetireKitFromMaintenanceUseCase retireKitFromMaintenanceUseCase;
	private final ReturnKitToServiceUseCase returnKitToServiceUseCase;
	private final GetKitTypeLabelsUseCase getKitTypeLabelsUseCase;
	private final GetKitStatusLabelsUseCase getKitStatusLabelsUseCase;
	private final GetAvailableKitsByTypeUseCase getAvailableKitsByTypeUseCase;
	private final GetMaintenanceKitsUseCase getMaintenanceKitsUseCase;

	private final RegisterReportUseCase registerReportUseCase;
	private final AssignResourcesReportUseCase assignResourcesReportUseCase;
	private final UndoReportResourcesUseCase undoReportResourcesUseCase;
	private final FinishReportInFieldUseCase finishReportInFieldUseCase;
	private final ApproveReportActionUseCase approveReportActionUseCase;
	private final RejectReportActionUseCase rejectReportActionUseCase;
	private final RequestReportCancellationUseCase requestReportCancellationUseCase;
	private final GetSortedAndFilteredReportsUseCase getSortedAndFilteredReportsUseCase;
	private final GetPendingReportsUseCase getPendingReportsUseCase;
	private final GetOnGoingReportsUseCase getOnGoingReportsUseCase;
	private final GetToConfirmReportsUseCase getToConfirmReportsUseCase;
	private final GetNextPendingReportUseCase getNextPendingReportUseCase;
	private final GenerateDailyCSVUseCase generateDailyCSVUseCase;
	private final GetCriticLevelLabelsUseCase getCriticLevelLabelsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;

	private final LoginUseCase loginUseCase;
	private final SaveSystemDataUseCase saveSystemDataUseCase;
	private final LoadSystemDataUseCase loadSystemDataUseCase;

	private Stage primaryStage;

	public AppController() {
		this.clientRepository = new ClientRepository();
		this.kitRepository = new KitRepository();
		this.reportRepository = new ReportRepository();
		this.serviceUnitRepository = new ServiceUnitRepository();
		this.technicianRepository = new TechnicianRepository();
		this.profileRepository = new ProfileRepository();

		registerClientUseCase = new RegisterClientUseCase(clientRepository);
		updateClientUseCase = new UpdateClientUseCase(clientRepository);
		getSortedClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepository);
		getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();
		getClientByIDUseCase = new GetClientByIDUseCase(clientRepository);

		registerTechnicianUseCase = new RegisterTechnicianUseCase(technicianRepository);
		updateTechnicianUseCase = new UpdateTechnicianUseCase(technicianRepository);
		getSortedAndDFilteredTechniciansUseCase = new GetSortedAndDFilteredTechniciansUseCase(technicianRepository);
		getTechnicianStatusLabelsUseCase = new GetTechnicianStatusLabelsUseCase();
		getTechnicianSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
		getAvailableTechnicianByZoneAndProblemUseCase = new GetAvailableTechnicianByZoneAndProblemUseCase(
				technicianRepository);

		registerServiceUnitUseCase = new RegisterServiceUnitUseCase(serviceUnitRepository);
		updateServiceUnitUseCase = new UpdateServiceUnitUseCase(serviceUnitRepository);
		approveUnitStatusUseCase = new ApproveUnitStatusUseCase(serviceUnitRepository);
		rejectUnitStatusUseCase = new RejectUnitStatusUseCase(serviceUnitRepository);
		getToConfirmServiceUnitsUseCase = new GetToConfirmServiceUnitsUseCase(serviceUnitRepository);
		getServiceUnitsStatusLabelsUseCase = new GetServiceUnitsStatusLabelsUseCase();
		getServiceUnitsTypeLabelsUseCase = new GetServiceUnitsTypeLabelsUseCase();
		getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(serviceUnitRepository);
		getAvailableUnitsByZoneUseCase = new GetAvailableUnitsByZoneUseCase(serviceUnitRepository);

		registerKitUseCase = new RegisterKitUseCase(kitRepository);
		updateKitUseCase = new UpdateKitUseCase(kitRepository);
		getSortedKitsUseCase = new GetSortedAndFilteredKitsUseCase(kitRepository);
		retireKitFromMaintenanceUseCase = new RetireKitFromMaintenanceUseCase(kitRepository);
		returnKitToServiceUseCase = new ReturnKitToServiceUseCase(kitRepository);
		getKitTypeLabelsUseCase = new GetKitTypeLabelsUseCase();
		getKitStatusLabelsUseCase = new GetKitStatusLabelsUseCase();
		getAvailableKitsByTypeUseCase = new GetAvailableKitsByTypeUseCase(kitRepository);
		getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepository);

		registerReportUseCase = new RegisterReportUseCase(clientRepository, reportRepository);
		assignResourcesReportUseCase = new AssignResourcesReportUseCase(reportRepository, technicianRepository,
				serviceUnitRepository, kitRepository);
		undoReportResourcesUseCase = new UndoReportResourcesUseCase(reportRepository);
		finishReportInFieldUseCase = new FinishReportInFieldUseCase(reportRepository);
		approveReportActionUseCase = new ApproveReportActionUseCase(reportRepository, kitRepository);
		rejectReportActionUseCase = new RejectReportActionUseCase(reportRepository);
		requestReportCancellationUseCase = new RequestReportCancellationUseCase(reportRepository);
		getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepository);
		getPendingReportsUseCase = new GetPendingReportsUseCase(reportRepository);
		getOnGoingReportsUseCase = new GetOnGoingReportsUseCase(reportRepository);
		getToConfirmReportsUseCase = new GetToConfirmReportsUseCase(reportRepository);
		getNextPendingReportUseCase = new GetNextPendingReportUseCase(reportRepository);
		generateDailyCSVUseCase = new GenerateDailyCSVUseCase(reportRepository);
		getCriticLevelLabelsUseCase = new GetCriticLevelLabelsUseCase();
		getZoneLabelsUseCase = new GetZoneLabelsUseCase();

		loginUseCase = new LoginUseCase(profileRepository);
		saveSystemDataUseCase = new SaveSystemDataUseCase(profileRepository, clientRepository, technicianRepository,
				serviceUnitRepository, kitRepository, reportRepository);
		loadSystemDataUseCase = new LoadSystemDataUseCase(profileRepository, clientRepository, technicianRepository,
				serviceUnitRepository, kitRepository, reportRepository);
	}

	public void run() {
		JavaFxLauncher.setController(this);

		// Disparamos la interfaz gráfica
		javafx.application.Application.launch(JavaFxLauncher.class);
	}

	public void startApplication(Stage primaryStage) {
		this.primaryStage = primaryStage;
		loadSystemDataUseCase.execute();

		loginView = new LoginView();
		loginView.setOnLoginAction(() -> {
			SessionDTO session = loginUseCase.execute(loginView.getUsername(), loginView.getPassword());
			if (session.isSuccess()) {
				initializeSystem(session.getRole());
			} else {
				loginView.showMessage(session.getMessage());
			}
		});
		primaryStage.setScene(loginView.getScene());
		primaryStage.show();
	}

	public void loadData() {
		loadSystemDataUseCase.execute();
	}

	public void saveData() {
		saveSystemDataUseCase.execute();
	}

	private void initializeSystem(String role) {

		mainView = new MainView(role);
		dashboardView = new DashboardView(role, this);
		requestsView = new RequestsView(role);
		unitsView = new UnitsView(role);
		techniciansView = new TechniciansView(role);
		kitsView = new KitsView(role);
		clientsView = new ClientsView(role);

		mainView.setNavigationController(this);
		clientsView.setController(this);
		unitsView.setController(this);
		kitsView.setController(this);
		techniciansView.setController(this);
		requestsView.setController(this);

		refreshAllViews();
		primaryStage.setScene(mainView.getScene());
		primaryStage.centerOnScreen();
	}

	public void registerClient(String id, String name, String type, String contact) {
		ResponseDTO res = registerClientUseCase.ResponseDTO(id, name, type, contact);
		handleResponse(res, this::refreshAllViews);
	}

	public void updateClient(String id, String name, String type, String contact) {
		ResponseDTO res = updateClientUseCase.execute(id, name, type, contact);
		handleResponse(res, this::refreshAllViews);
	}

	public void registerTechnician(String name, String specialty, String zone) {
		ResponseDTO res = registerTechnicianUseCase.execute(name, specialty, zone);
		handleResponse(res, this::refreshAllViews);
	}

	public void updateTechnician(String id, String name, String specialty, String zone, String status) {
		ResponseDTO res = updateTechnicianUseCase.execute(id, name, specialty, zone, status);
		handleResponse(res, this::refreshAllViews);
	}

	public void registerUnit(String type, String zone, int quantity) {
		ResponseDTO res = registerServiceUnitUseCase.execute(type, zone, quantity);
		handleResponse(res, this::refreshAllViews);
	}

	public void updateServiceUnit(String id, String type, String status, String zone) {
		ResponseDTO res = updateServiceUnitUseCase.execute(id, type, status, zone);
		handleResponse(res, this::refreshAllViews);
	}

	public void approveUnitStatus() {
		ResponseDTO res = approveUnitStatusUseCase.execute();
		handleResponse(res, this::refreshAllViews);
	}

	public void rejectUnitStatus() {
		ResponseDTO res = rejectUnitStatusUseCase.execute();
		handleResponse(res, this::refreshAllViews);
	}

	public void registerKit(String type, int quantity) {
		ResponseDTO res = registerKitUseCase.execute(type, quantity);
		handleResponse(res, this::refreshAllViews);
	}

	public void updateKitToMaintenance(String id, String type) {
		ResponseDTO res = updateKitUseCase.execute(id, type, "Mantenimiento");
		handleResponse(res, this::refreshAllViews);
	}

	public void updateKitStatus(String id, String type, String newStatus) {
		ResponseDTO res = updateKitUseCase.execute(id, type, newStatus);
		handleResponse(res, this::refreshAllViews);
	}

	public void returnKitToService() {
		ResponseDTO res = returnKitToServiceUseCase.execute();
		handleResponse(res, this::refreshAllViews);
	}

	public void retireKitFromMaintenance() {
		ResponseDTO res = retireKitFromMaintenanceUseCase.execute();
		handleResponse(res, this::refreshAllViews);
	}

	public SimpleList<String> getZoneLabels() {
		return getZoneLabelsUseCase.execute();
	}

	public SimpleList<String> getCriticLevelLabels() {
		return getCriticLevelLabelsUseCase.execute();
	}

	public SimpleList<String> getSpecialityLabels() {
		return getTechnicianSpecialityLabelsUseCase.execute();
	}

	public SimpleList<String> getTechnicianStatusLabels() {
		return getTechnicianStatusLabelsUseCase.execute();
	}

	public SimpleList<String> getClientTypeLabels() {
		return getClientTypeLabelsUseCase.execute();
	}

	public SimpleList<String> getUnitTypeLabels() {
		return getServiceUnitsTypeLabelsUseCase.execute();
	}

	public SimpleList<String> getUnitStatusLabels() {
		return getServiceUnitsStatusLabelsUseCase.execute();
	}

	public SimpleList<String> getKitTypeLabels() {
		return getKitTypeLabelsUseCase.execute();
	}

	public SimpleList<String> getKitStatusLabels() {
		return getKitStatusLabelsUseCase.execute();
	}

	public SimpleList<ServiceUnitDTO> getToConfirmUnits() {
		return getToConfirmServiceUnitsUseCase.execute();
	}

	public SimpleList<EntityItem> getSuggestedTechnicians(String zoneName, String specialtyName) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String zNorm = zoneName != null ? zoneName.replace(" ", "") : "";
		String sNorm = specialtyName != null ? specialtyName.replace(" ", "") : "";
		SimpleList<TechnicianDTO> dtos = getAvailableTechnicianByZoneAndProblemUseCase.execute(zNorm, sNorm);
		if (dtos.getSize() == 0) {
			dtos = getAvailableTechnicianByZoneAndProblemUseCase.execute(zoneName, specialtyName);
		}
		SimpleList.Iterator<TechnicianDTO> it = dtos.iterador();
		while (it.hasNext()) {
			TechnicianDTO t = it.Next();
			list.add(new EntityItem(t.getId(), t.getName()));
		}
		return list;
	}

	public SimpleList<EntityItem> getSuggestedUnits(String zoneName) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String zNorm = zoneName != null ? zoneName.replace(" ", "") : "";
		SimpleList<ServiceUnitDTO> dtos = getAvailableUnitsByZoneUseCase.execute(zNorm);
		if (dtos.getSize() == 0) {
			dtos = getAvailableUnitsByZoneUseCase.execute(zoneName);
		}
		SimpleList.Iterator<ServiceUnitDTO> it = dtos.iterador();
		while (it.hasNext()) {
			ServiceUnitDTO u = it.Next();
			list.add(new EntityItem(u.getId(), u.getType()));
		}
		return list;
	}

	public SimpleList<EntityItem> getSuggestedKits(String problemType) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String pNorm = problemType != null ? problemType.replace(" ", "") : "";
		SimpleList<KitDTO> dtos = getAvailableKitsByTypeUseCase.execute(pNorm);
		if (dtos.getSize() == 0) {
			dtos = getAvailableKitsByTypeUseCase.execute(problemType);
		}
		SimpleList.Iterator<KitDTO> it = dtos.iterador();
		while (it.hasNext()) {
			KitDTO k = it.Next();
			list.add(new EntityItem(k.getId(), k.getType()));
		}
		return list;
	}

	public void processNewRequest(String clientId) {
		ClientDTO client = getClientByIDUseCase.execute(clientId);

		if (client == null) {
			navigateToClients();
			clientsView.showAddClientDialog(clientId);
		} else {
			requestsView.showCreateReportDialog(client);
		}
	}

	public void submitReportCreation(String cliId, String desc, String type, String prio, String zone) {
		ResponseDTO res = registerReportUseCase.execute(cliId, desc, type, prio, zone);
		handleResponse(res, this::refreshAllViews);
	}

	public ReportDTO getNextPendingReport() {
		return getNextPendingReportUseCase.execute();
	}

	public void assignReportResources(String techId, String unitId, String kitId) {
		ResponseDTO res = assignResourcesReportUseCase.execute(techId, unitId, kitId);
		handleResponse(res, this::refreshAllViews);
	}

	public void finishReport(String ticketId) {
		ResponseDTO res = finishReportInFieldUseCase.execute(ticketId);
		handleResponse(res, this::refreshAllViews);
	}

	public void approveReport() {
		ResponseDTO res = approveReportActionUseCase.execute();
		handleResponse(res, this::refreshAllViews);
	}

	public void rejectReport() {
		ResponseDTO res = rejectReportActionUseCase.execute();
		handleResponse(res, this::refreshAllViews);
	}

	public void cancelReport(String ticketId) {
		ResponseDTO res = requestReportCancellationUseCase.execute(ticketId);
		handleResponse(res, this::refreshAllViews);
	}

	public void exportDailyReport() {
		handleResponse(generateDailyCSVUseCase.execute(""), () -> {
		});
	}

	public void refreshAllViews() {
		refreshClientsView();
		refreshKitsView();
		refreshUnitsView();
		refreshTechniciansView();
		refreshRequestsView();
		refreshDashboardView();
	}

	private void refreshDashboardView() {
		if (dashboardView == null) {
			return;
		}
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
		dashboardView.updateStatistics(String.valueOf(activeUnits), String.valueOf(critical), String.valueOf(maint),
				String.valueOf(total));
	}

	/**
	 * Actualiza la vista de solicitudes clasificando los tiquetes en sus 
	 * tres contenedores visuales correspondientes, respetando el flujo de colas y pilas.
	 */
	/**
	 * Actualiza la vista de solicitudes clasificando los tiquetes en sus 
	 * tres contenedores correspondientes de JavaFX, consumiendo las colas FIFO y las pilas.
	 */
	private void refreshRequestsView() {
		requestsView.clearPanels();

		// 1. Cargar solicitudes PENDIENTES desde las colas FIFO de prioridad activa
		SimpleList.Iterator<ReportDTO> pendIt = getPendingReportsUseCase.execute().iterador();
		while (pendIt.hasNext()) {
			ReportDTO r = pendIt.Next();
			if (r.canCancel()) {
				requestsView.addReportCard(r, "PENDING");
			}
		}

		// 2. Cargar solicitudes EN PROGRESO (ON_GOING) desde su pila LIFO activa
		SimpleList.Iterator<ReportDTO> ongoIt = getOnGoingReportsUseCase.execute().iterador();
		while (ongoIt.hasNext()) {
			ReportDTO r = ongoIt.Next();
			if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
				requestsView.addReportCard(r, "ONGOING");
			}
		}

		// 3. Cargar solicitudes POR CONFIRMAR (CONFIRM) desde la pila LIFO de auditoría del administrador
		SimpleList.Iterator<ReportDTO> confIt = getToConfirmReportsUseCase.execute().iterador();
		while (confIt.hasNext()) {
			ReportDTO r = confIt.Next();
			if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
				requestsView.addReportCard(r, "CONFIRM");
			}
		}
		
		// 4. NUEVO: Llenar el resumen histórico ordenado cruzando toda la memoria
		SimpleList.Iterator<ReportDTO> histIt = getSortedAndFilteredReportsUseCase.execute().iterador();
		while (histIt.hasNext()) {
			requestsView.addReportToSummary(histIt.Next());
		}
	}

	/**
	 * Actualiza la vista de control de unidades de servicio evitando duplicidades visuales
	 * cuando un recurso se encuentra en proceso de cambio de estado.
	 */
	/**
	 * Actualiza la vista de control de unidades de servicio evitando duplicidades visuales
	 * cuando una grúa, moto o camioneta está esperando la confirmación de cambios del admin.
	 */
	private void refreshUnitsView() {
		unitsView.clearTable();
        
		SimpleList<ServiceUnitDTO> pendingUnits = getToConfirmServiceUnitsUseCase.execute();
		SimpleList<ServiceUnitDTO> allUnits = getSortedAndFilteredServiceUnitsUseCase.execute();
        
		// Filtrar y renderizar las unidades activas libres de transiciones pendientes
		SimpleList.Iterator<ServiceUnitDTO> uIt = allUnits.iterador();
		while (uIt.hasNext()) {
			ServiceUnitDTO activeUnit = uIt.Next();
			boolean isPending = false;
            
			SimpleList.Iterator<ServiceUnitDTO> checkIt = pendingUnits.iterador();
			while (checkIt.hasNext()) {
				if (checkIt.Next().getId().equals(activeUnit.getId())) {
					isPending = true;
					break;
				}
			}
            
			if (!isPending) {
				unitsView.addUnit(activeUnit, false); // Añade al contenedor de activas/inactivas normales
			}
		}
        
		// Renderizar las unidades que están esperando la aprobación en la pila de auditoría
		SimpleList.Iterator<ServiceUnitDTO> pIt = pendingUnits.iterador();
		while (pIt.hasNext()) {
			unitsView.addUnit(pIt.Next(), true); // Añade al contenedor de pendientes de confirmación
		}
	}

	private void refreshTechniciansView() {
		techniciansView.clearTable();
		SimpleList.Iterator<TechnicianDTO> tIt = getSortedAndDFilteredTechniciansUseCase.execute().iterador();
		while (tIt.hasNext()) {
			TechnicianDTO t = tIt.Next();
			techniciansView.addTechnician(t.getId().toString(), t.getName(), t.getSpecialty(), t.getStatus(),
					t.getZone(), t.isEditable());
		}
	}

	/**
	 * Actualiza el inventario de herramientas combinando los listados generales
	 * con la pila LIFO de mantenimiento, aislando los elementos para evitar duplicidad.
	 */
	private void refreshKitsView() {
		kitsView.clearTable();
        
		// 1. Cargar primero los kits en mantenimiento directo desde la pila LIFO activa (asigna el tope LIFO)
		SimpleList.Iterator<KitDTO> mIt = getMaintenanceKitsUseCase.execute().iterador();
		while (mIt.hasNext()) {
			kitsView.addKit(mIt.Next());
		}
        
		// 2. Cargar los kits disponibles, asignados e inactivos desde la lista maestra ordenada
		SimpleList.Iterator<KitDTO> kIt = getSortedKitsUseCase.execute().iterador();
		while (kIt.hasNext()) {
			KitDTO kit = kIt.Next();
			// CORRECCIÓN: Filtrar para que no se vuelvan a pintar en la sección de disponibles
			if (!kit.getStatus().equalsIgnoreCase("En mantenimiento") && !kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
				kitsView.addKit(kit);
			}
		}
	}

	private void refreshClientsView() {
		clientsView.clearTable();
		SimpleList.Iterator<ClientDTO> cIt = getSortedClientsUseCase.execute().iterador();
		while (cIt.hasNext()) {
			clientsView.addClient(cIt.Next());
		}
	}

	private void handleResponse(ResponseDTO response, Runnable onSuccess) {
		showNotification(response.isSuccess(), response.getMessage());
		if (response.isSuccess() && onSuccess != null) {
			onSuccess.run();
		}
	}

	private void showNotification(boolean success, String message) {
		Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void navigateToDashboard() {
		mainView.setContent(dashboardView.getView());
	}

	public void navigateToRequests() {
		mainView.setContent(requestsView.getView());
	}

	public void navigateToUnits() {
		mainView.setContent(unitsView.getView());
	}

	public void navigateToTechnicians() {
		mainView.setContent(techniciansView.getView());
	}

	public void navigateToKits() {
		mainView.setContent(kitsView.getView());
	}

	public void navigateToClients() {
		mainView.setContent(clientsView.getView());
	}

	public void logout() {
		saveSystemDataUseCase.execute();
		startApplication(this.primaryStage);
	}
}