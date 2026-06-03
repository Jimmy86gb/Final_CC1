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


public class ClientsView {
	private VBox rootContainer;
	private GridPane dataGrid;
	private int currentRow = 1;
	private ClientsController controller;
	private String role;

	
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

	
	public void setController(ClientsController controller) {
		this.controller = controller;
	}

	
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
		grid.add(new Label(client.getId()), 1, 0); 
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

	
	public VBox getView() {
		return rootContainer;
	}
}