package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Arranca todo el programa.
 * Basicamente, JavaFX necesita esto para prender motores y que el sistema empiece a funcionar.
 * 
 * @author Jimmy86gb
 */
public class JavaFxLauncher extends Application {
	
	private static AppController controller;

    /**
     * Método para inyectar el controlador antes de ejecutar launch()
     */
    public static void setController(AppController controller) {
        controller = controller;
    }

    @Override
    public void init() throws Exception {
        controller = new AppController();

        controller.loadData();
    }

    @Override
    public void start(Stage stage) {
        controller.startApplication(stage);
    }

    @Override
    public void stop() throws Exception {
        controller.saveData();
    }
}

