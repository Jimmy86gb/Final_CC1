package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.dtos.EntityItem;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.dtos.SessionDTO;
import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ProfileRepository;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
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

    private MainView       mainView;
    private DashboardView  dashboardView;
    private RequestsView   requestsView;
    private UnitsView      unitsView;
    private TechniciansView techniciansView;
    private KitsView       kitsView;
    private ClientsView    clientsView;
    private LoginView      loginView;

    private final ClientRepository      clientRepository;
    private final KitRepository         kitRepository;
    private final ReportRepository      reportRepository;
    private final ServiceUnitRepository serviceUnitRepository;
    private final TechnicianRepository  technicianRepository;
    private final ProfileRepository     profileRepository;

    private final RegisterClientUseCase                  registerClientUseCase;
    private final UpdateClientUseCase                    updateClientUseCase;
    private final GetSortedAndFilteredClientsUseCase     getSortedClientsUseCase;
    private final GetClientTypeLabelsUseCase             getClientTypeLabelsUseCase;

    private final RegisterTechnicianUseCase                        registerTechnicianUseCase;
    private final UpdateTechnicianUseCase                          updateTechnicianUseCase;
    private final GetSortedAndDFilteredTechniciansUseCase          getSortedAndDFilteredTechniciansUseCase;
    private final GetTechnicianStatusLabelsUseCase                 getTechnicianStatusLabelsUseCase;
    private final GetTechnicianSpecialityLabelsUseCase             getTechnicianSpecialityLabelsUseCase;
    private final GetAvailableTechnicianByZoneAndProblemUseCase    getAvailableTechnicianByZoneAndProblemUseCase;

    private final RegisterServiceUnitUseCase                 registerServiceUnitUseCase;
    private final UpdateServiceUnitUseCase                   updateServiceUnitUseCase;
    private final ApproveUnitStatusUseCase                   approveUnitStatusUseCase;
    private final RejectUnitStatusUseCase                    rejectUnitStatusUseCase;
    private final GetToConfirmServiceUnitsUseCase            getToConfirmServiceUnitsUseCase;
    private final GetServiceUnitsStatusLabelsUseCase         getServiceUnitsStatusLabelsUseCase;
    private final GetServiceUnitsTypeLabelsUseCase           getServiceUnitsTypeLabelsUseCase;
    private final GetSortedAndFilteredServiceUnitsUseCase    getSortedAndFilteredServiceUnitsUseCase;
    private final GetAvailableUnitsByZoneUseCase             getAvailableUnitsByZoneUseCase;

    private final RegisterKitUseCase                 registerKitUseCase;
    private final UpdateKitUseCase                   updateKitUseCase;
    private final GetSortedAndFilteredKitsUseCase    getSortedKitsUseCase;
    private final RetireKitFromMaintenanceUseCase    retireKitFromMaintenanceUseCase;
    private final ReturnKitToServiceUseCase          returnKitToServiceUseCase;
    private final GetKitTypeLabelsUseCase            getKitTypeLabelsUseCase;
    private final GetKitStatusLabelsUseCase          getKitStatusLabelsUseCase;
    private final GetAvailableKitsByTypeUseCase      getAvailableKitsByTypeUseCase;
    private final GetMaintenanceKitsUseCase          getMaintenanceKitsUseCase;

    private final RegisterReportUseCase                 registerReportUseCase;
    private final AssignResourcesReportUseCase          assignResourcesReportUseCase;
    private final UndoReportResourcesUseCase            undoReportResourcesUseCase;
    private final FinishReportInFieldUseCase            finishReportInFieldUseCase;
    private final ApproveReportActionUseCase            approveReportActionUseCase;
    private final RejectReportActionUseCase             rejectReportActionUseCase;
    private final RequestReportCancellationUseCase      requestReportCancellationUseCase;
    private final GetSortedAndFilteredReportsUseCase    getSortedAndFilteredReportsUseCase;
    private final GetOnGoingReportsUseCase              getOnGoingReportsUseCase;
    private final GetToConfirmReportsUseCase            getToConfirmReportsUseCase;
    private final GetNextPendingReportUseCase           getNextPendingReportUseCase;
    private final GenerateDailyCSVUseCase               generateDailyCSVUseCase;
    private final GetCriticLevelLabelsUseCase           getCriticLevelLabelsUseCase;
    private final GetZoneLabelsUseCase                  getZoneLabelsUseCase;

    private final LoginUseCase           loginUseCase;
    private final SaveSystemDataUseCase  saveSystemDataUseCase;
    private final LoadSystemDataUseCase  loadSystemDataUseCase;

    private ProfileType       currentRole;
    private String            roleType;
    private Stage             primaryStage;
    private final StringBuilder consoleHistory = new StringBuilder("Sistema AutoRescate Iniciado...\n");

    public AppController() {
        this.clientRepository      = new ClientRepository();
        this.kitRepository         = new KitRepository();
        this.reportRepository      = new ReportRepository();
        this.serviceUnitRepository = new ServiceUnitRepository();
        this.technicianRepository  = new TechnicianRepository();
        this.profileRepository     = new ProfileRepository();

        registerClientUseCase      = new RegisterClientUseCase(clientRepository);
        updateClientUseCase        = new UpdateClientUseCase(clientRepository);
        getSortedClientsUseCase    = new GetSortedAndFilteredClientsUseCase(clientRepository);
        getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();

        registerTechnicianUseCase                     = new RegisterTechnicianUseCase(technicianRepository);
        updateTechnicianUseCase                       = new UpdateTechnicianUseCase(technicianRepository);
        getSortedAndDFilteredTechniciansUseCase       = new GetSortedAndDFilteredTechniciansUseCase(technicianRepository);
        getTechnicianStatusLabelsUseCase              = new GetTechnicianStatusLabelsUseCase();
        getTechnicianSpecialityLabelsUseCase          = new GetTechnicianSpecialityLabelsUseCase();
        getAvailableTechnicianByZoneAndProblemUseCase = new GetAvailableTechnicianByZoneAndProblemUseCase(technicianRepository);

        registerServiceUnitUseCase              = new RegisterServiceUnitUseCase(serviceUnitRepository);
        updateServiceUnitUseCase                = new UpdateServiceUnitUseCase(serviceUnitRepository);
        approveUnitStatusUseCase                = new ApproveUnitStatusUseCase(serviceUnitRepository);
        rejectUnitStatusUseCase                 = new RejectUnitStatusUseCase(serviceUnitRepository);
        getToConfirmServiceUnitsUseCase         = new GetToConfirmServiceUnitsUseCase(serviceUnitRepository);
        getServiceUnitsStatusLabelsUseCase      = new GetServiceUnitsStatusLabelsUseCase();
        getServiceUnitsTypeLabelsUseCase        = new GetServiceUnitsTypeLabelsUseCase();
        getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(serviceUnitRepository);
        getAvailableUnitsByZoneUseCase          = new GetAvailableUnitsByZoneUseCase(serviceUnitRepository);

        registerKitUseCase             = new RegisterKitUseCase(kitRepository);
        updateKitUseCase               = new UpdateKitUseCase(kitRepository);
        getSortedKitsUseCase           = new GetSortedAndFilteredKitsUseCase(kitRepository);
        retireKitFromMaintenanceUseCase= new RetireKitFromMaintenanceUseCase(kitRepository);
        returnKitToServiceUseCase      = new ReturnKitToServiceUseCase(kitRepository);
        getKitTypeLabelsUseCase        = new GetKitTypeLabelsUseCase();
        getKitStatusLabelsUseCase      = new GetKitStatusLabelsUseCase();
        getAvailableKitsByTypeUseCase  = new GetAvailableKitsByTypeUseCase(kitRepository);
        getMaintenanceKitsUseCase      = new GetMaintenanceKitsUseCase(kitRepository);

        registerReportUseCase              = new RegisterReportUseCase(clientRepository, reportRepository);
        assignResourcesReportUseCase       = new AssignResourcesReportUseCase(reportRepository, technicianRepository, serviceUnitRepository, kitRepository);
        undoReportResourcesUseCase         = new UndoReportResourcesUseCase(reportRepository);
        finishReportInFieldUseCase         = new FinishReportInFieldUseCase(reportRepository);
        approveReportActionUseCase         = new ApproveReportActionUseCase(reportRepository, kitRepository);
        rejectReportActionUseCase          = new RejectReportActionUseCase(reportRepository);
        requestReportCancellationUseCase   = new RequestReportCancellationUseCase(reportRepository);
        getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepository);
        getOnGoingReportsUseCase           = new GetOnGoingReportsUseCase(reportRepository);
        getToConfirmReportsUseCase         = new GetToConfirmReportsUseCase(reportRepository);
        getNextPendingReportUseCase        = new GetNextPendingReportUseCase(reportRepository);
        generateDailyCSVUseCase            = new GenerateDailyCSVUseCase(reportRepository);
        getCriticLevelLabelsUseCase        = new GetCriticLevelLabelsUseCase();
        getZoneLabelsUseCase               = new GetZoneLabelsUseCase();

        loginUseCase          = new LoginUseCase(profileRepository);
        saveSystemDataUseCase = new SaveSystemDataUseCase(profileRepository, clientRepository, technicianRepository, serviceUnitRepository, kitRepository, reportRepository);
        loadSystemDataUseCase = new LoadSystemDataUseCase(profileRepository, clientRepository, technicianRepository, serviceUnitRepository, kitRepository, reportRepository);
    }
    
    public void run() {
    	JavaFxLauncher.setController(this);
        
        // Disparamos la interfaz gráfica
        javafx.application.Application.launch(JavaFxLauncher.class);
    }
    
    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadSystemDataUseCase.execute();
        if (clientRepository.getAllClients().getSize() == 0) {
        	injectSeedData();
        }

        loginView = new LoginView();
        loginView.setOnLoginAction(() -> {
            SessionDTO session = loginUseCase.execute(loginView.getUsername(), loginView.getPassword());
            if (session.isSuccess()) {
                initializeSystem(ProfileType.valueOf(session.getRole()));
            } else {
                loginView.showMessage(session.getMessage());
            }
        });
        
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }
    
    public void loadData() {
        loadSystemDataUseCase.execute();
        if (clientRepository.getAllClients().getSize() == 0) {
        	injectSeedData();
        }
    }
    
    public void saveData() { 
    	saveSystemDataUseCase.execute(); 
    }

    private void initializeSystem(ProfileType role) {
        this.currentRole = role;
        roleType = (role == ProfileType.ADMIN) ? "ADMIN" : "OPERATOR";

        mainView        = new MainView(roleType);
        dashboardView   = new DashboardView(roleType, this);
        requestsView    = new RequestsView(roleType);
        unitsView       = new UnitsView(roleType);
        techniciansView = new TechniciansView(roleType);
        kitsView        = new KitsView(roleType);
        clientsView     = new ClientsView(roleType);

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

    public void performUndo() {
    	
        if (currentRole != ProfileType.ADMIN) {
            showNotification(false, "Acceso denegado: Solo Administradores pueden revertir operaciones.");
            return;
        }
        
        ResponseDTO res = undoReportResourcesUseCase.execute();
        
        if (res.isSuccess()) {
            handleResponse(res, this::refreshAllViews);
            return;
        }
        
        res = rejectReportActionUseCase.execute();
        
        if (res.isSuccess()) {
            handleResponse(res, this::refreshAllViews);
            return;
        }
        
        res = rejectUnitStatusUseCase.execute();
        if (res.isSuccess()) {
            handleResponse(res, this::refreshAllViews);
            return;
        }
        
        showNotification(false, "No hay operaciones pendientes que se puedan revertir.");
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

    public SimpleList<String> getZoneLabels()             { return getZoneLabelsUseCase.execute(); }
    public SimpleList<String> getCriticLevelLabels()      { return getCriticLevelLabelsUseCase.execute(); }
    public SimpleList<String> getSpecialityLabels()       { return getTechnicianSpecialityLabelsUseCase.execute(); }
    public SimpleList<String> getTechnicianStatusLabels() { return getTechnicianStatusLabelsUseCase.execute(); }
    public SimpleList<String> getClientTypeLabels()       { return getClientTypeLabelsUseCase.execute(); }
    public SimpleList<String> getUnitTypeLabels()         { return getServiceUnitsTypeLabelsUseCase.execute(); }
    public SimpleList<String> getUnitStatusLabels()       { return getServiceUnitsStatusLabelsUseCase.execute(); }
    public SimpleList<String> getKitTypeLabels()          { return getKitTypeLabelsUseCase.execute(); }
    public SimpleList<String> getKitStatusLabels()        { return getKitStatusLabelsUseCase.execute(); }
    public SimpleList<ServiceUnitDTO> getToConfirmUnits() { return getToConfirmServiceUnitsUseCase.execute(); }
    

    public SimpleList<EntityItem> getSuggestedTechnicians(String zoneName, String specialtyName) {
        SimpleList<EntityItem> list = new SimpleList<>();
        String zNorm = zoneName  != null ? zoneName.replace(" ", "")   : "";
        String sNorm = specialtyName != null ? specialtyName.replace(" ", "") : "";
        SimpleList<TechnicianDTO> dtos = getAvailableTechnicianByZoneAndProblemUseCase.execute(zNorm, sNorm);
        if (dtos.getSize() == 0) dtos = getAvailableTechnicianByZoneAndProblemUseCase.execute(zoneName, specialtyName);
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
        if (dtos.getSize() == 0) dtos = getAvailableUnitsByZoneUseCase.execute(zoneName);
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
        if (dtos.getSize() == 0) dtos = getAvailableKitsByTypeUseCase.execute(problemType);
        SimpleList.Iterator<KitDTO> it = dtos.iterador();
        while (it.hasNext()) {
            KitDTO k = it.Next();
            list.add(new EntityItem(k.getId(), k.getType()));
        }
        return list;
    }
    
    public void processNewRequest(String clientId) {
        Client client = clientRepository.getClientByID(clientId.trim());
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
    
    public ReportDTO getNextPendingReport() { return getNextPendingReportUseCase.execute(); }

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
    	handleResponse(generateDailyCSVUseCase.execute(""), () -> {});
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
        if (dashboardView == null) return;
        int activeUnits = 0, critical = 0, maint = 0, total = 0;
        SimpleList.Iterator<ServiceUnitDTO> uit = getSortedAndFilteredServiceUnitsUseCase.execute().iterador();
        while (uit.hasNext()) { if (!uit.Next().getStatus().equalsIgnoreCase("Mantenimiento")) activeUnits++; }
        maint = getMaintenanceKitsUseCase.execute().getSize();
        SimpleList.Iterator<ReportDTO> rit = getSortedAndFilteredReportsUseCase.execute().iterador();
        while (rit.hasNext()) {
            ReportDTO r = rit.Next(); total++;
            if (r.getPriority() != null && r.getPriority().equalsIgnoreCase("Alta")) critical++;
        }
        dashboardView.updateStatistics(String.valueOf(activeUnits), String.valueOf(critical),
                String.valueOf(maint), String.valueOf(total));
    }

    private void refreshRequestsView() {
        requestsView.clearPanels();

        SimpleList.Iterator<ReportDTO> pendIt = getSortedAndFilteredReportsUseCase.execute().iterador();
        while (pendIt.hasNext()) {
            ReportDTO r = pendIt.Next();
            // ESCUDO: Solo entran los verdaderamente pendientes (canCancel == true)
            if (r.canCancel()) { 
                requestsView.addReportCard(r, "PENDING");
            }
        }

        SimpleList.Iterator<ReportDTO> ongoIt = getOnGoingReportsUseCase.execute().iterador();
        while (ongoIt.hasNext()) {
            ReportDTO r = ongoIt.Next();
            if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
                requestsView.addReportCard(r, "ONGOING");
            }
        }

        SimpleList.Iterator<ReportDTO> confIt = getToConfirmReportsUseCase.execute().iterador();
        while (confIt.hasNext()) {
            ReportDTO r = confIt.Next();
            if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
                requestsView.addReportCard(r, "CONFIRM");
            }
        }
    }

    private void refreshUnitsView() {
        unitsView.clearTable();
        SimpleList.Iterator<ServiceUnitDTO> uIt = getSortedAndFilteredServiceUnitsUseCase.execute().iterador();
        while (uIt.hasNext()) unitsView.addUnit(uIt.Next(), false);
        SimpleList.Iterator<ServiceUnitDTO> pIt = getToConfirmServiceUnitsUseCase.execute().iterador();
        while (pIt.hasNext()) unitsView.addUnit(pIt.Next(), true);
    }

    private void refreshTechniciansView() {
        techniciansView.clearTable();
        SimpleList.Iterator<TechnicianDTO> tIt = getSortedAndDFilteredTechniciansUseCase.execute().iterador();
        while (tIt.hasNext()) {
            TechnicianDTO t = tIt.Next();
            techniciansView.addTechnician(t.getId().toString(), t.getName(),
                    t.getSpecialty(), t.getStatus(), t.getZone(),
                    t.isEditable());
        }
    }

    private void refreshKitsView() {
        kitsView.clearTable();
        SimpleList.Iterator<KitDTO> kIt = getSortedKitsUseCase.execute().iterador();
        while (kIt.hasNext()) kitsView.addKit(kIt.Next());
    }

    private void refreshClientsView() {
        clientsView.clearTable();
        SimpleList.Iterator<ClientDTO> cIt = getSortedClientsUseCase.execute().iterador();
        while (cIt.hasNext()) clientsView.addClient(cIt.Next());
    }

    private void handleResponse(ResponseDTO response, Runnable onSuccess) {
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess() && onSuccess != null) onSuccess.run();
    }

    private void showNotification(boolean success, String message) {
        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateToDashboard()   { mainView.setContent(dashboardView.getView()); }
    public void navigateToRequests()    { mainView.setContent(requestsView.getView()); }
    public void navigateToUnits()       { mainView.setContent(unitsView.getView()); }
    public void navigateToTechnicians() { mainView.setContent(techniciansView.getView()); }
    public void navigateToKits()        { mainView.setContent(kitsView.getView()); }
    public void navigateToClients()     { mainView.setContent(clientsView.getView()); }
    
    public void logout() {
        saveSystemDataUseCase.execute();
        this.currentRole = null;
        startApplication(this.primaryStage);
    }

    private void injectSeedData() {
        System.out.println("--- INYECTANDO DATOS SEMILLA ---");
        registerClientUseCase.ResponseDTO("102030", "Empresa de Transportes VIP",   "Empresarial", "3001234567");
        registerClientUseCase.ResponseDTO("405060", "Aseguradora Solidaria",         "Seguros",     "3109876543");
        registerClientUseCase.ResponseDTO("708090", "Juan Perez",                    "Particular",  "3201112233");

        registerTechnicianUseCase.execute("Carlos Ramirez",  "OperadordeGrua",          "Kennedy");
        registerTechnicianUseCase.execute("Julian Perez",    "MecanicoGeneral",          "Suba");
        registerTechnicianUseCase.execute("Ana Gomez",       "ElectricoAutomotriz",      "Chapinero");
        registerTechnicianUseCase.execute("Luis Martinez",   "CerrajerodeVehiculos",     "Usaquen");

        registerServiceUnitUseCase.execute("Grua",      "Kennedy",   2);
        registerServiceUnitUseCase.execute("Moto",      "Suba",      3);
        registerServiceUnitUseCase.execute("Camioneta", "Chapinero", 2);

        registerKitUseCase.execute("KitdeGrua",        3);
        registerKitUseCase.execute("KitGeneral",       5);
        registerKitUseCase.execute("KitdeElectricidad",2);

        saveSystemDataUseCase.execute();
        System.out.println("--- DATOS SEMILLA GUARDADOS ---");
    }
}