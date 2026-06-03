package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase lanzadora del entorno grafico JavaFX para el sistema AutoRescate 24/7.
 * Actua como el punto de entrada oficial del ciclo de vida de la aplicacion,
 * encargandose de inicializar el hilo de la interfaz grafica y delegar el control
 * absoluto al orquestador principal (AppController).
 * * @author Jimmy86gb
 */
public class JavaFxLauncher extends Application {

	private static AppController appController;

	/**
	 * Inyecta la instancia estatica del controlador principal antes de iniciar
	 * el ciclo de vida de JavaFX mediante el metodo launch(). 
	 * Permite vincular el hilo principal de ejecucion con el entorno grafico.
	 * * @param controller Instancia del controlador central del sistema.
	 */
	public static void setController(AppController controller) {
		appController = controller;
	}

	/**
	 * Fase de inicializacion previa a la construccion de la interfaz grafica.
	 * En este punto se instancia el controlador principal y se solicita la carga
	 * de los datos persistidos en memoria antes de mostrar cualquier ventana.
	 * * @throws Exception Si ocurre un error critico durante la lectura de datos.
	 */
	@Override
	public void init() throws Exception {
		appController = new AppController();
		appController.loadData();
	}

	/**
	 * Punto de arranque visual de la aplicacion.
	 * Delega al controlador principal la construccion, configuracion y 
	 * despliegue de las escenas sobre el escenario base.
	 * * @param stage Escenario principal (ventana) provisto por JavaFX.
	 */
	@Override
	public void start(Stage stage) {
		appController.startApplication(stage);
	}

	/**
	 * Fase de apagado seguro de la aplicacion (Shutdown Hook de JavaFX).
	 * Garantiza que el controlador principal guarde el estado actual del sistema 
	 * en los archivos de persistencia antes de que la aplicacion se cierre por completo.
	 * * @throws Exception Si ocurre un error critico durante la escritura de datos.
	 */
	@Override
	public void stop() throws Exception {
		appController.saveData();
	}
}