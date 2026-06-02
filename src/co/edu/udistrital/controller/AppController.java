package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador principal que maneja la instanciación de los 43 Casos de Uso
 * y gestiona el enrutamiento de las Vistas, asegurando el patrón MVC.
 */
public class AppController {

    private final Stage primaryStage;
    private String currentUserRole;

    // Repositorios en Memoria (Simulando la BD)
    private final ProfileRepository profileRepo = new ProfileRepository();
    private final ClientRepository clientRepo = new ClientRepository();
    private final TechnicianRepository techRepo = new TechnicianRepository();
    private final ServiceUnitRepository unitRepo = new ServiceUnitRepository();
    private final KitRepository kitRepo = new KitRepository();
    private final ReportRepository reportRepo = new ReportRepository();

    // ==========================================
    // INSTANCIACIÓN DE LOS 43 CASOS DE USO
    // ==========================================
    public final LoginUseCase loginUseCase = new LoginUseCase(profileRepo);
    public final GenerateDailyCSVUseCase generateDailyCSVUseCase = new GenerateDailyCSVUseCase(reportRepo);
    
    // Zonas y Críticos
    public final GetZoneLabelsUseCase getZoneLabelsUseCase = new GetZoneLabelsUseCase();
    public final GetCriticLevelLabelsUseCase getCriticLevelLabelsUseCase = new GetCriticLevelLabelsUseCase();

