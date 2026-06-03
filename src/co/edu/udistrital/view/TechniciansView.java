package co.edu.udistrital.view;

import co.edu.udistrital.controller.TechniciansController;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class TechniciansView {
	private VBox rootContainer;
	private GridPane dataGrid;
	private int currentRow = 1;
	private TechniciansController controller;
	private String role;

	
	public TechniciansView(String role) {
		this.role = role;
		rootContainer = new VBox(25);
		rootContainer.setPadding(new Insets(20));

		HBox headerBox = new HBox();
		headerBox.setAlignment(Pos.CENTER_LEFT);

		Label lblTitle = new Label("Gestion de Tecnicos");
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

		Button btnNewTechnician = new Button("+ Registrar Tecnico");
		btnNewTechnician
				.setStyle("-fx-background-color: #8B5CF6; -fx-text-fill: white; -fx-padding: 8 16; -fx-cursor: hand;");
		btnNewTechnician.setOnAction(e -> showAddTechnicianDialog());

		
		if (role.equals("OPERATOR")) {
			btnNewTechnician.setDisable(true);
		}

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		headerBox.getChildren().addAll(lblTitle, spacer, btnNewTechnician);

		VBox tableContainer = new VBox();
		tableContainer.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
		tableContainer.setPadding(new Insets(20));

		dataGrid = new GridPane();
		dataGrid.setHgap(30);
		dataGrid.setVgap(15);

		addHeaderCell("ID (UUID)", 0);
		addHeaderCell("Nombre", 1);
		addHeaderCell("Especialidad", 2);
		addHeaderCell("Estado", 3);
		addHeaderCell("Zona", 4);
		addHeaderCell("Acciones", 5);

		tableContainer.getChildren().add(dataGrid);
		rootContainer.getChildren().addAll(headerBox, tableContainer);
	}

	
	public void setController(TechniciansController controller) {
		this.controller = controller;
	}

	
	private void addHeaderCell(String text, int col) {
		Label lbl = new Label(text);
		lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
		lbl.setStyle(
				"-fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 10 0;");
		dataGrid.add(lbl, col, 0);
	}

	
	public void clearTable() {
		dataGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
		currentRow = 1;
	}

	
	public void addTechnician(String id, String name, String specialty, String status, String zone,
			boolean isEditable) {
		dataGrid.add(new Label(id.substring(0, 8)), 0, currentRow);
		dataGrid.add(new Label(name), 1, currentRow);
		dataGrid.add(new Label(specialty), 2, currentRow);
		dataGrid.add(new Label(status), 3, currentRow);
		dataGrid.add(new Label(zone), 4, currentRow);

		HBox actionsBox = new HBox(5);

		Button btnCopy = new Button("Copiar ID");
		btnCopy.setStyle("-fx-background-color: white; -fx-border-color: #D1D5DB; -fx-cursor: hand;");
		btnCopy.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {
			{
				putString(id);
			}
		}));

		if (role.equals("OPERATOR") || role.equals("ADMIN")) {
			Button btnToggle = new Button("Editar");
			btnToggle.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white;");

			btnToggle.setOnAction(e -> showEditTechnicianDialog(id, name, specialty, zone, status));
			actionsBox.getChildren().add(btnToggle);
		}

		actionsBox.getChildren().add(btnCopy);
		dataGrid.add(actionsBox, 5, currentRow);
		currentRow++;
	}

	
	private void showEditTechnicianDialog(String id, String name, String spec, String zone, String status) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Editar Tecnico");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		grid.add(new Label("ID: " + id.substring(0, 8)), 0, 0);
		grid.add(new Label("Nombre: " + name), 0, 1);
		grid.add(new Label("Especialidad: " + spec), 0, 2);

		ComboBox<String> statusBox = new ComboBox<>();
		SimpleList<String> statusList = controller.getTechnicianStatusLabels();
		SimpleList.Iterator<String> sIt = statusList.iterador();
		while (sIt.hasNext()) {
			statusBox.getItems().add(sIt.Next());
		}
		statusBox.setValue(status);

		ComboBox<String> zoneBox = new ComboBox<>();
		SimpleList<String> zoneList = controller.getZoneLabels();
		SimpleList.Iterator<String> zIt = zoneList.iterador();
		while (zIt.hasNext()) {
			zoneBox.getItems().add(zIt.Next());
		}
		zoneBox.setValue(zone);

		grid.add(new Label("Estado:"), 0, 3);
		grid.add(statusBox, 1, 3);
		grid.add(new Label("Zona:"), 0, 4);
		grid.add(zoneBox, 1, 4);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				controller.updateTechnician(id, name, spec, zoneBox.getValue(), statusBox.getValue());
			}
		});
	}

	
	private void showAddTechnicianDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Registrar Tecnico");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField nameField = new TextField();

		ComboBox<String> specialtyBox = new ComboBox<>();
		SimpleList.Iterator<String> sIt = controller.getSpecialityLabels().iterador();
		while (sIt.hasNext()) {
			specialtyBox.getItems().add(sIt.Next());
		}
		specialtyBox.getSelectionModel().selectFirst();

		ComboBox<String> zoneBox = new ComboBox<>();
		SimpleList.Iterator<String> zIt = controller.getZoneLabels().iterador();
		while (zIt.hasNext()) {
			zoneBox.getItems().add(zIt.Next());
		}
		zoneBox.getSelectionModel().selectFirst();

		grid.add(new Label("Nombre:"), 0, 0);
		grid.add(nameField, 1, 0);
		grid.add(new Label("Especialidad:"), 0, 1);
		grid.add(specialtyBox, 1, 1);
		grid.add(new Label("Zona:"), 0, 2);
		grid.add(zoneBox, 1, 2);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK && !nameField.getText().trim().isEmpty()) {
				controller.registerTechnician(nameField.getText(), specialtyBox.getValue(), zoneBox.getValue());
			}
		});
	}

	
	public VBox getView() {
		return rootContainer;
	}
}