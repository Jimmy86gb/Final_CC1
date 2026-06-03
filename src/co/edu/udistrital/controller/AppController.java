package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador principal de la aplicacion AutoRescate 24/7.
 * Actua como el orquestador central del sistema, gestionando el ciclo de vida
 * de la aplicacion, el enrutamiento entre vistas, la persistencia global de datos 
 * y la comunicacion entre los diferentes sub-controladores modulares.
 * * @author Jimmy86gb
 */
public class AppController {

	private Stage primaryStage;
	private MainView mainView;

	// Repositorios centralizados en memoria
	private final ClientRepository clientRepository;
	private final KitRepository kitRepository;
	private final ReportRepository reportRepository;
	private final ServiceUnitRepository serviceUnitRepository;
	private final TechnicianRepository technicianRepository;
	private final ProfileRepository profileRepository;

	// Casos de uso globales (Persistencia)
	private final SaveSystemDataUseCase saveSystemDataUseCase;
	private final LoadSystemDataUseCase loadSystemDataUseCase;

	// Sub-controladores modulares
	private LoginController loginController;
	private DashboardController dashboardController;
	private RequestsController requestsController;
	private UnitsController unitsController;
	private TechniciansController techniciansController;
	private KitsController kitsController;
	private ClientsController clientsController;

	/**
	 * Constructor de la clase AppController.
	 * Inicializa todos los repositorios en memoria y los casos de uso globales
	 * encargados de la carga y guardado de datos del sistema.
	 */
	public AppController() {
		this.clientRepository = new ClientRepository();
		this.kitRepository = new KitRepository();
		this.reportRepository = new ReportRepository();
		this.serviceUnitRepository = new ServiceUnitRepository();
		this.technicianRepository = new TechnicianRepository();
		this.profileRepository = new ProfileRepository();

		this.saveSystemDataUseCase = new SaveSystemDataUseCase(profileRepository, clientRepository, technicianRepository, serviceUnitRepository, kitRepository, reportRepository);
		this.loadSystemDataUseCase = new LoadSystemDataUseCase(profileRepository, clientRepository, technicianRepository, serviceUnitRepository, kitRepository, reportRepository);
	}

	/**
	 * Punto de entrada principal que enlaza este controlador con el lanzador
	 * e inicializa el entorno grafico de JavaFX.
	 */
	public void run() {
		JavaFxLauncher.setController(this);
		javafx.application.Application.launch(JavaFxLauncher.class);
	}

	/**
	 * Metodo de arranque de la interfaz grafica.
	 * Carga los datos persistidos y despliega la pantalla de inicio de sesion.
	 * * @param primaryStage Escenario principal proporcionado por JavaFX.
	 */
	public void startApplication(Stage primaryStage) {
		this.primaryStage = primaryStage;
		loadData();
		
		loginController = new LoginController(this, profileRepository);
		
		primaryStage.setScene(loginController.getView().getScene());
		primaryStage.show();
		primaryStage.centerOnScreen();
	}

	/**
	 * Carga los datos del sistema desde el archivo de persistencia hacia los repositorios en memoria.
	 */
	public void loadData() { loadSystemDataUseCase.execute(); }

	/**
	 * Guarda el estado actual de los repositorios en memoria hacia el archivo de persistencia.
	 */
	public void saveData() { saveSystemDataUseCase.execute(); }

	/**
	 * Inicializa los modulos principales del sistema una vez el usuario ha iniciado sesion.
	 * Instancia la vista principal y todos los sub-controladores inyectandoles sus dependencias.
	 * * @param role El rol del usuario autenticado (ej. ADMIN, OPERATOR).
	 */
	public void initializeSystem(String role) {
		mainView = new MainView(role);
		
		// Inyeccion de dependencias a los sub-controladores
		dashboardController = new DashboardController(this, role, serviceUnitRepository, kitRepository, reportRepository);
		clientsController = new ClientsController(this, role, clientRepository);
		techniciansController = new TechniciansController(this, role, technicianRepository);
		unitsController = new UnitsController(this, role, serviceUnitRepository);
		kitsController = new KitsController(this, role, kitRepository);
		requestsController = new RequestsController(this, role, reportRepository, clientRepository, technicianRepository, serviceUnitRepository, kitRepository);

		mainView.setNavigationController(this);

		refreshAllViews();
		primaryStage.setScene(mainView.getScene());
		primaryStage.centerOnScreen();
	}

	/**
	 * Maneja de forma centralizada las respuestas emitidas por los casos de uso,
	 * mostrando notificaciones al usuario y ejecutando acciones de refresco si corresponde.
	 * * @param response Objeto ResponseDTO con el resultado de la operacion.
	 * @param onSuccess Accion (Runnable) a ejecutar si la operacion fue exitosa (puede ser null).
	 */
	public void handleResponse(ResponseDTO response, Runnable onSuccess) {
		showNotification(response.isSuccess(), response.getMessage());
		if (response.isSuccess() && onSuccess != null) {
			onSuccess.run();
		}
	}

	/**
	 * Muestra una ventana emergente (Alert) de JavaFX con un mensaje para el usuario.
	 * * @param success Indica si la notificacion es un mensaje de informacion (true) o de error (false).
	 * @param message El texto descriptivo a mostrar en la alerta.
	 */
	private void showNotification(boolean success, String message) {
		Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Orquesta la actualizacion visual sincronizada del sistema.
	 * Solicita a todos los sub-controladores activos que refresquen sus vistas
	 * con los datos mas recientes de los repositorios.
	 */
	public void refreshAllViews() {
		if (clientsController != null) clientsController.refreshView();
		if (kitsController != null) kitsController.refreshView();
		if (unitsController != null) unitsController.refreshView();
		if (techniciansController != null) techniciansController.refreshView();
		if (requestsController != null) requestsController.refreshView();
		if (dashboardController != null) dashboardController.refreshView();
	}

	// --- Enrutamiento de vistas ---

	/** Navega al panel de control (Dashboard). */
	public void navigateToDashboard() { mainView.setContent(dashboardController.getView().getView()); }
	/** Navega al panel de solicitudes y despacho. */
	public void navigateToRequests() { mainView.setContent(requestsController.getView().getView()); }
	/** Navega al gestor de unidades de servicio. */
	public void navigateToUnits() { mainView.setContent(unitsController.getView().getView()); }
	/** Navega al directorio de tecnicos operativos. */
	public void navigateToTechnicians() { mainView.setContent(techniciansController.getView().getView()); }
	/** Navega al inventario de kits de servicio. */
	public void navigateToKits() { mainView.setContent(kitsController.getView().getView()); }
	/** Navega al registro de clientes. */
	public void navigateToClients() { mainView.setContent(clientsController.getView().getView()); }

	/**
	 * Facilita la comunicacion cruzada entre controladores.
	 * Redirige a la vista de clientes y abre inmediatamente el dialogo de registro.
	 * * @param clientId ID o Cedula a precargar en el formulario de registro.
	 */
	public void triggerAddClientDialog(String clientId) {
		navigateToClients();
		clientsController.showAddClientDialog(clientId);
	}

	/**
	 * Cierra la sesion del usuario activo, asegurando el guardado de todos los datos
	 * y redirigiendo al sistema a la pantalla de inicio de sesion.
	 */
	public void logout() {
		saveData();
		startApplication(this.primaryStage);
	}
}