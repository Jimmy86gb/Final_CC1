package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Optional;

/**
 * Clase que define la vista para la gestion del inventario de kits.
 * Se encarga de la visualizacion del inventario actual y provee la interfaz 
 * necesaria para la adicion de nuevos kits al sistema.
 * 
 * @author Jimmy86gb
 */
public class KitsView {
    private VBox rootContainer;
    private GridPane dataGrid;
    private int currentRow = 1;
    private AppController appController;
    private ProfileType role;
    
    /**
     * Constructor de la clase.
     * Inicializa los componentes graficos, define el diseño de la tabla y configura 
     * los permisos de visualizacion segun el rol del usuario autenticado.
     * 
     * @param role Perfil del usuario que accede a la vista.
     */
    public KitsView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(25);
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label("Inventario de Kits y Herramientas");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #111827;");
        
        Button btnNewKit = new Button("+ Anadir Kit");
        btnNewKit.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btnNewKit.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNewKit.setOnAction(e -> showAddKitDialog());
        
        if (role == ProfileType.ADMIN) {
            btnNewKit.setVisible(false);
            btnNewKit.setManaged(false);
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewKit);

        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
        tableContainer.setPadding(new Insets(10, 20, 20, 20));

        dataGrid = new GridPane();
        dataGrid.setHgap(40);
        dataGrid.setVgap(15);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));

        addHeaderCell("ID Serial", 0);
        addHeaderCell("Tipo de Kit", 1);
        addHeaderCell("Estado del Inventario", 2);
        addHeaderCell("Acciones", 3);

        tableContainer.getChildren().add(dataGrid);
        rootContainer.getChildren().addAll(headerBox, tableContainer);
    }

    /**
     * Se asigna el controlador que gestionara los eventos de la vista.
     * 
     * @param controller Instancia del controlador de la aplicacion.
     */
    public void setController(AppController controller) { 
    	this.appController = controller; 
    }

    /**
     * Se genera y configura un encabezado de tabla con estilos predefinidos.
     * 
     * @param text Texto del encabezado.
     * @param col Indice de la columna.
     */
    private void addHeaderCell(String text, int col) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: #6B7280; -fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 10 0;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        dataGrid.add(lbl, col, 0);
    }

    /**
     * Se eliminan los registros actuales de la tabla manteniendo el encabezado.
     */
    public void clearTable() {
        dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        currentRow = 1;
    }

    /**
     * Se inserta una nueva fila con la informacion de un kit en la tabla.
     * Se aplican estilos segun el estado y se gestiona la habilitacion de los controles 
     * segun la propiedad de edicion.
     * 
     * @param kit Objeto DTO que contiene la informacion del kit.
     */
    public void addKit(KitDTO kit) {
        String shortId = kit.getId().toString().substring(0, 8);
        Label lblId = createDataCell(shortId);
        Label lblType = createDataCell(kit.getType());
        
        Label lblStatus = new Label(kit.getStatus());
        lblStatus.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lblStatus.setPadding(new Insets(4, 8, 4, 8));
        
        if (kit.getStatus().equals("Disponible") || kit.getStatus().equals("AVAILABLE")) {
            lblStatus.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-background-radius: 12;");
        } else {
            lblStatus.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-background-radius: 12;");
        }

        Button btnAction = new Button(role == ProfileType.ADMIN ? "Corregir Estado" : "Consultar");
        btnAction.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-cursor: hand;");
        btnAction.setDisable(!kit.isEditable());

        dataGrid.add(lblId, 0, currentRow);
        dataGrid.add(lblType, 1, currentRow);
        dataGrid.add(lblStatus, 2, currentRow);
        dataGrid.add(btnAction, 3, currentRow);
        currentRow++;
    }

    /**
     * Se crea una etiqueta configurada con estilos visuales para la presentacion de datos.
     * 
     * @param text Contenido de la etiqueta.
     * @return Objeto Label configurado.
     */
    private Label createDataCell(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", 14));
        lbl.setStyle("-fx-text-fill: #111827;");
        return lbl;
    }

    /**
     * Se despliega un cuadro de dialogo para el ingreso de datos de un nuevo kit.
     * La informacion capturada es remitida al controlador para su procesamiento.
     */
    private void showAddKitDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Anadir Lote de Kits");
        dialog.setHeaderText("Ingrese los datos del nuevo kit");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Kit de Grua", "Kit de Electricidad", "Kit General", "Kit de Cerrajeria", "Kit de Montallantas");
        typeBox.getSelectionModel().selectFirst();
        
        TextField qtyField = new TextField("1");

        grid.add(new Label("Tipo de Kit:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Cantidad:"), 0, 1);    grid.add(qtyField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int qty = Integer.parseInt(qtyField.getText());
                appController.registerKit(typeBox.getValue(), qty);
            } catch(NumberFormatException ex) {
            }
        }
    }

    /**
     * Se obtiene el contenedor principal de la vista.
     * * @return Contenedor VBox de la vista.
     */
    public VBox getView() { return rootContainer; }
}