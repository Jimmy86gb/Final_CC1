package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.*;
import co.edu.udistrital.model.enums.ProfileType;
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

    private ClientRepository clientRepository;
    private KitRepository kitRepository;
    private ReportRepository reportRepository;
    private ServiceUnitRepository serviceUnitRepository;
    private TechnicianRepository technicianRepository;
    private ProfileRepository profileRepository;

    // --- CASOS DE USO INTEGRADOS ---
    private RegisterClientUseCase registerClientUseCase;
    private UpdateClientUseCase updateClientUseCase;
    private GetSortedAndFilteredClientsUseCase getSortedClientsUseCase;
    private GetClientTypeLabelsUseCase getClientTypeLabelsUseCase;

    private RegisterTechnicianUseCase registerTechnicianUseCase;
    private UpdateTechnicianUseCase updateTechnicianUseCase;
    private GetSortedAndDFilteredTechniciansUseCase getSortedAndDFilteredTechniciansUseCase;
    private GetTechnicianStatusLabelsUseCase getTechnicianStatusLabelsUseCase;
    private GetTechnicianSpecialityLabelsUseCase getTechnicianSpecialityLabelsUseCase;
    private GetAvailableTechnicianByZoneAndProblemUseCase getAvailableTechnicianByZoneAndProblemUseCase;

    private RegisterServiceUnitUseCase registerServiceUnitUseCase;
    private UpdateServiceUnitUseCase updateServiceUnitUseCase;
    private ApproveUnitStatusUseCase approveUnitStatusUseCase;
    private RejectUnitStatusUseCase rejectUnitStatusUseCase;
    private GetToConfirmServiceUnitsUseCase getToConfirmServiceUnitsUseCase;
    private GetServiceUnitsStatusLabelsUseCase getServiceUnitsStatusLabelsUseCase;
    private GetServiceUnitsTypeLabelsUseCase getServiceUnitsTypeLabelsUseCase;
    private GetSortedAndFilteredServiceUnitsUseCase getSortedAndFilteredServiceUnitsUseCase;
    private GetAvailableUnitsByZoneUseCase getAvailableUnitsByZoneUseCase;

    private RegisterKitUseCase registerKitUseCase;
    private UpdateKitUseCase updateKitUseCase;
    private GetSortedAndFilteredKitsUseCase getSortedKitsUseCase;
    private RetireKitFromMaintenanceUseCase retireKitFromMaintenanceUseCase;
    private ReturnKitToServiceUseCase returnKitToServiceUseCase;
    private GetKitTypeLabelsUseCase getKitTypeLabelsUseCase;
    private GetKitStatusLabelsUseCase getKitStatusLabelsUseCase;
    private GetAvailableKitsByTypeUseCase getAvailableKitsByTypeUseCase;
    private GetMaintenanceKitsUseCase getMaintenanceKitsUseCase;

    private RegisterReportUseCase registerReportUseCase;
    private AssignResourcesReportUseCase assignResourcesReportUseCase;
    private UndoReportResourcesUseCase undoReportResourcesUseCase;
    private FinishReportInFieldUseCase finishReportInFieldUseCase;
    private ApproveReportActionUseCase approveReportActionUseCase;
    private RejectReportActionUseCase rejectReportActionUseCase;
    private RequestReportCancellationUseCase requestReportCancellationUseCase;
    private GetSortedAndFilteredReportsUseCase getSortedAndFilteredReportsUseCase;
    private GetOnGoingReportsUseCase getOnGoingReportsUseCase;
    private GetToConfirmReportsUseCase getToConfirmReportsUseCase;
    private GetNextPendingReportUseCase getNextPendingReportUseCase;
    private GenerateDailyCSVUseCase generateDailyCSVUseCase;
    private GetCriticLevelLabelsUseCase getCriticLevelLabelsUseCase;
    private GetZoneLabelsUseCase getZoneLabelsUseCase;

    private LoginUseCase loginUseCase;
    private SaveSystemDataUseCase saveSystemDataUseCase;
    private LoadSystemDataUseCase loadSystemDataUseCase;

    private ProfileType currentRole;
    private String roleType;
    private Stage primaryStage;
    private StringBuilder consoleHistory = new StringBuilder("Sistema AutoRescate Iniciado...\n");

    public AppController() {
        this.clientRepository = new ClientRepository();
        this.kitRepository = new KitRepository();
        this.reportRepository = new ReportRepository();
        this.serviceUnitRepository = new ServiceUnitRepository();
        this.technicianRepository = new TechnicianRepository();
        this.profileRepository = new ProfileRepository();

        this.registerClientUseCase = new RegisterClientUseCase(clientRepository);
        this.updateClientUseCase = new UpdateClientUseCase(clientRepository);
        this.getSortedClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepository);
        this.getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();

        this.registerTechnicianUseCase = new RegisterTechnicianUseCase(technicianRepository);
        this.updateTechnicianUseCase = new UpdateTechnicianUseCase(technicianRepository);
        this.getSortedAndDFilteredTechniciansUseCase = new GetSortedAndDFilteredTechniciansUseCase(technicianRepository);
        this.getTechnicianStatusLabelsUseCase = new GetTechnicianStatusLabelsUseCase();
        this.getTechnicianSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
        this.getAvailableTechnicianByZoneAndProblemUseCase = new GetAvailableTechnicianByZoneAndProblemUseCase(technicianRepository);

        this.registerServiceUnitUseCase = new RegisterServiceUnitUseCase(serviceUnitRepository);
        this.updateServiceUnitUseCase = new UpdateServiceUnitUseCase(serviceUnitRepository);
        this.approveUnitStatusUseCase = new ApproveUnitStatusUseCase(serviceUnitRepository);
        this.rejectUnitStatusUseCase = new RejectUnitStatusUseCase(serviceUnitRepository);
        this.getToConfirmServiceUnitsUseCase = new GetToConfirmServiceUnitsUseCase(serviceUnitRepository);
        this.getServiceUnitsStatusLabelsUseCase = new GetServiceUnitsStatusLabelsUseCase();
        this.getServiceUnitsTypeLabelsUseCase = new GetServiceUnitsTypeLabelsUseCase();
        this.getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(serviceUnitRepository);
        this.getAvailableUnitsByZoneUseCase = new GetAvailableUnitsByZoneUseCase(serviceUnitRepository);

        this.registerKitUseCase = new RegisterKitUseCase(kitRepository);
        this.updateKitUseCase = new UpdateKitUseCase(kitRepository);
        this.getSortedKitsUseCase = new GetSortedAndFilteredKitsUseCase(kitRepository);
        this.retireKitFromMaintenanceUseCase = new RetireKitFromMaintenanceUseCase(kitRepository);
        this.returnKitToServiceUseCase = new ReturnKitToServiceUseCase(kitRepository);
        this.getKitTypeLabelsUseCase = new GetKitTypeLabelsUseCase();
        this.getKitStatusLabelsUseCase = new GetKitStatusLabelsUseCase();
        this.getAvailableKitsByTypeUseCase = new GetAvailableKitsByTypeUseCase(kitRepository);
        this.getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepository);

        this.registerReportUseCase = new RegisterReportUseCase(clientRepository, reportRepository);
        this.assignResourcesReportUseCase = new AssignResourcesReportUseCase(reportRepository, technicianRepository, serviceUnitRepository, kitRepository);
        this.undoReportResourcesUseCase = new UndoReportResourcesUseCase(reportRepository);
        this.finishReportInFieldUseCase = new FinishReportInFieldUseCase(reportRepository);
        this.approveReportActionUseCase = new ApproveReportActionUseCase(reportRepository, kitRepository);
        this.rejectReportActionUseCase = new RejectReportActionUseCase(reportRepository);
        this.requestReportCancellationUseCase = new RequestReportCancellationUseCase(reportRepository);
        this.getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepository);
        this.getOnGoingReportsUseCase = new GetOnGoingReportsUseCase(reportRepository);
        this.getToConfirmReportsUseCase = new GetToConfirmReportsUseCase(reportRepository);
        this.getNextPendingReportUseCase = new GetNextPendingReportUseCase(reportRepository);
        this.generateDailyCSVUseCase = new GenerateDailyCSVUseCase(reportRepository);
        this.getCriticLevelLabelsUseCase = new GetCriticLevelLabelsUseCase();
        this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();

        this.loginUseCase = new LoginUseCase(profileRepository);
        this.saveSystemDataUseCase = new SaveSystemDataUseCase(profileRepository,  clientRepository, technicianRepository, serviceUnitRepository, kitRepository, reportRepository);
        this.loadSystemDataUseCase = new LoadSystemDataUseCase(profileRepository,  clientRepository, technicianRepository, serviceUnitRepository, kitRepository, reportRepository);
    }

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        loadSystemDataUseCase.execute(); // Persistencia inicial

        if (clientRepository.getAllClients().getSize() == 0) {
            injectSeedData();
        }
        
        loginView = new LoginView();
        loginView.setOnLoginAction(() -> {
            SessionDTO session = loginUseCase.execute(loginView.getUsername(), loginView.getPassword());
            if (session.isSuccess()) {
                logAction("SESION INICIADA: " + session.getRole());
                initializeSystem(ProfileType.valueOf(session.getRole()));
            } else {
                loginView.showMessage(session.getMessage());
            }
        });
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }

    private void initializeSystem(ProfileType role) {
        this.currentRole = role;
        if(role == ProfileType.ADMIN) {
        	roleType = "ADMIN";
        }else {
        	roleType = "OPERATOR";
        }
        mainView = new MainView(roleType);
        dashboardView = new DashboardView(roleType, this);
        requestsView = new RequestsView(roleType);
        unitsView = new UnitsView(roleType);
        techniciansView = new TechniciansView(roleType);
        kitsView = new KitsView(roleType);
        clientsView = new ClientsView(roleType);

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

    // --- CONSOLA Y LIFO GLOBAL ---
    
    private void logAction(String message) {
        consoleHistory.append("> ").append(message).append("\n");
        if (dashboardView != null) dashboardView.updateLog(message);
    }

    public String getConsoleHistory() { return consoleHistory.toString(); }

    public void performUndo() {
        if (currentRole != ProfileType.ADMIN) {
            showNotification(false, "Acceso denegado: Solo Administradores pueden revertir el sistema.");
            return;
        }
        ResponseDTO res = undoReportResourcesUseCase.execute();
        if (res.isSuccess()) logAction("REVERSIÓN EJECUTADA: " + res.getMessage());
        handleResponse(res, this::refreshAllViews);
    }

    // --- LABELS (ETIQUETAS DINÁMICAS PARA COMBOBOXES) ---
    public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
    public SimpleList<String> getCriticLevelLabels() { return getCriticLevelLabelsUseCase.execute(); }
    public SimpleList<String> getSpecialityLabels() { return getTechnicianSpecialityLabelsUseCase.execute(); }
    public SimpleList<String> getClientTypeLabels() { return getClientTypeLabelsUseCase.execute(); }
    public SimpleList<String> getUnitTypeLabels() { return getServiceUnitsTypeLabelsUseCase.execute(); }
    public SimpleList<String> getUnitStatusLabels() { return getServiceUnitsStatusLabelsUseCase.execute(); }
    public SimpleList<String> getKitTypeLabels() { return getKitTypeLabelsUseCase.execute(); }
    public SimpleList<String> getKitStatusLabels() { return getKitStatusLabelsUseCase.execute(); }
    public SimpleList<String> getTechnicianStatusLabels() { return getTechnicianStatusLabelsUseCase.execute(); }

    // --- FILTRADOS DE SUGERENCIAS USANDO DTOs CORRECTAMENTE ---
    public SimpleList<EntityItem> getSuggestedTechnicians(String zone, String spec) {
    	String safeZone = zone != null ? zone : "";
        String safeSpec = spec != null ? spec : "";
        
        SimpleList<EntityItem> list = new SimpleList<>();
        SimpleList.Iterator<TechnicianDTO> it = getAvailableTechnicianByZoneAndProblemUseCase.execute(safeZone, safeSpec).iterador();
        while(it.hasNext()) { TechnicianDTO t = it.Next(); list.add(new EntityItem(t.getId().toString(), t.getName() + " (" + t.getSpecialty() + ")")); }
        return list;
    }

    public SimpleList<EntityItem> getSuggestedUnits(String zone) {
    	String safeZone = zone != null ? zone : "";
        
        SimpleList<EntityItem> list = new SimpleList<>();
        SimpleList.Iterator<ServiceUnitDTO> it = getAvailableUnitsByZoneUseCase.execute(safeZone).iterador();
        while(it.hasNext()) { ServiceUnitDTO u = it.Next(); list.add(new EntityItem(u.getId().toString(), u.getType())); }
        return list;
    }

    public SimpleList<EntityItem> getAvailableKitsForUI(String type) {
    	String safeType = type != null ? type : "";
        
        SimpleList<EntityItem> list = new SimpleList<>();
        SimpleList.Iterator<KitDTO> it = getAvailableKitsByTypeUseCase.execute(safeType).iterador();
        while(it.hasNext()) { KitDTO k = it.Next(); list.add(new EntityItem(k.getId().toString(), k.getType())); }
        return list;
    }

    // --- MÉTODOS CRUD (CREAR Y ACTUALIZAR) ---
    public void registerClient(String id, String name, String type, String contact) {
        handleResponse(registerClientUseCase.ResponseDTO(id, name, type, contact), this::refreshAllViews);
    }
    public void updateClient(String id, String name, String type, String contact) {
        handleResponse(updateClientUseCase.execute(id, name, type, contact), this::refreshAllViews);
    }
    
    public void registerTechnician(String name, String specialty, String zone) {
        handleResponse(registerTechnicianUseCase.execute(name, specialty, zone), this::refreshAllViews);
    }
    public void updateTechnician(String id, String name, String specialty, String zone, String status) {
        handleResponse(updateTechnicianUseCase.execute(id, name, specialty, zone, status), this::refreshAllViews);
    }

    public void registerUnit(String type, String zone, int quantity) {
        handleResponse(registerServiceUnitUseCase.execute(type, zone, quantity), this::refreshAllViews);
    }
    public void updateServiceUnit(String id, String type, String status, String zone) {
        handleResponse(updateServiceUnitUseCase.execute(id, type, status, zone), this::refreshAllViews);
    }
    public void approveUnitStatus() { // Usado por Admin para unidades "Por Confirmar"
        handleResponse(approveUnitStatusUseCase.execute(), this::refreshAllViews);
    }
    public void rejectUnitStatus() {
        handleResponse(rejectUnitStatusUseCase.execute(), this::refreshAllViews);
    }

    public void registerKit(String type, int quantity) {
        handleResponse(registerKitUseCase.execute(type, quantity), this::refreshAllViews);
    }
    public void updateKitToMaintenance(String id, String type) {
        handleResponse(updateKitUseCase.execute(id, type, "Mantenimiento"), this::refreshAllViews);
    }
    public void returnKitToService() {
        handleResponse(returnKitToServiceUseCase.execute(), this::refreshAllViews);
    }
    public void retireKitFromMaintenance() {
        handleResponse(retireKitFromMaintenanceUseCase.execute(), this::refreshAllViews);
    }

    // --- REPORTES Y EMERGENCIAS ---
    public void processNewRequest(String clientId) {
        Client client = clientRepository.getClientByID(clientId.trim());
        if (client == null) {
            navigateToClients(); clientsView.showAddClientDialog(clientId);
        } else {
            requestsView.showCreateReportDialog(client);
        }
    }
    public void submitReportCreation(String cliId, String desc, String type, String prio, String zone) {
        ResponseDTO res = registerReportUseCase.execute(cliId, desc, type, prio, zone);
        if(res.isSuccess()) logAction("EMERGENCIA REGISTRADA: Cliente " + cliId);
        handleResponse(res, this::refreshAllViews);
    }
    public void assignReportResources(String techId, String unitId, String kitId) {
        ResponseDTO res = assignResourcesReportUseCase.execute(techId, unitId, kitId);
        if(res.isSuccess()) logAction("ASIGNACIÓN: TKT vinculado a Tec: " + techId + " / Unidad: " + unitId);
        handleResponse(res, this::refreshAllViews);
    }
    public void finishReport(String ticketId) {
        ResponseDTO res = finishReportInFieldUseCase.execute(ticketId);
        if(res.isSuccess()) logAction("FINALIZADO EN CAMPO: Siniestro " + ticketId);
        handleResponse(res, this::refreshAllViews);
    }
    public void approveReport() {
        ResponseDTO res = approveReportActionUseCase.execute();
        if(res.isSuccess()) logAction("APROBADO POR ADMIN: Cierre exitoso");
        handleResponse(res, this::refreshAllViews);
    }
    public void rejectReport() {
        ResponseDTO res = rejectReportActionUseCase.execute();
        if(res.isSuccess()) logAction("RECHAZADO: Siniestro retornado a progreso");
        handleResponse(res, this::refreshAllViews);
    }
    public void cancelReport(String ticketId) {
        handleResponse(requestReportCancellationUseCase.execute(ticketId), this::refreshAllViews);
    }
    public ReportDTO getNextPendingReport() { return getNextPendingReportUseCase.execute(); }
    public void exportDailyReport() {
        handleResponse(generateDailyCSVUseCase.execute(""), () -> logAction("REPORTE CSV: Generado con éxito."));
    }

    // --- REFRESHERS (CORRECCIÓN DE DTOs) ---
    public void refreshAllViews() {
        refreshClientsView(); refreshKitsView(); refreshUnitsView();
        refreshTechniciansView(); refreshRequestsView(); refreshDashboardView();
        saveSystemDataUseCase.execute(); // Persistencia en cada cambio
    }

    private void refreshDashboardView() {
        if (dashboardView != null) {
            int activeUnits = 0, critical = 0, maint = 0, total = 0;
            
            SimpleList.Iterator<ServiceUnitDTO> uit = getSortedAndFilteredServiceUnitsUseCase.execute().iterador();
            while(uit.hasNext()) { if(!uit.Next().getStatus().equals("Mantenimiento")) activeUnits++; }
            
            maint = getMaintenanceKitsUseCase.execute().getSize();
            
            SimpleList.Iterator<ReportDTO> rit = getSortedAndFilteredReportsUseCase.execute().iterador();
            while(rit.hasNext()) { ReportDTO r = rit.Next(); total++; if(r.getPriority().equals("Alta")) critical++; }
            
            SimpleList.Iterator<ReportDTO> oit = getOnGoingReportsUseCase.execute().iterador();
            while(oit.hasNext()) { ReportDTO r = oit.Next(); total++; if(r.getPriority().equals("Alta")) critical++; }

            dashboardView.updateStatistics(String.valueOf(activeUnits), String.valueOf(critical), String.valueOf(maint), String.valueOf(total));
        }
    }

    private void refreshRequestsView() {
        requestsView.clearPanels();
        SimpleList.Iterator<ReportDTO> pendIt = getSortedAndFilteredReportsUseCase.execute().iterador();
        while(pendIt.hasNext()) requestsView.addReportCard(pendIt.Next(), "PENDING");
        
        SimpleList.Iterator<ReportDTO> ongoIt = getOnGoingReportsUseCase.execute().iterador();
        while(ongoIt.hasNext()) requestsView.addReportCard(ongoIt.Next(), "ONGOING");
        
        SimpleList.Iterator<ReportDTO> confIt = getToConfirmReportsUseCase.execute().iterador();
        while(confIt.hasNext()) requestsView.addReportCard(confIt.Next(), "CONFIRM");
    }

    private void refreshUnitsView() {
        unitsView.clearTable();
        SimpleList.Iterator<ServiceUnitDTO> uIt = getSortedAndFilteredServiceUnitsUseCase.execute().iterador();
        while(uIt.hasNext()) {
            ServiceUnitDTO u = uIt.Next();
            unitsView.addUnit(u.getId().toString(), u.getType(), u.getStatus(), u.getZone(), u.getStatus().equals("Disponible"));
        }
    }

    private void refreshTechniciansView() {
        techniciansView.clearTable();
        SimpleList.Iterator<TechnicianDTO> tIt = getSortedAndDFilteredTechniciansUseCase.execute().iterador();
        while(tIt.hasNext()) {
            TechnicianDTO t = tIt.Next();
            techniciansView.addTechnician(t.getId().toString(), t.getName(), t.getSpecialty(), t.getStatus(), t.getZone(), t.getStatus().equals("Disponible"));
        }
    }

    private void refreshKitsView() {
        kitsView.clearTable();
        SimpleList.Iterator<KitDTO> kIt = getSortedKitsUseCase.execute().iterador();
        while(kIt.hasNext()) kitsView.addKit(kIt.Next());
    }

    private void refreshClientsView() {
        clientsView.clearTable();
        SimpleList.Iterator<ClientDTO> cIt = getSortedClientsUseCase.execute().iterador();
        while(cIt.hasNext()) clientsView.addClient(cIt.Next());
    }

    private void handleResponse(ResponseDTO response, Runnable onSuccess) {
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess() && onSuccess != null) onSuccess.run();
    }

    private void showNotification(boolean success, String message) {
        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setContentText(message); alert.showAndWait();
    }

    public void navigateToDashboard() { mainView.setContent(dashboardView.getView()); }
    public void navigateToRequests() { mainView.setContent(requestsView.getView()); }
    public void navigateToUnits() { mainView.setContent(unitsView.getView()); }
    public void navigateToTechnicians() { mainView.setContent(techniciansView.getView()); }
    public void navigateToKits() { mainView.setContent(kitsView.getView()); }
    public void navigateToClients() { mainView.setContent(clientsView.getView()); }

    public void logout() {
        saveSystemDataUseCase.execute(); // Asegurar grabado en disco
        logAction("SESION CERRADA");
        this.currentRole = null;
        startApplication(this.primaryStage);
    }
    
    /**
     * Inyecta datos predefinidos de prueba para la sustentación.
     * Solo se ejecuta si el sistema no encuentra archivos de guardado previos.
     */
    private void injectSeedData() {
        System.out.println("--- INICIANDO INYECCIÓN DE DATOS SEMILLA ---");

        // 1. Crear Clientes
        registerClientUseCase.ResponseDTO("102030", "Empresa de Transportes VIP", "Empresarial", "3001234567");
        registerClientUseCase.ResponseDTO("405060", "Aseguradora Solidaria", "Seguros", "3109876543");
        registerClientUseCase.ResponseDTO("708090", "Juan Perez", "Particular", "3201112233");

        // 2. Crear Técnicos (Usamos los nombres técnicos sin espacios como los espera el Factory)
        registerTechnicianUseCase.execute("Carlos Ramirez", "OperadordeGrua", "Kennedy");
        registerTechnicianUseCase.execute("Julian Perez", "MecanicoGeneral", "Suba");
        registerTechnicianUseCase.execute("Ana Gomez", "ElectricoAutomotriz", "Chapinero");
        registerTechnicianUseCase.execute("Luis Martinez", "CerrajerodeVehiculos", "Usaquen");

        // 3. Crear Unidades de Servicio
        registerServiceUnitUseCase.execute("Grua", "Kennedy", 2);
        registerServiceUnitUseCase.execute("Moto", "Suba", 3);
        registerServiceUnitUseCase.execute("Camioneta", "Chapinero", 2);

        // 4. Crear Lotes de Kits
        registerKitUseCase.execute("KitdeGrua", 3);
        registerKitUseCase.execute("KitGeneral", 5);
        registerKitUseCase.execute("KitdeElectricidad", 2);

        // 5. Crear Solicitudes / Siniestros
        registerReportUseCase.execute("102030", "Camión bloqueado, falla de motor principal.", "MecanicoGeneral", "Alta", "Suba");
        registerReportUseCase.execute("405060", "Choque múltiple en la avenida, requiere traslado.", "OperadordeGrua", "Alta", "Kennedy");
        registerReportUseCase.execute("708090", "Vehículo no enciende, posible batería muerta.", "ElectricoAutomotriz", "Media", "Chapinero");
        registerReportUseCase.execute("102030", "Llaves olvidadas dentro del vehículo.", "CerrajerodeVehiculos", "Baja", "Usaquen");

        // 6. Guardar automáticamente en disco para que no se vuelva a inyectar la próxima vez
        saveSystemDataUseCase.execute();
        
        System.out.println("--- DATOS SEMILLA INYECTADOS Y GUARDADOS CON ÉXITO ---");
    }
}