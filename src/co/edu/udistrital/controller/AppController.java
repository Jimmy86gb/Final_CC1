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

        // Conecta como el controlador de todas esas vistas
        mainView.setNavigationController(this);
        clientsView.setController(this);
        unitsView.setController(this);
        kitsView.setController(this);
        techniciansView.setController(this);
        requestsView.setController(this);

        // Llena las tablas con los datos que hayan guardados
        refreshClientsView();
        refreshKitsView();
        refreshUnitsView();
        refreshTechniciansView();

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
}