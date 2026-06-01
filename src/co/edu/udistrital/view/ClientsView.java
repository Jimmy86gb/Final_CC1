package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Optional;

/**
 * Clase que define la vista para la gestion de clientes.
 * Se encarga de la visualizacion del listado de clientes registrados y 
 * provee la interfaz necesaria para el registro de nuevos elementos en el sistema.
 * 
 * @author Jimmy86gb
 */
public class ClientsView {
    private VBox rootContainer;
    private GridPane dataGrid;
    private int currentRow = 1;
    private AppController appController;
    private ProfileType role;
    
    /**
     * Constructor de la clase.
     * Inicializa los componentes graficos, define el diseño de la tabla y configura 
     * los permisos de visualizacion segun el rol del usuario.
     * 
     * @param role Perfil del usuario que accede a la vista.
     */
    public ClientsView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(25);
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTitle = new Label("Directorio de Clientes");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #111827;");
        
        Button btnNewClient = new Button("+ Nuevo Cliente");
        btnNewClient.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btnNewClient.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNewClient.setOnAction(e -> showAddClientDialog(""));
        
        if(role == ProfileType.ADMIN){
            btnNewClient.setVisible(false);
            btnNewClient.setManaged(false);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewClient);

        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
        tableContainer.setPadding(new Insets(10, 20, 20, 20));

        dataGrid = new GridPane();
        dataGrid.setHgap(40);
        dataGrid.setVgap(15);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));

        addHeaderCell("ID / Cedula", 0);
        addHeaderCell("Nombre Completo", 1);
        addHeaderCell("Tipo de Cliente", 2);
        addHeaderCell("Contacto", 3);
        addHeaderCell("Acciones", 4);

        tableContainer.getChildren().add(dataGrid);
        rootContainer.getChildren().addAll(headerBox, tableContainer);
    }

    /**
     * Asigna el controlador que gestionara los eventos de la vista.
     * 
     * @param controller Instancia del controlador de la aplicacion.
     */
    public void setController(AppController controller) { 
        this.appController = controller; 
    }

    /**
     * Crea y configura una celda de encabezado para la tabla de datos.
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
     * Elimina los registros actuales de la tabla manteniendo unicamente el encabezado.
     */
    public void clearTable() {
        dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        currentRow = 1;
    }

    /**
     * Agrega una nueva fila con la informacion de un cliente a la tabla de visualizacion.
     * 
     * @param client Objeto DTO con los datos del cliente.
     */
    public void addClient(ClientDTO client) {
        Label lblId = createDataCell(client.getId());
        Label lblName = createDataCell(client.getName());
        Label lblContact = createDataCell(client.getContactInfo());
        
        Label lblType = new Label(client.getType());
        lblType.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lblType.setPadding(new Insets(4, 8, 4, 8));
        lblType.setStyle("-fx-background-color: #E0E7FF; -fx-text-fill: #3730A3; -fx-background-radius: 12;");

        Button btnEdit = new Button("Editar");
        btnEdit.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand; -fx-text-fill: #374151;");

        dataGrid.add(lblId, 0, currentRow);
        dataGrid.add(lblName, 1, currentRow);
        dataGrid.add(lblType, 2, currentRow);
        dataGrid.add(lblContact, 3, currentRow);
        dataGrid.add(btnEdit, 4, currentRow);
        currentRow++;
    }

    /**
     * Crea una etiqueta con los estilos visuales definidos para la presentacion de datos.
     * 
     * @param text Contenido del texto.
     * @return Label configurado.
     */
    private Label createDataCell(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", 14));
        lbl.setStyle("-fx-text-fill: #111827;");
        return lbl;
    }

    /**
     * Despliega un cuadro de dialogo para el ingreso de datos de un nuevo cliente.
     * La informacion capturada es enviada al controlador para su procesamiento.
     * 
     * @param prefillId Valor predeterminado para el campo de ID.
     */
    public void showAddClientDialog(String prefillId) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Nuevo Cliente");
        dialog.setHeaderText("Ingrese los datos del cliente");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(prefillId);
        TextField nameField = new TextField();
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Particular", "Empresarial", "Seguros");
        typeBox.getSelectionModel().selectFirst();
        TextField contactField = new TextField();

        grid.add(new Label("ID/Cedula:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);    grid.add(nameField, 1, 1);
        grid.add(new Label("Tipo:"), 0, 2);      grid.add(typeBox, 1, 2);
        grid.add(new Label("Contacto:"), 0, 3);  grid.add(contactField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appController.registerClient(idField.getText(), nameField.getText(), typeBox.getValue(), contactField.getText());
        }
    }

    /**
     * Retorna el contenedor principal de la vista.
     * 
     * @return Objeto VBox con el diseño de la vista.
     */
    public VBox getView() { return rootContainer; }
}