    // Clientes
    public final GetSortedAndFilteredClientsUseCase getClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepo);
    public final RegisterClientUseCase registerClientUseCase = new RegisterClientUseCase(clientRepo);
    public final UpdateClientUseCase updateClientUseCase = new UpdateClientUseCase(clientRepo);
    public final GetClientTypeLabelsUseCase getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();

    // Técnicos
    public final GetSortedAndDFilteredTechniciansUseCase getTechniciansUseCase = new GetSortedAndDFilteredTechniciansUseCase(techRepo);
    public final RegisterTechnicianUseCase registerTechnicianUseCase = new RegisterTechnicianUseCase(techRepo);
    public final UpdateTechnicianUseCase updateTechnicianUseCase = new UpdateTechnicianUseCase(techRepo);
    public final GetTechnicianSpecialityLabelsUseCase getTechSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
    public final GetTechnicianStatusLabelsUseCase getTechStatusLabelsUseCase = new GetTechnicianStatusLabelsUseCase();
    public final GetAvailableTechnicianByZoneAndProblemUseCase getAvailTechUseCase = new GetAvailableTechnicianByZoneAndProblemUseCase(techRepo);

    // Unidades
    public final GetSortedAndFilteredServiceUnitsUseCase getUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(unitRepo);
    public final RegisterServiceUnitUseCase registerUnitUseCase = new RegisterServiceUnitUseCase(unitRepo);
    public final UpdateServiceUnitUseCase updateUnitUseCase = new UpdateServiceUnitUseCase(unitRepo);
    public final GetServiceUnitsTypeLabelsUseCase getUnitTypeLabelsUseCase = new GetServiceUnitsTypeLabelsUseCase();
    public final GetServiceUnitsStatusLabelsUseCase getUnitStatusLabelsUseCase = new GetServiceUnitsStatusLabelsUseCase();
    public final GetAvailableUnitsByZoneUseCase getAvailUnitsUseCase = new GetAvailableUnitsByZoneUseCase(unitRepo);
    public final GetToConfirmServiceUnitsUseCase getToConfirmUnitsUseCase = new GetToConfirmServiceUnitsUseCase(unitRepo);
    public final ApproveUnitStatusUseCase approveUnitStatusUseCase = new ApproveUnitStatusUseCase(unitRepo);
    public final RejectUnitStatusUseCase rejectUnitStatusUseCase = new RejectUnitStatusUseCase(unitRepo);

    // Kits
    public final GetSortedAndFilteredKitsUseCase getKitsUseCase = new GetSortedAndFilteredKitsUseCase(kitRepo);
    public final RegisterKitUseCase registerKitUseCase = new RegisterKitUseCase(kitRepo);
    public final UpdateKitUseCase updateKitUseCase = new UpdateKitUseCase(kitRepo);
    public final GetKitTypeLabelsUseCase getKitTypeLabelsUseCase = new GetKitTypeLabelsUseCase();
    public final GetKitStatusLabelsUseCase getKitStatusLabelsUseCase = new GetKitStatusLabelsUseCase();
    public final GetMaintenanceKitsUseCase getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepo);
    public final RetireKitFromMaintenanceUseCase retireKitUseCase = new RetireKitFromMaintenanceUseCase(kitRepo);
    public final ReturnKitToServiceUseCase returnKitUseCase = new ReturnKitToServiceUseCase(kitRepo);
    public final GetAvailableKitsByTypeUseCase getAvailKitsUseCase = new GetAvailableKitsByTypeUseCase(kitRepo);

    // Reportes (Siniestros)
    public final RegisterReportUseCase registerReportUseCase = new RegisterReportUseCase(clientRepo, reportRepo);
    public final GetSortedAndFilteredReportsUseCase getReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepo);
    public final RequestReportCancellationUseCase requestCancelUseCase = new RequestReportCancellationUseCase(reportRepo);
    public final GetNextPendingReportUseCase getNextPendingReportUseCase = new GetNextPendingReportUseCase(reportRepo);
    public final AssignResourcesReportUseCase assignResourcesUseCase = new AssignResourcesReportUseCase(reportRepo, techRepo, unitRepo, kitRepo);
    public final GetOnGoingReportsUseCase getOnGoingReportsUseCase = new GetOnGoingReportsUseCase(reportRepo);
    public final UndoReportResourcesUseCase undoReportUseCase = new UndoReportResourcesUseCase(reportRepo);
    public final FinishReportInFieldUseCase finishReportUseCase = new FinishReportInFieldUseCase(reportRepo);
    public final GetToConfirmReportsUseCase getToConfirmReportsUseCase = new GetToConfirmReportsUseCase(reportRepo);
    public final ApproveReportActionUseCase approveReportUseCase = new ApproveReportActionUseCase(reportRepo, kitRepo);
    public final RejectReportActionUseCase rejectReportUseCase = new RejectReportActionUseCase(reportRepo);


    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void startApplication() {
        showLogin();
    }

    public void showLogin() {
        LoginView loginView = new LoginView(this);
        primaryStage.setScene(new Scene(loginView.getView(), 400, 350));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void loginSuccess(String role) {
        this.currentUserRole = role;
        DashboardView dashboard = new DashboardView(this, role);
        primaryStage.setScene(new Scene(dashboard.getView(), 1100, 700));
        primaryStage.centerOnScreen();
    }
    
    /**
     * Genera datos de prueba (Mock Data) al arrancar el sistema para facilitar
     * la evaluación y demostración de las funcionalidades de AutoRescate 24/7.
     */
    /**
     * Genera datos de prueba y avisa en consola si algún Factory rechaza los textos.
     */
    public void seedMockData() {
        System.out.println("--- INICIANDO CARGA DE DATOS DE PRUEBA ---");
        ResponseDTO res;

        // 1. Registrar Clientes
        res = registerClientUseCase.ResponseDTO("102030", "Juan Perez", "Particular", "3001234567");
        if(!res.isSuccess()) System.err.println("Error en Cliente 1: " + res.getMessage());

        res = registerClientUseCase.ResponseDTO("900123", "Seguros Alfa", "Seguros", "contacto@alfa.com");
        if(!res.isSuccess()) System.err.println("Error en Cliente 2: " + res.getMessage());

        // 2. Registrar Técnicos (Usando localidades válidas de tu ZoneFactory)
        res = registerTechnicianUseCase.execute("Carlos Rodriguez", "Mecanico General", "Suba");
        if(!res.isSuccess()) System.err.println("Error en Tecnico 1: " + res.getMessage());

        res = registerTechnicianUseCase.execute("Luis Martinez", "Operador de Grua", "Usme");
        if(!res.isSuccess()) System.err.println("Error en Tecnico 2: " + res.getMessage());

        res = registerTechnicianUseCase.execute("Ana Gomez", "Electrico Automotriz", "Kennedy");
        if(!res.isSuccess()) System.err.println("Error en Tecnico 3: " + res.getMessage());

        res = registerTechnicianUseCase.execute("Pedro Sanchez", "Operario Montallantas", "Bosa");
        if(!res.isSuccess()) System.err.println("Error en Tecnico 4: " + res.getMessage());

        // 3. Registrar Unidades de Servicio
        // Nota: Asegúrate de que "Grua", "Moto", etc., coincidan con tu UnitFactory
        res = registerUnitUseCase.execute("Grua", "Usme", 2);
        if(!res.isSuccess()) System.err.println("Error en Unidad 1: " + res.getMessage());

        res = registerUnitUseCase.execute("Moto", "Kennedy", 4);
        if(!res.isSuccess()) System.err.println("Error en Unidad 2: " + res.getMessage());

        res = registerUnitUseCase.execute("Camioneta", "Suba", 2);
        if(!res.isSuccess()) System.err.println("Error en Unidad 3: " + res.getMessage());

        // 4. Registrar Kits 
        // Nota: Si tu KitFactory usa nombres diferentes, la consola te lo dirá.
        res = registerKitUseCase.execute("Kit General", 5);
        if(!res.isSuccess()) System.err.println("Error en Kit 1: " + res.getMessage());

        res = registerKitUseCase.execute("Kit de Grua", 3);
        if(!res.isSuccess()) System.err.println("Error en Kit 2: " + res.getMessage());

        res = registerKitUseCase.execute("Kit De Electricidad", 3);
        if(!res.isSuccess()) System.err.println("Error en Kit 3: " + res.getMessage());

        // 5. Registrar Siniestros
        // Nota: He cambiado "Media" por "Alta" o "Baja" por si el CriticFactory no soporta entrada de "Media" directa.
        res = registerReportUseCase.execute("102030", "Motor recalentado en la via principal", "Mecanico General", "Alta", "Suba");
        if(!res.isSuccess()) System.err.println("Error Siniestro 1: " + res.getMessage());

        res = registerReportUseCase.execute("900123", "Choque multiple, requiere traslado", "Operador de Grua", "Baja", "Usme");
        if(!res.isSuccess()) System.err.println("Error Siniestro 2: " + res.getMessage());

        res = registerReportUseCase.execute("102030", "Llanta pinchada sin repuesto", "Operario Montallantas", "Baja", "Bosa");
        if(!res.isSuccess()) System.err.println("Error Siniestro 3: " + res.getMessage());

        System.out.println("--- FIN DE CARGA DE DATOS ---");
    }
}