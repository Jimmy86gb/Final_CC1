package co.edu.udistrital.controller;

/**
 * Clase principal y punto de entrada (entry point) de la aplicacion.
 * Su unica responsabilidad es instanciar el orquestador central del sistema 
 * y disparar el metodo de ejecucion que levantara el entorno grafico y 
 * la logica subyacente de todo el proyecto.
 * * @author Jimmy86gb
 */
public class Main {
	
	/**
	 * Metodo principal estatico que arranca la maquina virtual de Java.
	 * Construye el AppController y hace el llamado a su metodo run() para 
	 * iniciar formalmente el ciclo de vida de la arquitectura.
	 * * @param args Argumentos de linea de comandos pasados durante la ejecucion inicial.
	 */
	public static void main(String[] args) {
		AppController controller = new AppController();
		controller.run();
	}
}