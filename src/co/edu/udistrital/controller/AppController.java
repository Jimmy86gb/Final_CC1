package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class AppController {

	private Stage primaryStage;
	private MainView mainView;

	
	private final ClientRepository clientRepository;
	private final KitRepository kitRepository;
	private final ReportRepository reportRepository;
	private final ServiceUnitRepository serviceUnitRepository;
	private final TechnicianRepository technicianRepository;
	private final ProfileRepository profileRepository;

	
	private final SaveSystemDataUseCase saveSystemDataUseCase;
	private final LoadSystemDataUseCase loadSystemDataUseCase;

	
	private LoginController loginController;
	private DashboardController dashboardController;
	private RequestsController requestsController;
	private UnitsController unitsController;
	private TechniciansController techniciansController;
	private KitsController kitsController;
	private ClientsController clientsController;

	
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

	
	public void run() {
		JavaFxLauncher.setController(this);
		javafx.application.Application.launch(JavaFxLauncher.class);
	}

	
	public void startApplication(Stage primaryStage) {
		this.primaryStage = primaryStage;
		loadData();
		
		loginController = new LoginController(this, profileRepository);
		
		primaryStage.setScene(loginController.getView().getScene());
		primaryStage.show();
		primaryStage.centerOnScreen();
	}

	
	public void loadData() { loadSystemDataUseCase.execute(); }

	
	public void saveData() { saveSystemDataUseCase.execute(); }

	
	public void initializeSystem(String role) {
		mainView = new MainView(role);
		
		
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

	
	public void handleResponse(ResponseDTO response, Runnable onSuccess) {
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

	
	public void refreshAllViews() {
		if (clientsController != null) clientsController.refreshView();
		if (kitsController != null) kitsController.refreshView();
		if (unitsController != null) unitsController.refreshView();
		if (techniciansController != null) techniciansController.refreshView();
		if (requestsController != null) requestsController.refreshView();
		if (dashboardController != null) dashboardController.refreshView();
	}

	

	
	public void navigateToDashboard() { mainView.setContent(dashboardController.getView().getView()); }
	
	public void navigateToRequests() { mainView.setContent(requestsController.getView().getView()); }
	
	public void navigateToUnits() { mainView.setContent(unitsController.getView().getView()); }
	
	public void navigateToTechnicians() { mainView.setContent(techniciansController.getView().getView()); }
	
	public void navigateToKits() { mainView.setContent(kitsController.getView().getView()); }
	
	public void navigateToClients() { mainView.setContent(clientsController.getView().getView()); }

	
	public void triggerAddClientDialog(String clientId) {
		navigateToClients();
		clientsController.showAddClientDialog(clientId);
	}

	
	public void logout() {
		saveData();
		startApplication(this.primaryStage);
	}
}