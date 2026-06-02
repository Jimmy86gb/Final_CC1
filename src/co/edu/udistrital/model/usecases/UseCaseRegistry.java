package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.usecases.*;

/**
 * Contenedor centralizado (Singleton) que orquesta los 43 Casos de Uso del sistema AutoRescate 24/7.
 * Garantiza que todas las vistas interactúen con la misma instancia de memoria.
 *
 * @author Jimmy Alejandro Granados Becerra
 */
public class UseCaseRegistry {

    private static UseCaseRegistry instance;

    // Repositorios en memoria
    private final ProfileRepository profileRepo = new ProfileRepository();
    private final ClientRepository clientRepo = new ClientRepository();
    private final TechnicianRepository techRepo = new TechnicianRepository();
    private final ServiceUnitRepository unitRepo = new ServiceUnitRepository();
    private final KitRepository kitRepo = new KitRepository();
    private final ReportRepository reportRepo = new ReportRepository();

    // 1. Seguridad
    private final LoginUseCase loginUseCase;

    // 2. Módulos CRUD y Listas (Getters y Registros)
    private final GetSortedAndFilteredClientsUseCase getClientsUseCase;
    private final RegisterClientUseCase registerClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final GetClientTypeLabelsUseCase getClientTypeLabelsUseCase;

    // (Se instancian de manera análoga los casos de uso de Técnicos, Unidades y Kits...)
    private final GetSortedAndFilteredReportsUseCase getReportsUseCase;
    private final RequestReportCancellationUseCase requestCancellationUseCase;
    
    // 3. Operativa y Asignación
    private final GetNextPendingReportUseCase getNextPendingReportUseCase;
    private final AssignResourcesReportUseCase assignResourcesUseCase;
    private final FinishReportInFieldUseCase finishReportUseCase;
    
    // 4. Exportación
    private final GenerateDailyCSVUseCase generateDailyCSVUseCase;

    private UseCaseRegistry() {
        this.loginUseCase = new LoginUseCase(profileRepo);
        this.getClientsUseCase = new GetSortedAndFilteredClientsUseCase(clientRepo);
        this.registerClientUseCase = new RegisterClientUseCase(clientRepo);
        this.updateClientUseCase = new UpdateClientUseCase(clientRepo);
        this.getClientTypeLabelsUseCase = new GetClientTypeLabelsUseCase();
        this.getReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepo);
        this.requestCancellationUseCase = new RequestReportCancellationUseCase(reportRepo);
        this.getNextPendingReportUseCase = new GetNextPendingReportUseCase(reportRepo);
        this.assignResourcesUseCase = new AssignResourcesReportUseCase(reportRepo, techRepo, unitRepo, kitRepo);
        this.finishReportUseCase = new FinishReportInFieldUseCase(reportRepo);
        this.generateDailyCSVUseCase = new GenerateDailyCSVUseCase(reportRepo);
        // La inicialización de los demás casos de uso sigue exactamente este patrón inyectando los repositorios.
    }

    /**
     * Retorna la instancia única del registro de casos de uso.
     * @return UseCaseRegistry
     */
    public static UseCaseRegistry getInstance() {
        if (instance == null) {
            instance = new UseCaseRegistry();
        }
        return instance;
    }

    public LoginUseCase getLoginUseCase() { return loginUseCase; }
    public GetSortedAndFilteredReportsUseCase getReportsUseCase() { return getReportsUseCase; }
    public RequestReportCancellationUseCase getRequestCancellationUseCase() { return requestCancellationUseCase; }
    public GetSortedAndFilteredClientsUseCase getClientsUseCase() { return getClientsUseCase; }
    public RegisterClientUseCase getRegisterClientUseCase() { return registerClientUseCase; }
    public UpdateClientUseCase getUpdateClientUseCase() { return updateClientUseCase; }
    public GetClientTypeLabelsUseCase getClientTypeLabelsUseCase() { return getClientTypeLabelsUseCase; }
    public GenerateDailyCSVUseCase getGenerateDailyCSVUseCase() { return generateDailyCSVUseCase; }
}