package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.entities.*;
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

    private RegisterClientUseCase registerClientUseCase;
    private GetSortedAndFilteredClientsUseCase getSortedClientsUseCase;
    private RegisterKitUseCase registerKitUseCase;
    private UpdateKitUseCase updateKitUseCase;
    private GetSortedAndFilteredKitsUseCase getSortedKitsUseCase;
    private RetireKitFromMaintenanceUseCase retireKitFromMaintenanceUseCase;
    private ReturnKitToServiceUseCase returnKitToServiceUseCase;
    private GetKitTypeLabelsUseCase getKitTypeLabelsUseCase;
    private RegisterServiceUnitUseCase registerServiceUnitUseCase;
    private RegisterTechnicianUseCase registerTechnicianUseCase;
    private LoginUseCase loginUseCase;
    
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

    private ProfileType currentRole;
    private Stage primaryStage;
    
    private StringBuilder consoleHistory = new StringBuilder("Sistema iniciado...\n");

    public AppController() {
        this.clientRepository = new ClientRepository();
        this.kitRepository = new KitRepository();
        this.reportRepository = new ReportRepository();
        this.serviceUnitRepository = new ServiceUnitRepository();
        this.technicianRepository = new TechnicianRepository();
        this.profileRepository = new ProfileRepository();

        this.registerClientUseCase = new RegisterClientUseCase(clientRepository);
        this.getSortedClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepository);
        this.registerKitUseCase = new RegisterKitUseCase(kitRepository);
        this.updateKitUseCase = new UpdateKitUseCase(kitRepository);
        this.getSortedKitsUseCase = new GetSortedAndFilteredKitsUseCase(kitRepository);
        this.retireKitFromMaintenanceUseCase = new RetireKitFromMaintenanceUseCase(kitRepository);
        this.returnKitToServiceUseCase = new ReturnKitToServiceUseCase(kitRepository);
        this.getKitTypeLabelsUseCase = new GetKitTypeLabelsUseCase();

        this.registerServiceUnitUseCase = new RegisterServiceUnitUseCase(serviceUnitRepository);
        this.registerTechnicianUseCase = new RegisterTechnicianUseCase(technicianRepository);
        this.loginUseCase = new LoginUseCase(profileRepository);

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
    }

    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginView = new LoginView();
        loginView.setOnLoginAction(() -> {
            SessionDTO session = loginUseCase.execute(loginView.getUsername(), loginView.getPassword());
            if (session.isSuccess()) initializeSystem(ProfileType.valueOf(session.getRole()));
            else loginView.showMessage(session.getMessage());
        });
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }

    private void initializeSystem(ProfileType role) {
        this.currentRole = role;
        mainView = new MainView(role);
        dashboardView = new DashboardView(role);
        requestsView = new RequestsView(role);
        unitsView = new UnitsView(role);
        techniciansView = new TechniciansView(role);
        kitsView = new KitsView(role);
        clientsView = new ClientsView(role);

        if (clientRepository.getAllClients().getSize() == 0) seedData();

        mainView.setNavigationController(this);
        clientsView.setController(this);
        unitsView.setController(this);
        kitsView.setController(this);
        techniciansView.setController(this);
        requestsView.setController(this);
        dashboardView.setController(this);

        dashboardView.updateLog(getConsoleHistory());
        refreshAllViews();
        primaryStage.setScene(mainView.getScene());
        primaryStage.centerOnScreen();
    }
    
    /**
     * Helper para enviar mensajes al log del Dashboard (Consola)
     */
 // 2. Modifica tu método logToDashboard
    private void logToDashboard(String message) {
        String entry = "> " + message + "\n";
        consoleHistory.append(entry); // Guardamos en la "memoria" del controlador
        if (dashboardView != null) {
            dashboardView.updateLog(message);
        }
    }

    // --- FLUJO DE EMERGENCIAS ---

    public void processNewRequest(String clientId) {
        Client client = clientRepository.getClientByID(clientId.trim());
        if (client == null) {
            navigateToClients();
            clientsView.showAddClientDialog(clientId);
        } else {
            requestsView.showCreateReportDialog(client);
        }
    }

    public void submitReportCreation(String clientId, String description, String type, String priority, String zone) {
        handleResponse(registerReportUseCase.execute(clientId, description, type.replace(" ", ""), priority.replace(" ", ""), zone.replace(" ", "")), this::refreshAllViews);
    }

    public ReportDTO getNextPendingReport() {
        return getNextPendingReportUseCase.execute();
    }

    public void assignReportResources(String techId, String unitId, String kitId) {
        ResponseDTO response = assignResourcesReportUseCase.execute(techId, unitId, kitId);
        if (response.isSuccess()) {
            logToDashboard("ASIGNACIÓN: TKT Asignado a Técnico " + techId);
        }
        handleResponse(response, this::refreshAllViews);
    }

    public void undoReport() {
        handleResponse(undoReportResourcesUseCase.execute(), this::refreshAllViews);
    }

    public void finishReport(String ticketId) {
        handleResponse(finishReportInFieldUseCase.execute(ticketId), this::refreshAllViews);
    }

    public void approveReport() {
        ResponseDTO response = approveReportActionUseCase.execute();
        if (response.isSuccess()) {
            logToDashboard("APROBACIÓN: Servicio consolidado y cerrado.");
        }
        handleResponse(response, this::refreshAllViews);
    }

    public void rejectReport() {
        handleResponse(rejectReportActionUseCase.execute(), this::refreshAllViews);
    }

    public void cancelReport(String ticketId) {
        handleResponse(requestReportCancellationUseCase.execute(ticketId), this::refreshAllViews);
    }

    public void exportDailyReport() {
        handleResponse(generateDailyCSVUseCase.execute(""), null);
    }

    // --- FLUJO KITS (MANTENIMIENTO EN PILA) ---

    public void updateKitToMaintenance(String id, String type) {
        handleResponse(updateKitUseCase.execute(id, type.replace(" ", ""), "Mantenimiento"), this::refreshAllViews);
    }

    public void returnKitToService() {
        handleResponse(returnKitToServiceUseCase.execute(), this::refreshAllViews);
    }

    public void retireKitFromMaintenance() {
        handleResponse(retireKitFromMaintenanceUseCase.execute(), this::refreshAllViews);
    }

    // --- REGISTRO DE ENTIDADES ---

    public void registerClient(String id, String name, String type, String contact) {
        handleResponse(registerClientUseCase.ResponseDTO(id, name, type.replace(" ", ""), contact), this::refreshAllViews);
    }

    public void registerTechnician(String name, String specialty, String zone) {
        handleResponse(registerTechnicianUseCase.execute(name, specialty.replace(" ", ""), zone.replace(" ", "")), this::refreshAllViews);
    }

    public void registerUnit(String type, String zone, int quantity) {
        handleResponse(registerServiceUnitUseCase.execute(type.replace(" ", ""), zone.replace(" ", ""), quantity), this::refreshAllViews);
    }

    public void registerKit(String type, int quantity) {
        handleResponse(registerKitUseCase.execute(type.replace(" ", ""), quantity), this::refreshAllViews);
    }

    // --- FILTRADO INTELIGENTE PARA LA VISTA ---

    public SimpleList<String> getKitTypes() { 
        return getKitTypeLabelsUseCase.execute(); 
    }

    public SimpleList<EntityItem> getSuggestedTechnicians(String zoneName, String specialtyName) {
        SimpleList<EntityItem> list = new SimpleList<>();
        SimpleList.Iterator<Technician> it = technicianRepository.getAllTechnicians().iterador();
        
        String cleanZone = zoneName.replace(" ", "").toLowerCase();
        String cleanSpec = specialtyName.replace(" ", "").toLowerCase();

        while (it.hasNext()) {
            Technician t = it.Next();
            if (t.getStatus() == co.edu.udistrital.model.enums.TechnicianStatus.AVAILABLE &&
                t.getZone().getDisplayName().replace(" ", "").toLowerCase().equals(cleanZone) &&
                t.getSpecialty().getDisplayName().replace(" ", "").toLowerCase().equals(cleanSpec)) {
                
                list.add(new EntityItem(t.getId().toString(), t.getName() + " (" + t.getSpecialty().getDisplayName() + ")"));
            }
        }
        return list;
    }

    public SimpleList<EntityItem> getSuggestedUnits(String zoneName) {
        SimpleList<EntityItem> list = new SimpleList<>();
        SimpleList.Iterator<ServiceUnit> it = serviceUnitRepository.getAllUnits().iterador();
        String cleanZone = zoneName.replace(" ", "").toLowerCase();

        while (it.hasNext()) {
            ServiceUnit u = it.Next();
            if (u.getStatus() == co.edu.udistrital.model.enums.UnitStatus.AVAILABLE &&
                u.getZone().getDisplayName().replace(" ", "").toLowerCase().equals(cleanZone)) {
                list.add(new EntityItem(u.getId().toString(), u.getType().getDisplayName()));
            }
        }
        return list;
    }

    public SimpleList<EntityItem> getAvailableKitsForUI() {
        SimpleList<EntityItem> list = new SimpleList<>();
        SimpleList.Iterator<Kit> it = kitRepository.getAllKits().iterador();
        while (it.hasNext()) {
            Kit k = it.Next();
            if (k.getStatus() == co.edu.udistrital.model.enums.UnitStatus.AVAILABLE) {
                list.add(new EntityItem(k.getId().toString(), k.getType().getDisplayName()));
            }
        }
        return list;
    }
    
    public String getConsoleHistory() {
        return consoleHistory.toString();
    }

    // --- REFRESHERS CON ITERADORES NATIVOS ---

    public void refreshAllViews() {
        refreshClientsView(); 
        refreshKitsView(); 
        refreshUnitsView();
        refreshTechniciansView(); 
        refreshRequestsView(); 
        refreshDashboardView();
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

    private void refreshClientsView() {
        clientsView.clearTable();
        SimpleList.Iterator<ClientDTO> cIt = getSortedClientsUseCase.execute().iterador();
        while(cIt.hasNext()) clientsView.addClient(cIt.Next());
    }
    
    private void refreshKitsView() {
        kitsView.clearTable();
        SimpleList.Iterator<KitDTO> kIt = getSortedKitsUseCase.execute().iterador();
        while(kIt.hasNext()) kitsView.addKit(kIt.Next());
    }

    private void refreshUnitsView() {
        unitsView.clearTable();
        SimpleList.Iterator<ServiceUnit> uIt = serviceUnitRepository.getAllUnits().iterador();
        while(uIt.hasNext()) {
            ServiceUnit u = uIt.Next();
            unitsView.addUnit(u.getId().toString(), u.getType().getDisplayName(), u.getStatus().getDisplayName(), u.getZone().getDisplayName(), u.getStatus().getDisplayName().equals("Disponible"));
        }
    }

    private void refreshTechniciansView() {
        techniciansView.clearTable();
        SimpleList.Iterator<Technician> tIt = technicianRepository.getAllTechnicians().iterador();
        while(tIt.hasNext()) {
            Technician t = tIt.Next();
            techniciansView.addTechnician(t.getId().toString(), t.getName(), t.getSpecialty().getDisplayName(), t.getStatus().getDisplayName(), t.getZone().getDisplayName(), t.getStatus().getDisplayName().equals("Disponible"));
        }
    }

    private void refreshDashboardView() {
        if (dashboardView != null) {
            int activeUnits = 0, critical = 0, maint = 0, total = 0;
            
            SimpleList.Iterator<ServiceUnit> uit = serviceUnitRepository.getAllUnits().iterador();
            while(uit.hasNext()) { 
                if(uit.Next().getStatus() != co.edu.udistrital.model.enums.UnitStatus.MAINTENANCE) activeUnits++; 
            }
            
            maint = kitRepository.getMaintenanceKits().getSize();
            
            SimpleList.Iterator<Report> rit = reportRepository.getAllReports().iterador();
            while(rit.hasNext()) { 
                Report r = rit.Next(); 
                total++;
                if(r.getPriority() == co.edu.udistrital.model.enums.CriticLevel.HIGH) critical++;
            }
            dashboardView.updateStatistics(String.valueOf(activeUnits), String.valueOf(critical), String.valueOf(maint), String.valueOf(total));
        }
    }

    private void handleResponse(ResponseDTO response, Runnable onSuccess) {
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess() && onSuccess != null) onSuccess.run();
    }

    private void showNotification(boolean success, String message) {
        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Permite al supervisor revertir la última operación realizada en el sistema.
     * Esto cumple con el requerimiento de restaurar consistencia ante errores de despacho.
     */
 // --- AHORA, MODIFICA TUS MÉTODOS DE ACCIÓN ASÍ ---

    public void performUndo() {
        if (currentRole != ProfileType.ADMIN) {
            showNotification(false, "Acceso denegado.");
            return;
        }
        ResponseDTO response = undoReportResourcesUseCase.execute();
        if (response.isSuccess()) {
            logToDashboard("REVERSIÓN: " + response.getMessage());
        }
        handleResponse(response, this::refreshAllViews);
    }

    // --- NAVEGACIÓN ---
    public void navigateToDashboard() { mainView.setContent(dashboardView.getView()); }
    public void navigateToRequests() { mainView.setContent(requestsView.getView()); }
    public void navigateToUnits() { mainView.setContent(unitsView.getView()); }
    public void navigateToTechnicians() { mainView.setContent(techniciansView.getView()); }
    public void navigateToKits() { mainView.setContent(kitsView.getView()); }
    public void navigateToClients() { mainView.setContent(clientsView.getView()); }

    public void logout() {
        this.currentRole = null;
        startApplication(this.primaryStage);
    }

    private void seedData() {
        registerClientUseCase.ResponseDTO("101010", "Transportes Rapidos SAS", "Empresarial", "3001112233");
        registerTechnicianUseCase.execute("Carlos Ramirez", "MecanicoGeneral", "Kennedy");
        registerTechnicianUseCase.execute("Julian Perez", "OperadordeGrua", "Suba");
        registerServiceUnitUseCase.execute("Grua", "Suba", 2);
        registerServiceUnitUseCase.execute("Moto", "Kennedy", 2);
        registerKitUseCase.execute("KitGeneral", 2);
        registerKitUseCase.execute("KitdeGrua", 2);
        registerReportUseCase.execute("101010", "Camión varado por motor", "MecanicoGeneral", "Alta", "Kennedy");
        registerReportUseCase.execute("101010", "Estrellada fuerte", "OperadordeGrua", "Alta", "Suba");
    }
}