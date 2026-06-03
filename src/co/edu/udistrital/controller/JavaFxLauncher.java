package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;


public class JavaFxLauncher extends Application {

	private static AppController appController;

	
	public static void setController(AppController controller) {
		appController = controller;
	}

	
	@Override
	public void init() throws Exception {
		appController = new AppController();
		appController.loadData();
	}

	
	@Override
	public void start(Stage stage) {
		appController.startApplication(stage);
	}

	
	@Override
	public void stop() throws Exception {
		appController.saveData();
	}
}