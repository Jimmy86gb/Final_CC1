package co.edu.udistrital.view;

import co.edu.udistrital.controller.UnitsController;
import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class UnitsView {
    private VBox rootContainer;
    private VBox activeContainer;
    private VBox pendingContainer;
    private UnitsController controller;
    private String role;

    public UnitsView(String role) {
    	this.role = role;
        rootContainer = new VBox(25);
        rootContainer.setPadding(new Insets(20));
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label("Control de Unidades de Servicio");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        
        // BOTÓN RESTAURADO
        Button btnNewUnit = new Button("+ Registrar Unidad");
        btnNewUnit.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-padding: 8 16; -fx-cursor: hand;");
        btnNewUnit.setOnAction(e -> showAddUnitDialog());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewUnit);

        HBox columnsContainer = new HBox(20);
        activeContainer = new VBox(12);
        pendingContainer = new VBox(12);
        
        VBox colActive = createColumn("✅ Unidades Activas", "#DBEAFE", activeContainer);
        VBox colPending = createColumn("⏳ Cambios Pendientes (Aprobación Admin)", "#FEF3C7", pendingContainer);
        
        columnsContainer.getChildren().addAll(colActive, colPending);
        HBox.setHgrow(colActive, Priority.ALWAYS);
        HBox.setHgrow(colPending, Priority.ALWAYS);

        rootContainer.getChildren().addAll(headerBox, columnsContainer);
        VBox.setVgrow(columnsContainer, Priority.ALWAYS);
    }

    public void setController(UnitsController controller) { this.controller = controller; }

    private VBox createColumn(String title, String bgColor, VBox internalContainer) {
        VBox col = new VBox(15);
        col.setPadding(new Insets(15));
        col.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        internalContainer.setStyle("-fx-background-color: transparent;");
        internalContainer.setPadding(new Insets(5));
        
        ScrollPane scroll = new ScrollPane(internalContainer);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: " + bgColor + "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
        scroll.setBorder(Border.EMPTY);
        
        VBox.setVgrow(scroll, Priority.ALWAYS);
        col.getChildren().addAll(lbl, scroll);
        return col;
    }

    public void clearTable() {
        activeContainer.getChildren().clear();
        pendingContainer.getChildren().clear();
    }

    public void addUnit(ServiceUnitDTO unit, boolean isPending) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1;");

        Label lblId = new Label("ID: " + unit.getId().toString().substring(0, 8));
        lblId.setFont(Font.font("Consolas", 11));
        
        Label lblType = new Label("Vehículo: " + unit.getType());
        lblType.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Label lblStatus = new Label("Estado: " + unit.getStatus());
        Label lblZone = new Label("Zona: " + unit.getZone());

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCopy = new Button("📋 ID");
        btnCopy.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(unit.getId().toString()); }}));
        actions.getChildren().add(btnCopy);

        // Operador Solicita Cambio
        if (!isPending && role.equals("OPERATOR")) {
            Button btnEdit = new Button("✏️ Solicitar Cambio");
            btnEdit.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-cursor: hand;");
            btnEdit.setOnAction(e -> showEditUnitDialog(unit.getId().toString(), unit.getType(), unit.getStatus(), unit.getZone()));
            actions.getChildren().add(btnEdit);
        }

        // Administrador Aprueba/Rechaza Cambio
        if (isPending && role.equals("ADMIN")) {
            Button btnApprove = new Button("Aprobar");
            btnApprove.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;");
            btnApprove.setOnAction(e -> controller.approveUnitStatus());
            
            Button btnReject = new Button("Rechazar");
            btnReject.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white;");
            btnReject.setOnAction(e -> controller.rejectUnitStatus());
            
            actions.getChildren().addAll(btnApprove, btnReject);
        }

        card.getChildren().addAll(lblId, lblType, lblStatus, lblZone, actions);

        if (isPending) pendingContainer.getChildren().add(card);
        else activeContainer.getChildren().add(card);
    }
    
    private void showAddUnitDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Unidad");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        ComboBox<String> typeBox = new ComboBox<>();
        SimpleList.Iterator<String> tIt = controller.getUnitTypeLabels().iterador();
        while(tIt.hasNext()) typeBox.getItems().add(tIt.Next());
        typeBox.getSelectionModel().selectFirst();
        
        ComboBox<String> zoneBox = new ComboBox<>();
        SimpleList.Iterator<String> zIt = controller.getZoneLabels().iterador();
        while(zIt.hasNext()) zoneBox.getItems().add(zIt.Next());
        zoneBox.getSelectionModel().selectFirst();

        TextField qtyField = new TextField("1");

        grid.add(new Label("Tipo:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Zona:"), 0, 1);   grid.add(zoneBox, 1, 1);
        grid.add(new Label("Cantidad:"), 0, 2); grid.add(qtyField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                controller.registerUnit(typeBox.getValue(), zoneBox.getValue(), Integer.parseInt(qtyField.getText()));
            }
        });
    }

    private void showEditUnitDialog(String id, String currentType, String currentStatus, String currentZone) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Solicitar Cambio: " + id.substring(0, 8));
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        ComboBox<String> statusBox = new ComboBox<>();
        SimpleList.Iterator<String> sIt = controller.getUnitStatusLabels().iterador();
        while(sIt.hasNext()) statusBox.getItems().add(sIt.Next());
        statusBox.setValue(currentStatus);
        
        ComboBox<String> zoneBox = new ComboBox<>();
        SimpleList.Iterator<String> zIt = controller.getZoneLabels().iterador();
        while(zIt.hasNext()) zoneBox.getItems().add(zIt.Next());
        zoneBox.setValue(currentZone);

        grid.add(new Label("Nuevo Estado:"), 0, 0); grid.add(statusBox, 1, 0);
        grid.add(new Label("Nueva Zona:"), 0, 1);   grid.add(zoneBox, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                controller.updateServiceUnit(id, currentType, statusBox.getValue(), zoneBox.getValue());
                new Alert(Alert.AlertType.INFORMATION, "Cambio enviado a la pila de aprobación.").showAndWait();
            }
        });
    }

    public VBox getView() { return rootContainer; }
}