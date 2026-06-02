package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.entities.*;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.*;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.enums.UnitStatus;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador principal de la aplicacion. Aca es donde se conecta todo el MVC 
 * de este proyecto final para que la vista y la logica se hablen sin enredarse.
 * Literalmente es el cerebro que mueve las fichas del AutoRescate.
 * 
 * @author Jimmy86gb
 */
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
    private GetSortedAndFilteredKitsUseCase getSortedKitsUseCase;
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
    private GenerateDailyCSVUseCase generateDailyCSVUseCase;
 // Agrégalo junto a los otros UseCases
    private GetNextPendingReportUseCase getNextPendingReportUseCase;
    
    private ProfileType currentRole;
    private Stage primaryStage;

    /**
     * Constructor del controlador.
     * Instanciar todo en orden para que no salte un NullPointerException 
     * de la nada. Primero creamos las memorias (repositorios) y luego se las pasamos 
     * a los casos de uso para que trabajen con datos reales.
     */
    public AppController() {
        // Asegurar la memoria
        this.clientRepository = new ClientRepository();
        this.kitRepository = new KitRepository();
        this.reportRepository = new ReportRepository();
        this.serviceUnitRepository = new ServiceUnitRepository();
        this.technicianRepository = new TechnicianRepository();
        this.profileRepository = new ProfileRepository();

        // Pasar esa memoria a la logica
        this.registerClientUseCase = new RegisterClientUseCase(this.clientRepository);
        this.getSortedClientsUseCase = new GetSortedAndFilteredClientsUseCase(this.clientRepository);
        this.registerKitUseCase = new RegisterKitUseCase(this.kitRepository);
        this.getSortedKitsUseCase = new GetSortedAndFilteredKitsUseCase(this.kitRepository);
        this.registerServiceUnitUseCase = new RegisterServiceUnitUseCase(this.serviceUnitRepository);
        this.registerTechnicianUseCase = new RegisterTechnicianUseCase(this.technicianRepository);
        this.loginUseCase = new LoginUseCase(this.profileRepository);
    
        this.registerReportUseCase = new RegisterReportUseCase(this.clientRepository, this.reportRepository);
        this.finishReportInFieldUseCase = new FinishReportInFieldUseCase(this.reportRepository);
        this.approveReportActionUseCase = new ApproveReportActionUseCase(this.reportRepository, this.kitRepository);
        this.rejectReportActionUseCase = new RejectReportActionUseCase(this.reportRepository);
        this.requestReportCancellationUseCase = new RequestReportCancellationUseCase(this.reportRepository);
        this.getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(this.reportRepository);
        this.getOnGoingReportsUseCase = new GetOnGoingReportsUseCase(this.reportRepository);
        this.getToConfirmReportsUseCase = new GetToConfirmReportsUseCase(this.reportRepository);
    
        this.getNextPendingReportUseCase = new GetNextPendingReportUseCase(this.reportRepository);
    }

    /**
     * Metodo que arranca toda la aplicacion.
     * Muestra la pantalla de login para ver si el que entra 
     * es admin u operador y revisa que las credenciales cuadren.
     * 
     * @param primaryStage El escenario principal de JavaFX donde pinta todo
     */
    public void startApplication(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginView = new LoginView();

        // Decirs al boton de login que hacer cuando le den clic
        loginView.setOnLoginAction(() -> {
            String user = loginView.getUsername();
            String pass = loginView.getPassword();

            SessionDTO session = loginUseCase.execute(user, pass);
            if (!session.isSuccess()) {
                loginView.showMessage(session.getMessage());
                return;
            }

            try {
                // Si sale bien saca el rol y arma el sistema
                ProfileType role = ProfileType.valueOf(session.getRole());
                initializeSystem(role);
            } catch (IllegalArgumentException ex) {
                loginView.showMessage("Rol desconocido en el sistema.");
            }
        });

        primaryStage.setTitle("AutoRescate 24/7 - Iniciar Sesion");
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }

    /**
     * Arma todas las ventanas del sistema despues de que alguien se loguea.
     * Configura los permisos segun el rol y carga los datos  en las tablas.
     * 
     * @param role El perfil del usuario que acaba de entrar (Admin u Operador)
     */
    private void initializeSystem(ProfileType role) {
        this.currentRole = role;

        // Crea las vistas pasandole el rol para que oculten o muestren botones
        mainView = new MainView(role);
        dashboardView = new DashboardView(role);
        requestsView = new RequestsView(role);
        unitsView = new UnitsView(role);
        techniciansView = new TechniciansView(role);
        kitsView = new KitsView(role);
        clientsView = new ClientsView(role);

        // Si la memoria de clientes está vacía, inyectamos todos los datos base
        if (clientRepository.getAllClients().getSize() == 0) {
            seedData();
        }
        // Conecta como el controlador de todas esas vistas
        mainView.setNavigationController(this);
        clientsView.setController(this);
        unitsView.setController(this);
        kitsView.setController(this);
        techniciansView.setController(this);
        requestsView.setController(this);

        // Llena las tablas con los datos que hayan guardados
        refreshAllViews();

        primaryStage.setScene(mainView.getScene());
        mainView.getScene().getWindow().centerOnScreen();
    }

    /**
     * Procesa cuando se quiere crear una nueva solicitud de emergencia.
     * Revisa si la cedula existe. Si no, manda al usuario a crear el cliente primero.
     * 
     * @param clientId La cedula o ID que escribio el operador
     */
    public void processNewRequest(String clientId) {
    	String cleanId = clientId.trim();
        Client client = clientRepository.getClientByID(cleanId);
        
        if (client == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("Cliente No Encontrado");
            alert.setContentText("Sera redirigido para registrar este nuevo cliente en el sistema.");
            alert.showAndWait();
            navigateToClients();
            clientsView.showAddClientDialog(clientId);
        } else {
            requestsView.showCreateReportDialog(client);
        }
    }
    
    public void submitReportCreation(String clientId, String description, String type, String priority, String zone) {
        // Limpiamos los espacios de los textos que vienen de la vista (ej: "Alta " -> "Alta", "Mecanico General" -> "MecanicoGeneral")
        ResponseDTO response = registerReportUseCase.execute(
            clientId, 
            description, 
            type.replace(" ", ""), 
            priority.replace(" ", ""), 
            zone.replace(" ", "")
        );
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
    }

    public void assignReportResources(String techId, String unitId, String kitId) {
        ResponseDTO response = assignResourcesReportUseCase.execute(techId, unitId, kitId);
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
        refreshTechniciansView();
        refreshUnitsView();
        refreshKitsView();
    }

    public void cancelReport(String ticketId) {
        ResponseDTO response = requestReportCancellationUseCase.execute(ticketId);
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
    }

    public void undoReport() {
        ResponseDTO response = undoReportResourcesUseCase.execute();
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
        refreshTechniciansView();
        refreshUnitsView();
        refreshKitsView();
    }

    public void finishReport(String ticketId) {
        ResponseDTO response = finishReportInFieldUseCase.execute(ticketId);
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
    }

    public void approveReport() {
        ResponseDTO response = approveReportActionUseCase.execute();
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
        refreshTechniciansView();
        refreshUnitsView();
        refreshKitsView();
    }

    public void rejectReport() {
        ResponseDTO response = rejectReportActionUseCase.execute();
        showNotification(response.isSuccess(), response.getMessage());
        refreshRequestsView();
    }

    public void exportDailyReport() {
        ResponseDTO response = generateDailyCSVUseCase.execute("hola");
        showNotification(response.isSuccess(), response.getMessage());
    }
    
 // --- MÉTODOS PARA CARGAR DATOS EN LOS COMBOBOX ---

    public SimpleList<EntityItem> getAvailableTechniciansForUI() {
        SimpleList<EntityItem> list = new SimpleList<>();
        var iterator = technicianRepository.getAllTechnicians().iterador();
        while (iterator.hasNext()) {
            Technician t = iterator.Next();
            if (t.getStatus() == co.edu.udistrital.model.enums.TechnicianStatus.AVAILABLE) {
            	String displayString = t.getName() + " (" + t.getSpecialty().getDisplayName() + ")";
            	list.add(new EntityItem(t.getId().toString(), displayString));
            }
        }
        return list;
    }

    public SimpleList<EntityItem> getAvailableUnitsForUI() {
        SimpleList<EntityItem> list = new SimpleList<>();
        var iterator = serviceUnitRepository.getAllUnits().iterador();
        while (iterator.hasNext()) {
            ServiceUnit u = iterator.Next();
            if (u.getStatus() == co.edu.udistrital.model.enums.UnitStatus.AVAILABLE) {
                list.add(new EntityItem(u.getId().toString(), u.getType().getDisplayName() + " (" + u.getZone().getDisplayName() + ")"));
            }
        }
        return list;
    }

    public SimpleList<EntityItem> getAvailableKitsForUI() {
        SimpleList<EntityItem> list = new SimpleList<>();
        var iterator = kitRepository.getAllKits().iterador();
        while (iterator.hasNext()) {
            Kit k = iterator.Next();
            if (k.getStatus() == co.edu.udistrital.model.enums.UnitStatus.AVAILABLE) {
                list.add(new EntityItem(k.getId().toString(), k.getType().getDisplayName()));
            }
        }
        return list;
    }
    
 // 1. Filtra técnicos por ZONA y ESPECIALIDAD
    public SimpleList<EntityItem> getSuggestedTechnicians(String zoneName, String specialtyName) {
        SimpleList<EntityItem> list = new SimpleList<>();
        // Asumo que tienes factories para convertir texto a Enums
        var zone = new co.edu.udistrital.model.enums.ZoneFactory().generateOperationZone(zoneName);
        var spec = new co.edu.udistrital.model.enums.TechnicianFactory().generaTechnicianSpecialty(specialtyName);
        
        var it = technicianRepository.getAvailableTechnicians(zone, spec).iterador();
        while (it.hasNext()) {
            Technician t = it.Next();
            list.add(new EntityItem(t.getId().toString(), t.getName()));
        }
        return list;
    }

    // 2. Filtra unidades por ZONA
    public SimpleList<EntityItem> getSuggestedUnits(String zoneName) {
        SimpleList<EntityItem> list = new SimpleList<>();
        var zone = new co.edu.udistrital.model.enums.ZoneFactory().generateOperationZone(zoneName);
        
        var it = serviceUnitRepository.getAvailableUnitsByZone(zone).iterador();
        while (it.hasNext()) {
            ServiceUnit u = it.Next();
            list.add(new EntityItem(u.getId().toString(), u.getType().getDisplayName()));
        }
        return list;
    }

    /**
     * Guarda un cliente nuevo en el sistema y refresca la tabla.
     * 
     * @param id La cedula del cliente
     * @param name El nombre completo
     * @param type Si es particular, empresa, etc
     * @param contact Numero de celular o telefono
     */
    public void registerClient(String id, String name, String type, String contact) {
        ResponseDTO response = registerClientUseCase.ResponseDTO(id, name, type.replace(" ", ""), contact);
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess()) {
        	refreshClientsView();
        }
    }

    /**
     * Mete una nueva unidad de servicio (como una grua o moto) al parque automotor.
     * 
     * @param type El tipo de vehiculo
     * @param zone La zona de Bogota donde opera
     * @param quantity Cuantas unidades de estas vamos a registrar
     */
    public void registerUnit(String type, String zone, int quantity) {
        ResponseDTO response = registerServiceUnitUseCase.execute(type.replace(" ", ""), zone.replace(" ", ""), quantity);
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess()) {
        	refreshUnitsView();
        }
    }

    /**
     * Añade un nuevo lote de kits al inventario para que los tecnicos los usen.
     * 
     * @param type El tipo de kit (ej. Cerrajeria)
     * @param quantity La cantidad de kits que llegaron
     */
    public void registerKit(String type, int quantity) {
        ResponseDTO response = registerKitUseCase.execute(type.replace(" ", ""), quantity);
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess()) {
        	refreshKitsView();
        }
    }

    /**
     * Registra a un nuevo tecnico en el sistema.
     * 
     * @param name El nombre del tecnico
     * @param specialty En que es experto (mecanica, grua, etc)
     * @param zone La zona que le toca cubrir
     */
    public void registerTechnician(String name, String specialty, String zone) {
        ResponseDTO response = registerTechnicianUseCase.execute(name, specialty.replace(" ", ""), zone.replace(" ", ""));
        showNotification(response.isSuccess(), response.getMessage());
        if (response.isSuccess()) {
        	refreshTechniciansView();
        }
    }
    
    private void refreshAllViews() {
        refreshClientsView();
        refreshKitsView();
        refreshUnitsView();
        refreshTechniciansView();
        refreshRequestsView();
        refreshDashboardView();
    }

    private void refreshRequestsView() {
        requestsView.clearPanels();
        
        // 1. Mostrar Pendientes
        SimpleList.Iterator<ReportDTO> iterPending = getSortedAndFilteredReportsUseCase.execute().iterador();
        while (iterPending.hasNext()) {
            ReportDTO rep = iterPending.Next();
            if (rep.canCancel()) { 
                requestsView.addReportCard(rep, "PENDING");
            }
        }
        // 2. Mostrar En Progreso
        SimpleList.Iterator<ReportDTO> iterOngoing = getOnGoingReportsUseCase.execute().iterador();
        while (iterOngoing.hasNext()) {
            requestsView.addReportCard(iterOngoing.Next(), "ONGOING");
        }
        // 3. Mostrar Por Confirmar
        SimpleList.Iterator<ReportDTO> iterConfirm = getToConfirmReportsUseCase.execute().iterador();
        while (iterConfirm.hasNext()) {
            requestsView.addReportCard(iterConfirm.Next(), "CONFIRM");
        }
    }
    

    /**
     * Limpia la tabla de clientes y la vuelve a dibujar con lo que hay en memoria.
     * Aca usamos el iterador propio para cumplir con las reglas del proyecto.
     */
    private void refreshClientsView() {
        clientsView.clearTable();
        SimpleList<ClientDTO> list = getSortedClientsUseCase.execute();
        SimpleList.Iterator<ClientDTO> iterator = list.iterador();
        while (iterator.hasNext()) {
            clientsView.addClient(iterator.Next());
        }
    }

    /**
     * Limpia y actualiza la tabla de los kits.
     */
    private void refreshKitsView() {
        kitsView.clearTable();
        SimpleList<KitDTO> list = getSortedKitsUseCase.execute();
        SimpleList.Iterator<KitDTO> iterator = list.iterador();
        while (iterator.hasNext()) {
            kitsView.addKit(iterator.Next());
        }
    }

    /**
     * Actualiza la vista de las unidades de servicio. 
     * Ademas revisa si la unidad esta disponible pa saber si habilita los botones o no.
     */
    private void refreshUnitsView() {
        unitsView.clearTable();
        SimpleList<ServiceUnit> list = serviceUnitRepository.getAllUnits();
        SimpleList.Iterator<ServiceUnit> iterator = list.iterador();
        while (iterator.hasNext()) {
            ServiceUnit unit = iterator.Next();
            boolean canEdit = unit.getStatus().getDisplayName().equals("Disponible");
            unitsView.addUnit(unit.getId().toString(), unit.getType().getDisplayName(),
                    unit.getStatus().getDisplayName(), unit.getZone().getDisplayName(), canEdit);
        }
    }

    /**
     * Refresca la tabla de los tecnicos viendo si estan ocupados o libres.
     */
    private void refreshTechniciansView() {
        techniciansView.clearTable();
        SimpleList<Technician> list = technicianRepository.getAllTechnicians();
        SimpleList.Iterator<Technician> iterator = list.iterador();
        while (iterator.hasNext()) {
            Technician tech = iterator.Next();
            boolean canEdit = tech.getStatus().getDisplayName().equals("Disponible");
            techniciansView.addTechnician(tech.getId().toString(), tech.getName(),
                    tech.getSpecialty().getDisplayName(), tech.getStatus().getDisplayName(),
                    tech.getZone().getDisplayName(), canEdit);
        }
    }
    
    /**
     * Calcula las estadisticas actuales del sistema iterando sobre las estructuras
     * de datos y envia los resultados a la vista del Dashboard.
     */
    private void refreshDashboardView() {
        // 1. Contar Unidades Activas (Disponibles o Asignadas)
        int activeUnits = 0;
        SimpleList.Iterator<ServiceUnit> unitIt = serviceUnitRepository.getAllUnits().iterador();
        while (unitIt.hasNext()) {
            UnitStatus status = unitIt.Next().getStatus();
            if (status == co.edu.udistrital.model.enums.UnitStatus.AVAILABLE || 
                status == co.edu.udistrital.model.enums.UnitStatus.ASSIGNED) {
                activeUnits++;
            }
        }

        // 2. Contar Casos Criticos (Prioridad ALTA y que esten Pendientes o En Progreso)
        int criticalCases = 0;
        SimpleList.Iterator<Report> reportIt = reportRepository.getAllReports().iterador();
        while (reportIt.hasNext()) {
            Report r = reportIt.Next();
            if (r.getPriority() == co.edu.udistrital.model.enums.CriticLevel.HIGH && 
               (r.getStatus() == co.edu.udistrital.model.enums.ReportStatus.PENDING || 
                r.getStatus() == co.edu.udistrital.model.enums.ReportStatus.ON_GOING)) {
                criticalCases++;
            }
        }

        // 3. Contar Elementos en Mantenimiento (Kits en la pila + Unidades dañadas)
        int maintenanceCount = 0;
        maintenanceCount += kitRepository.getMaintenanceKits().getSize();
        
        SimpleList.Iterator<ServiceUnit> maintUnitIt = serviceUnitRepository.getAllUnits().iterador();
        while(maintUnitIt.hasNext()) {
            if (maintUnitIt.Next().getStatus() == co.edu.udistrital.model.enums.UnitStatus.MAINTENANCE) {
                maintenanceCount++;
            }
        }

        // Se envian los datos calculados a la vista
        if (dashboardView != null) {
            dashboardView.updateStatistics(
                String.valueOf(activeUnits), 
                String.valueOf(criticalCases), 
                String.valueOf(maintenanceCount)
            );
        }
    }
    
    /**
     * Puente entre la Vista y el Caso de Uso para obtener 
     * el reporte más urgente de la cola.
     */
    public ReportDTO getNextPendingReport() {
        return getNextPendingReportUseCase.execute();
    }

    /**
     * Saca una ventanita de alerta pa avisarle al usuario que paso con lo que intento hacer.
     * 
     * @param success Si salio bien mandamos info, si no, mandamos un error
     * @param message El texto chiquito que explica que paso
     */
    private void showNotification(boolean success, String message) {
        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metodos pa cambiar de pantalla en el menu
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

    /**
     * Borra el rol actual y devuelve todo a la pantalla de login.
     */
    public void logout() {
        this.currentRole = null;
        startApplication(this.primaryStage);
    }
    
    /**
     * Método encargado de poblar el sistema con datos de prueba (Seed Data)
     * para facilitar la sustentación y evitar registrar todo manualmente.
     */
    private void seedData() {
        System.out.println("--- INICIANDO CARGA DE DATOS SEMILLA ---");
        
        // 1. Crear Clientes
        System.out.println("Cliente 1: " + registerClientUseCase.ResponseDTO("101010", "Transportes Rapidos SAS", "Empresarial", "3001112233").getMessage());
        System.out.println("Cliente 2: " + registerClientUseCase.ResponseDTO("202020", "Seguros TodoRiesgo", "Seguros", "3104445566").getMessage());
        
        // 2. Crear Técnicos
        System.out.println("Tecnico 1: " + registerTechnicianUseCase.execute("Carlos Ramirez", "OperadordeGrua", "Kennedy").getMessage());
        System.out.println("Tecnico 2: " + registerTechnicianUseCase.execute("Julian Perez", "MecanicoGeneral", "Suba").getMessage());

        // 3. Crear Unidades de Servicio
        System.out.println("Unidad 1: " + registerServiceUnitUseCase.execute("Grua", "Kennedy", 2).getMessage());
        System.out.println("Unidad 2: " + registerServiceUnitUseCase.execute("Moto", "Suba", 3).getMessage());

        // 4. Crear Kits
        System.out.println("Kit 1: " + registerKitUseCase.execute("KitdeGrua", 2).getMessage());
        System.out.println("Kit 2: " + registerKitUseCase.execute("KitGeneral", 4).getMessage());

        // 5. Crear Solicitudes
        // CORRECCIÓN: Mandamos los textos con espacios exactamente igual a como los genera el ComboBox de la interfaz.
     // 5. Crear Solicitudes
        // CORRECCIÓN: Quitamos los espacios para que el Factory los reconozca
        System.out.println("Siniestro 1: " + registerReportUseCase.execute("101010", "Camión varado por motor", "Mecanico General", "Alta", "Suba").getMessage());
        System.out.println("Siniestro 2: " + registerReportUseCase.execute("202020", "Estrellada en la principal", "Operador de Grua", "Alta", "Kennedy").getMessage());
        System.out.println("Siniestro 3: " + registerReportUseCase.execute("101010", "Llanta pinchada sin repuesto", "Operario Montallantas", "Media", "Suba").getMessage());
        
        System.out.println("--- FIN CARGA DE DATOS ---");
    }
}