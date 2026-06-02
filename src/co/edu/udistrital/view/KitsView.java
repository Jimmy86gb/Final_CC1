package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class KitsView {
    private VBox rootContainer;
    private GridPane dataGrid;
    private int currentRow = 1;
    private AppController appController;
    private String role;
    
    public KitsView(String role) {
        this.role = role;
        rootContainer = new VBox(25);
        rootContainer.setPadding(new Insets(30));
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label("Inventario y Taller de Kits");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        
        Button btnNewKit = new Button("+ Añadir Lote de Kits");
        btnNewKit.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNewKit.setOnAction(e -> showAddKitDialog());
        if (!(role.equals("ADMIN"))) btnNewKit.setDisable(true);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewKit);

        // PANEL DE MANTENIMIENTO (Lógica LIFO - Sin seleccionar IDs)
        HBox maintPanel = new HBox(15);
        maintPanel.setAlignment(Pos.CENTER_LEFT);
        maintPanel.setPadding(new Insets(15));
        maintPanel.setStyle("-fx-background-color: #FEF3C7; -fx-border-color: #F59E0B; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        Label lblMaintTitle = new Label("🛠 Pila de Mantenimiento (LIFO):");
        lblMaintTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Button btnReturnKit = new Button("Retornar Tope al Servicio");
        btnReturnKit.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-cursor: hand;");
        btnReturnKit.setOnAction(e -> appController.returnKitToService());
        
        Button btnRetireKit = new Button("Dar de Baja Tope");
        btnRetireKit.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-cursor: hand;");
        btnRetireKit.setOnAction(e -> appController.retireKitFromMaintenance());
        
        if (!(role.equals("ADMIN"))) {
            btnReturnKit.setDisable(true);
            btnRetireKit.setDisable(true);
        }
        
        maintPanel.getChildren().addAll(lblMaintTitle, btnReturnKit, btnRetireKit);

        // Tabla General
        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        tableContainer.setPadding(new Insets(20));

        dataGrid = new GridPane();
        dataGrid.setHgap(30);
        dataGrid.setVgap(15);
        
        addHeaderCell("ID Serial", 0);
        addHeaderCell("Tipo de Kit", 1);
        addHeaderCell("Estado", 2);
        addHeaderCell("Acciones", 3);

        tableContainer.getChildren().add(dataGrid);
        rootContainer.getChildren().addAll(headerBox, maintPanel, tableContainer);
    }

    public void setController(AppController controller) { this.appController = controller; }

    private void addHeaderCell(String text, int col) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: #6B7280; -fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 10 0;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        dataGrid.add(lbl, col, 0);
    }

    public void clearTable() {
        dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        currentRow = 1;
    }

    public void addKit(KitDTO kit) {
        String fullId = kit.getId().toString();
        
        dataGrid.add(new Label(fullId.substring(0, 8)), 0, currentRow);
        dataGrid.add(new Label(kit.getType()), 1, currentRow);
        
        Label statusLbl = new Label(kit.getStatus());
        statusLbl.setStyle(kit.getStatus().equalsIgnoreCase("Disponible") ? "-fx-text-fill: #065F46; -fx-font-weight: bold;" : "-fx-text-fill: #991B1B; -fx-font-weight: bold;");
        dataGrid.add(statusLbl, 2, currentRow);

        HBox actions = new HBox(10);
        
        if (kit.getStatus().equalsIgnoreCase("Disponible") && (role.equals("ADMIN"))) {
            Button btnMaint = new Button("Enviar a Mantenimiento");
            btnMaint.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-cursor: hand;");
            btnMaint.setOnAction(e -> appController.updateKitToMaintenance(fullId, kit.getType()));
            actions.getChildren().add(btnMaint);
        } else if (kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
            Label lblPila = new Label("(En Pila LIFO de Reparación)");
            lblPila.setStyle("-fx-text-fill: #F59E0B; -fx-font-style: italic;");
            actions.getChildren().add(lblPila);
        }

        dataGrid.add(actions, 3, currentRow);
        currentRow++;
    }

    private void showAddKitDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Lote de Kits");
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        ComboBox<String> typeBox = new ComboBox<>();
        // ITERACIÓN CORREGIDA
        SimpleList.Iterator<String> tIt = appController.getKitTypeLabels().iterador();
        while(tIt.hasNext()) typeBox.getItems().add(tIt.Next());
        typeBox.getSelectionModel().selectFirst();
        
        TextField qtyField = new TextField("1");
        
        grid.add(new Label("Tipo:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Cantidad:"), 0, 1); grid.add(qtyField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if(res == ButtonType.OK) appController.registerKit(typeBox.getValue(), Integer.parseInt(qtyField.getText()));
        });
    }

    public VBox getView() { return rootContainer; }
}