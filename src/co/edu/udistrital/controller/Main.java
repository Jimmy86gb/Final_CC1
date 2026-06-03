package co.edu.udistrital.controller;

/**
 * Arranca todo el programa. Basicamente, JavaFX necesita esto para prender
 * motores y que el sistema empiece a funcionar.
 * 
 * @author Jimmy86gb
 */
public class Main {
	public static void main(String[] args) {
		AppController controller = new AppController();
		controller.run();
	}
}