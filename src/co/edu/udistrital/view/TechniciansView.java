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

public class TechniciansView {
    private VBox rootContainer;
    private GridPane dataGrid;
    private int currentRow = 1;
    private AppController appController;
    private ProfileType role;
    
    public TechniciansView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(25);
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label("Gestion de Tecnicos");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        
        Button btnNewTechnician = new Button("+ Registrar Tecnico");
        btnNewTechnician.setStyle("-fx-background-color: #8B5CF6; -fx-text-fill: white; -fx-padding: 8 16; -fx-cursor: hand;");
        btnNewTechnician.setOnAction(e -> showAddTechnicianDialog());
        if (role == ProfileType.ADMIN) btnNewTechnician.setDisable(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewTechnician);

        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
        tableContainer.setPadding(new Insets(20));

        dataGrid = new GridPane();
        dataGrid.setHgap(30);
        dataGrid.setVgap(15);

        addHeaderCell("ID", 0);
        addHeaderCell("Nombre", 1);
        addHeaderCell("Especialidad", 2);
        addHeaderCell("Estado", 3);
        addHeaderCell("Zona", 4);
        addHeaderCell("Acciones", 5);

        tableContainer.getChildren().add(dataGrid);
        rootContainer.getChildren().addAll(headerBox, tableContainer);
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

    public void addTechnician(String id, String name, String specialty, String status, String zone, boolean isEditable) {
        dataGrid.add(new Label(id.substring(0, 8)), 0, currentRow);
        dataGrid.add(new Label(name), 1, currentRow);
        dataGrid.add(new Label(specialty), 2, currentRow);
        dataGrid.add(new Label(status), 3, currentRow);
        dataGrid.add(new Label(zone), 4, currentRow);

        Button btnAction = new Button("Copiar ID");
        btnAction.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-cursor: hand;");
        btnAction.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(id); }}));

        dataGrid.add(btnAction, 5, currentRow);
        currentRow++;
    }

    private void showAddTechnicianDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Tecnico");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        
        ComboBox<String> specialtyBox = new ComboBox<>();
        SimpleList.Iterator<String> sIt = appController.getSpecialityLabels().iterador();
        while(sIt.hasNext()) specialtyBox.getItems().add(sIt.Next());
        specialtyBox.getSelectionModel().selectFirst();
        
        ComboBox<String> zoneBox = new ComboBox<>();
        SimpleList.Iterator<String> zIt = appController.getZoneLabels().iterador();
        while(zIt.hasNext()) zoneBox.getItems().add(zIt.Next());
        zoneBox.getSelectionModel().selectFirst();

        grid.add(new Label("Nombre:"), 0, 0);       grid.add(nameField, 1, 0);
        grid.add(new Label("Especialidad:"), 0, 1); grid.add(specialtyBox, 1, 1);
        grid.add(new Label("Zona:"), 0, 2);         grid.add(zoneBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) appController.registerTechnician(nameField.getText(), specialtyBox.getValue(), zoneBox.getValue());
        });
    }

    public VBox getView() { return rootContainer; }
}