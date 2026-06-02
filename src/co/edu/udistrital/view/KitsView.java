package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.KitDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class KitsView {
    private VBox rootContainer;
    private VBox availableContainer;
    private VBox maintenanceContainer;
    private AppController appController;
    private String role;

    public KitsView(String role) {
        this.role = role;
        rootContainer = new VBox(25);
        rootContainer.setPadding(new Insets(20));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = new Label("Inventario y Mantenimiento de Kits");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer); // Eliminado botón de registrar

        HBox columnsContainer = new HBox(20);
        availableContainer = new VBox(12);
        maintenanceContainer = new VBox(12);
        
        VBox colAvailable = createColumn("✅ Kits Disponibles", "#D1FAE5", availableContainer);
        VBox colMaintenance = createColumn("🛠 Kits en Mantenimiento", "#FEE2E2", maintenanceContainer);
        
        columnsContainer.getChildren().addAll(colAvailable, colMaintenance);
        HBox.setHgrow(colAvailable, Priority.ALWAYS);
        HBox.setHgrow(colMaintenance, Priority.ALWAYS);

        rootContainer.getChildren().addAll(headerBox, columnsContainer);
    }

    private VBox createColumn(String title, String bgColor, VBox internalContainer) {
        VBox col = new VBox(15);
        col.setPadding(new Insets(15));
        col.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        
        Label lblSection = new Label(title);
        lblSection.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        
        internalContainer.setStyle("-fx-background-color: transparent;");
        internalContainer.setPadding(new Insets(5));
        
        ScrollPane scroll = new ScrollPane(internalContainer);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(600);
        scroll.setStyle("-fx-background: " + bgColor + "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
        scroll.setBorder(Border.EMPTY);
        
        VBox.setVgrow(scroll, Priority.ALWAYS);
        col.getChildren().addAll(lblSection, scroll);
        return col;
    }

    public void setController(AppController controller) { this.appController = controller; }

    public void clearTable() {
        availableContainer.getChildren().clear();
        maintenanceContainer.getChildren().clear();
    }

    public void addKit(KitDTO kit) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1;");

        Label lblId = new Label("ID: " + kit.getId().toString().substring(0, 8));
        lblId.setFont(Font.font("Consolas", 11));
        
        Label lblType = new Label(kit.getType());
        lblType.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCopy = new Button("📋 ID");
        btnCopy.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(kit.getId().toString()); }}));
        actions.getChildren().add(btnCopy);

        // Operador y Admin pueden gestionar disponibilidad
        Button btnAction = new Button();
        if (kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
            btnAction.setText("Retornar a Servicio");
            btnAction.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-cursor: hand;");
            btnAction.setOnAction(e -> appController.retireKitFromMaintenance());
        } else {
            btnAction.setText("Enviar a Mantenimiento");
            btnAction.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-cursor: hand;");
            btnAction.setOnAction(e -> appController.updateKitToMaintenance(kit.getId().toString(), kit.getType()));
        }
        actions.getChildren().add(btnAction);

        card.getChildren().addAll(lblId, lblType, actions);

        if (kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
            maintenanceContainer.getChildren().add(card);
        } else {
            availableContainer.getChildren().add(card);
        }
    }

    public VBox getView() { return rootContainer; }
}