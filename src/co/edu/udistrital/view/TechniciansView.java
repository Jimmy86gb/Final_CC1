package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.util.Optional;

/**
 * Clase que define la vista para la gestion de tecnicos.
 * Se encarga de la visualizacion del personal tecnico y gestiona los componentes 
 * necesarios para el registro de nuevos recursos en el sistema.
 * * @author Jimmy86gb
 */
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
        lblTitle.setStyle("-fx-text-fill: #111827;");
        
        Button btnNewTechnician = new Button("+ Registrar Tecnico");
        btnNewTechnician.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btnNewTechnician.setStyle("-fx-background-color: #8B5CF6; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNewTechnician.setOnAction(e -> showAddTechnicianDialog());
        
        if (role == ProfileType.ADMIN) {
            btnNewTechnician.setVisible(false);
            btnNewTechnician.setManaged(false);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewTechnician);

        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
        tableContainer.setPadding(new Insets(10, 20, 20, 20));

        dataGrid = new GridPane();
        dataGrid.setHgap(30);
        dataGrid.setVgap(15);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));

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
        lbl.setStyle("-fx-text-fill: #6B7280; -fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 10 0;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        dataGrid.add(lbl, col, 0);
    }

    public void clearTable() {
        dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        currentRow = 1;
    }

    public void addTechnician(String id, String name, String specialty, String status, String zone, boolean isEditable) {
        Label lblId = createDataCell(id.substring(0, 8));
        Label lblName = createDataCell(name);
        Label lblSpecialty = createDataCell(specialty);
        Label lblZone = createDataCell(zone);
        
        Label lblStatus = new Label(status);
        lblStatus.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lblStatus.setPadding(new Insets(4, 8, 4, 8));
        
        if (status.equals("Disponible")) {
            lblStatus.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-background-radius: 12;");
        } else if (status.equals("Ocupado")) {
            lblStatus.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-background-radius: 12;");
        } else {
            lblStatus.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -background-radius: 12;");
        }

        Button btnAction = new Button("Copiar ID");
        btnAction.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-cursor: hand;");
        btnAction.setDisable(!isEditable);

        // ACCION: Copia el UUID completo al portapapeles del sistema
        btnAction.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(id);
            clipboard.setContent(content);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("¡ID del Técnico copiado al portapapeles!\nYa puedes hacer Ctrl+V.");
            alert.showAndWait();
        });

        dataGrid.add(lblId, 0, currentRow);
        dataGrid.add(lblName, 1, currentRow);
        dataGrid.add(lblSpecialty, 2, currentRow);
        dataGrid.add(lblStatus, 3, currentRow);
        dataGrid.add(lblZone, 4, currentRow);
        dataGrid.add(btnAction, 5, currentRow);
        currentRow++;
    }

    private Label createDataCell(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", 14));
        lbl.setStyle("-fx-text-fill: #111827;");
        return lbl;
    }

    private void showAddTechnicianDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Tecnico");
        dialog.setHeaderText("Ingrese los datos del tecnico");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        ComboBox<String> specialtyBox = new ComboBox<>();
        specialtyBox.getItems().addAll("Mecanico General", "Electrico Automotriz", "Cerrajero de Vehiculos", "Operador de Grua", "Operario Montallantas");
        specialtyBox.getSelectionModel().selectFirst();
        
        ComboBox<String> zoneBox = new ComboBox<>();
        zoneBox.getItems().addAll("Usaquen", "Chapinero", "Santa Fe", "Suba", "Kennedy", "Fontibon", "Bosa");
        zoneBox.getSelectionModel().selectFirst();

        grid.add(new Label("Nombre:"), 0, 0);       grid.add(nameField, 1, 0);
        grid.add(new Label("Especialidad:"), 0, 1); grid.add(specialtyBox, 1, 1);
        grid.add(new Label("Zona:"), 0, 2);         grid.add(zoneBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appController.registerTechnician(nameField.getText(), specialtyBox.getValue(), zoneBox.getValue());
        }
    }

    public VBox getView() { return rootContainer; }
}