package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Arranca todo el programa. Basicamente, JavaFX necesita esto para prender
 * motores y que el sistema empiece a funcionar.
 * 
 * @author Jimmy86gb
 */
public class JavaFxLauncher extends Application {

	private static AppController appController;

	/**
	 * Método para inyectar el controlador antes de ejecutar launch()
	 */
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
