package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal que inicializa la aplicación JavaFX y el Controlador MVC.
 * 
 * @author Jimmy Alejandro Granados Becerra
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AutoRescate 24/7 - Sistema Operativo");
        
        // Instanciamos el controlador general que orquestará toda la aplicación
        AppController appController = new AppController(primaryStage);
        
        appController.seedMockData();
        // Ejecutamos el método del controlador para iniciar la vista
        appController.startApplication();
    }

    public static void main(String[] args) {
        launch(args);
    }
}