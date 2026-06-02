package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardView {
    private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    
    private Label lblActiveUnits;
    private Label lblCriticalCases;
    private Label lblMaintenance;
    private Label lblTotalRequests;
    private TextArea consoleLog; // <- El área de registro

    public DashboardView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(20);
        rootContainer.setPadding(new Insets(20));
        
        Label lblTitle = new Label("Resumen General");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #111827;");

        // Tarjetas de KPIs
        HBox cardsContainer = new HBox(20);
        lblActiveUnits = new Label("0");
        lblCriticalCases = new Label("0");
        lblMaintenance = new Label("0");
        lblTotalRequests = new Label("0");
        
        cardsContainer.getChildren().addAll(
            createCard("Unidades Activas", lblActiveUnits, "#10B981"),
            createCard("Casos Críticos", lblCriticalCases, "#EF4444"),
            createCard("En Mantenimiento", lblMaintenance, "#F59E0B"),
            createCard("Total Solicitudes", lblTotalRequests, "#3B82F6")
        );

        // --- APARTADO DE CONSOLA / REGISTRO ---
        Label lblLog = new Label("Historial de Operaciones (Últimas acciones):");
        lblLog.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        
        consoleLog = new TextArea();
        consoleLog.setEditable(false);
        consoleLog.setPrefHeight(150);
        consoleLog.setFont(Font.font("Consolas", 12)); // Fuente tipo consola
        consoleLog.setStyle("-fx-control-inner-background: #1E293B; -fx-text-fill: #34D399; -fx-border-radius: 5;");
        consoleLog.setText("Sistema iniciado...\n");

        // --- BOTONES DE ACCIÓN ---
        HBox actionsContainer = new HBox(15);
        actionsContainer.setAlignment(Pos.CENTER_LEFT);

        Button btnUndoGlobal = new Button("🔄 Revertir última operación");
        btnUndoGlobal.setStyle("-fx-background-color: #7C3AED; -fx-text-fill: white; -fx-padding: 10 20; -fx-cursor: hand;");
        btnUndoGlobal.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de revertir la última operación registrada?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                appController.performUndo();
            }
        });

        actionsContainer.getChildren().add(btnUndoGlobal);

        if (role == ProfileType.ADMIN) {
            Button btnCSV = new Button("Exportar reporte diario (CSV)");
            btnCSV.setStyle("-fx-background-color:#10B981; -fx-text-fill:white; -fx-padding: 10 20; -fx-cursor: hand;");
            btnCSV.setOnAction(e -> { if(appController != null) appController.exportDailyReport(); });
            actionsContainer.getChildren().add(btnCSV);
        }

        rootContainer.getChildren().addAll(lblTitle, cardsContainer, lblLog, consoleLog, actionsContainer);
    
        if (appController != null) {
            consoleLog.setText(appController.getConsoleHistory());
        }
    }

    /**
     * Agrega una nueva línea al log del dashboard.
     * @param message El mensaje de la operación realizada.
     */
    public void updateLog(String message) {
        consoleLog.appendText("> " + message + "\n");
    }

    public void setController(AppController controller) {
        this.appController = controller;
    }

    public void updateStatistics(String active, String critical, String maint, String total) {
        lblActiveUnits.setText(active);
        lblCriticalCases.setText(critical);
        lblMaintenance.setText(maint);
        lblTotalRequests.setText(total);
    }

    private VBox createCard(String title, Label lblValue, String hexColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefSize(250, 120);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lblTitle.setStyle("-fx-text-fill: #6B7280;");

        lblValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        lblValue.setStyle("-fx-text-fill: " + hexColor + ";");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    public VBox getView() { return rootContainer; }
}