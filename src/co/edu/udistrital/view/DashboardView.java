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
    private String role;
    private AppController appController;
    
    private Label lblActiveUnits;
    private Label lblCriticalCases;
    private Label lblMaintenance;
    private Label lblTotalRequests;

    public DashboardView(String role, AppController controller) {
        this.role = role;
        this.appController = controller;
        rootContainer = new VBox(20);
        rootContainer.setPadding(new Insets(20));
        
        Label lblTitle = new Label("Resumen General");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

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
        

        HBox actionsContainer = new HBox(15);
        actionsContainer.setAlignment(Pos.CENTER_LEFT);
        
        if ((role.equals("ADMIN"))) {
            Button btnCSV = new Button("Exportar reporte diario (CSV)");
            btnCSV.setStyle("-fx-background-color:#10B981; -fx-text-fill:white; -fx-padding: 10 20; -fx-cursor: hand;");
            btnCSV.setOnAction(e -> { if(appController != null) appController.exportDailyReport(); });
            actionsContainer.getChildren().add(btnCSV);
        }

        rootContainer.getChildren().addAll(lblTitle, cardsContainer, actionsContainer);
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
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lblValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        lblValue.setStyle("-fx-text-fill: " + hexColor + ";");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    public VBox getView() { return rootContainer; }
}