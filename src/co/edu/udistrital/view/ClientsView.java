package co.edu.udistrital.view;

import co.edu.udistrital.controller.ClientsController;
import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Clase encargada de renderizar la interfaz grafica para la gestion de clientes.
 * Utiliza los componentes de JavaFX para construir un panel interactivo que 
 * incluye una tabla de datos (GridPane) y cuadros de dialogo para la creacion 
 * y edicion de perfiles. Mantiene una referencia a su controlador para delegar 
 * las acciones del usuario.
 * * @author Jimmy86gb
 */
public class ClientsView {
	private VBox rootContainer;
	private GridPane dataGrid;
	private int currentRow = 1;
	private ClientsController controller;
	private String role;

	/**
	 * Constructor de la vista de clientes.
	 * Inicializa el contenedor principal, la barra de herramientas superior 
	 * y la cuadricula de datos (GridPane) con sus respectivos encabezados.
	 * * @param role Rol del usuario actual para aplicar restricciones visuales (ej. ocultar botones).
	 */
	public ClientsView(String role) {
		this.role = role;
		rootContainer = new VBox(25);

		HBox headerBox = new HBox();
		headerBox.setAlignment(Pos.CENTER_LEFT);

		Label lblTitle = new Label("Directorio de Clientes");
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

		Button btnNewClient = new Button("+ Nuevo Cliente");
		btnNewClient.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-padding: 8 16; -fx-cursor: hand;");
		btnNewClient.setOnAction(e -> showAddClientDialog(""));
		
		if (role.equals("ADMIN")) {
			btnNewClient.setDisable(true);
		}

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		headerBox.getChildren().addAll(lblTitle, spacer, btnNewClient);

		VBox tableContainer = new VBox();
		tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
		tableContainer.setPadding(new Insets(20));

		dataGrid = new GridPane();
		dataGrid.setHgap(40);
		dataGrid.setVgap(15);

		addHeaderCell("ID / Cedula", 0);
		addHeaderCell("Nombre Completo", 1);
		addHeaderCell("Tipo de Cliente", 2);
		addHeaderCell("Contacto", 3);
		addHeaderCell("Acciones", 4);

		tableContainer.getChildren().add(dataGrid);
		rootContainer.getChildren().addAll(headerBox, tableContainer);
	}

	/**
	 * Establece el controlador que gestionara los eventos de esta vista.
	 * * @param controller Instancia de ClientsController.
	 */
	public void setController(ClientsController controller) {
		this.controller = controller;
	}

	/**
	 * Metodo auxiliar para crear y estilizar los encabezados de la cuadricula.
	 * * @param text Texto del encabezado.
	 * @param col Posicion de la columna en el GridPane.
	 */
	private void addHeaderCell(String text, int col) {
		Label lbl = new Label(text);
		lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
		lbl.setStyle("-fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 10 0;");
		dataGrid.add(lbl, col, 0);
	}

	/**
	 * Limpia dinamicamente las filas de datos del GridPane sin borrar los encabezados.
	 * Restablece el contador de filas para la proxima carga de datos.
	 */
	public void clearTable() {
		dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
		currentRow = 1;
	}

	/**
	 * Agrega una nueva fila al GridPane con la informacion del cliente y sus botones de accion.
	 * * @param client Objeto DTO con los datos del cliente a renderizar.
	 */
	public void addClient(ClientDTO client) {
		dataGrid.add(new Label(client.getId()), 0, currentRow);
		dataGrid.add(new Label(client.getName()), 1, currentRow);
		dataGrid.add(new Label(client.getType()), 2, currentRow);
		dataGrid.add(new Label(client.getContactInfo()), 3, currentRow);

		Button btnEdit = new Button("Editar");
		btnEdit.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-cursor: hand;");
		btnEdit.setOnAction(e -> showEditClientDialog(client));

		dataGrid.add(btnEdit, 4, currentRow);
		currentRow++;
	}

	/**
	 * Muestra un cuadro de dialogo interactivo para registrar un nuevo cliente.
	 * * @param prefillId ID a pre-cargar en el campo de texto (util cuando se redirige desde otra vista).
	 */
	public void showAddClientDialog(String prefillId) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Registrar Nuevo Cliente");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField idField = new TextField(prefillId);
		TextField nameField = new TextField();

		ComboBox<String> typeBox = new ComboBox<>();
		SimpleList.Iterator<String> cIt = controller.getClientTypeLabels().iterador();
		while (cIt.hasNext()) {
			typeBox.getItems().add(cIt.Next());
		}
		typeBox.getSelectionModel().selectFirst();

		TextField contactField = new TextField();

		grid.add(new Label("ID/Cedula:"), 0, 0);
		grid.add(idField, 1, 0);
		grid.add(new Label("Nombre:"), 0, 1);
		grid.add(nameField, 1, 1);
		grid.add(new Label("Tipo:"), 0, 2);
		grid.add(typeBox, 1, 2);
		grid.add(new Label("Contacto:"), 0, 3);
		grid.add(contactField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				controller.registerClient(idField.getText(), nameField.getText(), typeBox.getValue(), contactField.getText());
			}
		});
	}

	/**
	 * Muestra un cuadro de dialogo pre-poblado para editar la informacion de un cliente.
	 * * @param client DTO del cliente cuyos datos seran modificados.
	 */
	private void showEditClientDialog(ClientDTO client) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Editar Cliente");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField nameField = new TextField(client.getName());
		ComboBox<String> typeBox = new ComboBox<>();
		SimpleList.Iterator<String> cIt = controller.getClientTypeLabels().iterador();
		while (cIt.hasNext()) {
			typeBox.getItems().add(cIt.Next());
		}
		typeBox.setValue(client.getType());
		TextField contactField = new TextField(client.getContactInfo());

		grid.add(new Label("ID/Cedula:"), 0, 0);
		grid.add(new Label(client.getId()), 1, 0); // El ID no es editable
		grid.add(new Label("Nombre:"), 0, 1);
		grid.add(nameField, 1, 1);
		grid.add(new Label("Tipo:"), 0, 2);
		grid.add(typeBox, 1, 2);
		grid.add(new Label("Contacto:"), 0, 3);
		grid.add(contactField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				controller.updateClient(client.getId(), nameField.getText(), typeBox.getValue(), contactField.getText());
			}
		});
	}

	/**
	 * Retorna el contenedor raiz de esta vista para ser acoplado en la ventana principal.
	 * * @return Objeto VBox principal.
	 */
	public VBox getView() {
		return rootContainer;
	}
}