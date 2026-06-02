package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.util.Optional;

public class UnitsView {
    private VBox rootContainer;
    private GridPane dataGrid;
    private int currentRow = 1;
    private AppController appController;
    private String role;

    public UnitsView(String role) {
        this.role = role;
        rootContainer = new VBox(25);
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label("Unidades de Servicio");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        
        Button btnNewUnit = new Button("+ Registrar Unidad");
        btnNewUnit.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-padding: 8 16; -fx-cursor: hand;");
        btnNewUnit.setOnAction(e -> showAddUnitDialog());
        if ((role.equals("ADMIN"))) btnNewUnit.setDisable(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewUnit);

        // Panel de Aprobación de Unidades para ADMIN
        HBox confirmPanel = new HBox(15);
        if ((role.equals("ADMIN"))) {
            confirmPanel.setAlignment(Pos.CENTER_LEFT);
            confirmPanel.setPadding(new Insets(15));
            confirmPanel.setStyle("-fx-background-color: #E0E7FF; -fx-border-color: #3730A3; -fx-border-radius: 8;");
            
            Label lblConfTitle = new Label("🛡 Confirmar Unidades (Pila):");
            lblConfTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            
            Button btnApprove = new Button("Aprobar Tope");
            btnApprove.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;");
            btnApprove.setOnAction(e -> appController.approveUnitStatus());
            
            Button btnReject = new Button("Rechazar Tope");
            btnReject.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white;");
            btnReject.setOnAction(e -> appController.rejectUnitStatus());
            
            confirmPanel.getChildren().addAll(lblConfTitle, btnApprove, btnReject);
        }

        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
        tableContainer.setPadding(new Insets(20));

        dataGrid = new GridPane();
        dataGrid.setHgap(30);
        dataGrid.setVgap(15);

        addHeaderCell("ID (UUID)", 0);
        addHeaderCell("Tipo", 1);
        addHeaderCell("Estado", 2);
        addHeaderCell("Zona", 3);
        addHeaderCell("Acciones", 4);

        tableContainer.getChildren().add(dataGrid);
        
        if ((role.equals("ADMIN"))) rootContainer.getChildren().addAll(headerBox, confirmPanel, tableContainer);
        else rootContainer.getChildren().addAll(headerBox, tableContainer);
    }

    public void setController(AppController controller) { this.appController = controller; }

    private void addHeaderCell(String text, int col) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 10 0;");
        dataGrid.add(lbl, col, 0);
    }

    public void clearTable() {
        dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        currentRow = 1;
    }

    public void addUnit(String id, String type, String status, String zone, boolean isEditable) {
        dataGrid.add(new Label(id.substring(0, 8)), 0, currentRow);
        dataGrid.add(new Label(type), 1, currentRow);
        dataGrid.add(new Label(status), 2, currentRow);
        dataGrid.add(new Label(zone), 3, currentRow);

        Button btnAction = new Button("Copiar ID");
        btnAction.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-cursor: hand;");
        btnAction.setOnAction(e -> {
            Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(id); }});
        });
        
        dataGrid.add(btnAction, 4, currentRow);
        currentRow++;
    }

    private void showAddUnitDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Unidad");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> typeBox = new ComboBox<>();
        SimpleList.Iterator<String> tIt = appController.getUnitTypeLabels().iterador();
        while(tIt.hasNext()) typeBox.getItems().add(tIt.Next());
        typeBox.getSelectionModel().selectFirst();
        
        ComboBox<String> zoneBox = new ComboBox<>();
        SimpleList.Iterator<String> zIt = appController.getZoneLabels().iterador();
        while(zIt.hasNext()) zoneBox.getItems().add(zIt.Next());
        zoneBox.getSelectionModel().selectFirst();

        TextField qtyField = new TextField("1");

        grid.add(new Label("Tipo de Unidad:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Zona:"), 0, 1);           grid.add(zoneBox, 1, 1);
        grid.add(new Label("Cantidad:"), 0, 2);       grid.add(qtyField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                try { appController.registerUnit(typeBox.getValue(), zoneBox.getValue(), Integer.parseInt(qtyField.getText())); } 
                catch(NumberFormatException ex) {}
            }
        });
    }

    public VBox getView() { return rootContainer; }
}