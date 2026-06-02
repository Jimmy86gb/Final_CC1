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
    private VBox unavailableContainer;
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
        headerBox.getChildren().addAll(lblTitle, spacer); 

        HBox columnsContainer = new HBox(20);
        availableContainer = new VBox(12);
        unavailableContainer = new VBox(12);
        
        VBox colAvailable = createColumn("✅ Kits Disponibles", "#D1FAE5", availableContainer);
        VBox colUnavailable = createColumn("⚠️ En Mantenimiento / Inactivos", "#FEE2E2", unavailableContainer);
        
        columnsContainer.getChildren().addAll(colAvailable, colUnavailable);
        HBox.setHgrow(colAvailable, Priority.ALWAYS);
        HBox.setHgrow(colUnavailable, Priority.ALWAYS);

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
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: " + bgColor + "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
        scroll.setBorder(Border.EMPTY);
        
        VBox.setVgrow(scroll, Priority.ALWAYS);
        col.getChildren().addAll(lblSection, scroll);
        return col;
    }

    public void setController(AppController controller) { this.appController = controller; }

    public void clearTable() {
        availableContainer.getChildren().clear();
        unavailableContainer.getChildren().clear();
    }

    public void addKit(KitDTO kit) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1;");

        Label lblId = new Label("ID: " + kit.getId().toString().substring(0, 8));
        lblId.setFont(Font.font("Consolas", 11));
        
        Label lblType = new Label(kit.getType());
        lblType.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Label lblStatus = new Label("Estado: " + kit.getStatus());
        lblStatus.setFont(Font.font("Segoe UI", 12));

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCopy = new Button("📋 ID");
        btnCopy.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(kit.getId().toString()); }}));
        actions.getChildren().add(btnCopy);

        if (kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
        	if (role.equals("ADMIN")) {
                Button btnReturn = new Button("Retornar (Pila)");
                btnReturn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-cursor: hand;");
                btnReturn.setOnAction(e -> appController.returnKitToService());

                Button btnRetire = new Button("Baja (Pila)");
                btnRetire.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-cursor: hand;");
                btnRetire.setOnAction(e -> appController.retireKitFromMaintenance());
                
                actions.getChildren().addAll(btnReturn, btnRetire);
            }
        } else {
            // AQUÍ EL TOGGLE DISPONIBLE / INACTIVO
            ComboBox<String> stateBox = new ComboBox<>();
            stateBox.getItems().addAll("Disponible", "Inactivo");
            stateBox.setValue(kit.getStatus());
            
            Button btnApply = new Button("Aplicar");
            btnApply.setOnAction(e -> appController.updateKitStatus(kit.getId().toString(), kit.getType(), stateBox.getValue()));
            
            actions.getChildren().addAll(stateBox, btnApply);
        }

        card.getChildren().addAll(lblId, lblType, lblStatus, actions);

        if (kit.getStatus().equalsIgnoreCase("Disponible")) {
            availableContainer.getChildren().add(card);
        } else {
            unavailableContainer.getChildren().add(card);
        }
    }

    public VBox getView() { return rootContainer; }
}