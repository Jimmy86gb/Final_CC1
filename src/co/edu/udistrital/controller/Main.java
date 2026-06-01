package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Arranca todo el programa.
 * Basicamente, JavaFX necesita esto para prender motores y que el sistema empiece a funcionar.
 * 
 * @author Jimmy86gb
 */
public class Main extends Application {

    /**
     * Creamos el controlador y le Pasa el escenario principal para que empiece 
     * a mostrar las ventanas. Es el puente entre el arranque y la logica.
     * 
     * @param primaryStage El escenario principal de JavaFX donde se monta todo.
     */
    @Override
    public void start(Stage primaryStage) {
        // Crea el cerebro del programa y le pasa la ventana principal
        AppController controller = new AppController();
        controller.startApplication(primaryStage);
    }

    /**
     * El metodo main clasico de Java. 
     * Llama al launch de JavaFX para que se inicie todo el ciclo de vida de la aplicacion.
     * 
     * @param args Argumentos que vienen de la consola.
     */
    public static void main(String[] args) {
        launch(args);
    }
}