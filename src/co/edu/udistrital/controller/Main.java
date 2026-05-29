package co.edu.udistrital.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import co.edu.udistrital.view.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instanciar Vistas
        MainView mainView = new MainView();
        UnitsView unitsView = new UnitsView();
        RequestsView requestsView = new RequestsView();

        // (Opcional) Sobrescribimos el comportamiento de los botones del MainView
        // para que en lugar de crear vistas nuevas en blanco, muestren estas instancias.
        // Aquí podrías ajustar tu MainView para recibir estas vistas ya instanciadas.

        // 2. Instanciar Controlador
        AppController controller = new AppController();
        controller.setViews(mainView, unitsView, requestsView);

        // 3. Configurar Escenario (Stage)
        primaryStage.setTitle("AutoRescate 24/7 - Sistema de Despacho");
        primaryStage.setScene(mainView.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}