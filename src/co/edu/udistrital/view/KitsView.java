package co.edu.udistrital.view;

import co.edu.udistrital.controller.KitsController;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Clase encargada de la representacion visual del inventario de Kits.
 * Implementa una interfaz dividida en paneles para distinguir entre la
 * disponibilidad operativa de los equipos y la pila de mantenimiento (LIFO).
 * Gestiona la interaccion del usuario para el registro, activacion y gestion de
 * recursos tecnicos. * @author Jimmy86gb
 */
public class KitsView {
	private VBox rootContainer;
	private VBox availableContainer;
	private VBox maintenanceContainer;
	private KitsController controller;
	private String role;

	/**
	 * Constructor de la vista de kits. Configura el diseño de columnas para separar
	 * visualmente los kits operativos de aquellos que se encuentran en proceso de
	 * reparacion o mantenimiento. * @param role Perfil de seguridad para habilitar
	 * o restringir acciones administrativas.
	 */
	public KitsView(String role) {
		this.role = role;
		rootContainer = new VBox(25);
		rootContainer.setPadding(new Insets(20));

		HBox headerBox = new HBox();
		headerBox.setAlignment(Pos.CENTER_LEFT);
		Label lblTitle = new Label("Inventario y Mantenimiento de Kits");
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

		Button btnNewKit = new Button("+ Registrar Kit");
		btnNewKit.setStyle(
				"-fx-background-color: #8B5CF6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
		btnNewKit.setOnAction(e -> showAddKitDialog());

		if (role.equals("OPERATOR")) {
			btnNewKit.setDisable(true);
		}

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		headerBox.getChildren().addAll(lblTitle, spacer, btnNewKit);

		HBox columnsContainer = new HBox(20);
		availableContainer = new VBox(12);
		maintenanceContainer = new VBox(12);

		VBox colAvailable = createColumn("✅ Kits Disponibles / Inactivos", "#D1FAE5", availableContainer);
		VBox colMaintenance = createColumn("🛠 Kits en Mantenimiento (Pila)", "#FEE2E2", maintenanceContainer);

		columnsContainer.getChildren().addAll(colAvailable, colMaintenance);
		HBox.setHgrow(colAvailable, Priority.ALWAYS);
		HBox.setHgrow(colMaintenance, Priority.ALWAYS);

		rootContainer.getChildren().addAll(headerBox, columnsContainer);
	}

	/**
	 * Metodo factoria para generar columnas de datos estandarizadas. Envuelve el
	 * contenedor de datos en un ScrollPane para garantizar la navegabilidad cuando
	 * el inventario supera el espacio vertical. * @param title Titulo de la
	 * columna.
	 * 
	 * @param bgColor           Color de fondo en hexadecimal.
	 * @param internalContainer Contenedor hijo donde se añadiran las tarjetas.
	 * @return Contenedor VBox con la estructura completa de la columna.
	 */
	private VBox createColumn(String title, String bgColor, VBox internalContainer) {
		VBox col = new VBox(15);
		col.setPadding(new Insets(15));
		col.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

		Label lblSection = new Label(title);
		lblSection.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

		internalContainer.setStyle("-fx-background-color: transparent;");
		internalContainer.setPadding(new Insets(5));

		ScrollPane scroll = new ScrollPane(internalContainer);
		scroll.setFitToWidth(true);
		scroll.setFitToHeight(true);
		scroll.setStyle("-fx-background: " + bgColor
				+ "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
		scroll.setBorder(Border.EMPTY);

		VBox.setVgrow(scroll, Priority.ALWAYS);
		col.getChildren().addAll(lblSection, scroll);
		return col;
	}

	/**
	 * Inyecta el controlador asociado a esta vista. * @param controller Instancia
	 * de KitsController.
	 */
	public void setController(KitsController controller) {
		this.controller = controller;
	}

	/**
	 * Limpia ambos contenedores de la vista para permitir una recarga limpia de los
	 * datos provenientes del repositorio.
	 */
	public void clearTable() {
		availableContainer.getChildren().clear();
		maintenanceContainer.getChildren().clear();
	}

	/**
	 * Crea y renderiza una tarjeta informativa (card) para un Kit especifico.
	 * Aplica logica de seguridad para habilitar botones de mantenimiento solo bajo
	 * el patron LIFO y habilita controles de administracion segun el rol. * @param
	 * kit Objeto DTO del kit a visualizar.
	 */
	public void addKit(KitDTO kit) {
		VBox card = new VBox(8);
		card.setPadding(new Insets(15));
		card.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1;");

		Label lblId = new Label("ID: " + kit.getId().toString().substring(0, 8));
		lblId.setFont(Font.font("Consolas", 11));

		Label lblType = new Label(kit.getType());
		lblType.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

		Label lblStatus = new Label("Estado: " + kit.getStatus());

		HBox actions = new HBox(10);
		actions.setAlignment(Pos.CENTER_RIGHT);

		Button btnCopy = new Button("📋 ID");
		btnCopy.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {
			{
				putString(kit.getId().toString());
			}
		}));
		actions.getChildren().add(btnCopy);

		boolean isMaintenance = kit.getStatus().equalsIgnoreCase("En mantenimiento")
				|| kit.getStatus().equalsIgnoreCase("Mantenimiento");

		if (isMaintenance) {
			if (role.equals("ADMIN")) {
				Button btnReturn = new Button("Retornar (Pila)");
				btnReturn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-cursor: hand;");
				btnReturn.setOnAction(e -> controller.returnKitToService());

				Button btnRetire = new Button("Baja (Pila)");
				btnRetire.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-cursor: hand;");
				btnRetire.setOnAction(e -> controller.retireKitFromMaintenance());

				// Regla LIFO estricta: Solo el elemento tope (isEditable) puede salir de la
				// pila
				if (!kit.isEditable()) {
					btnReturn.setDisable(true);
					btnRetire.setDisable(true);
				}

				actions.getChildren().addAll(btnReturn, btnRetire);
			}
		} else {
			if (role.equals("ADMIN")) {
				boolean isAvailable = kit.getStatus().equalsIgnoreCase("Disponible");
				Button btnToggle = new Button(isAvailable ? "Desactivar" : "Activar");
				btnToggle.setStyle(isAvailable ? "-fx-background-color: #6B7280; -fx-text-fill: white;"
						: "-fx-background-color: #3B82F6; -fx-text-fill: white;");

				btnToggle.setOnAction(e -> {
					String newStatus = isAvailable ? "Inactivo" : "Disponible";
					controller.updateKitStatus(kit.getId().toString(), kit.getType(), newStatus);
				});
				actions.getChildren().add(btnToggle);
			}
		}

		card.getChildren().addAll(lblId, lblType, lblStatus, actions);

		if (isMaintenance) {
			maintenanceContainer.getChildren().add(card);
		} else {
			availableContainer.getChildren().add(card);
		}
	}

	/**
	 * Muestra el dialogo para el registro de nuevos lotes de kits en inventario.
	 */
	private void showAddKitDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Registrar Kit");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<String> typeBox = new ComboBox<>();
		SimpleList.Iterator<String> it = controller.getKitTypeLabels().iterador();
		while (it.hasNext()) {
			typeBox.getItems().add(it.Next());
		}
		typeBox.getSelectionModel().selectFirst();

		TextField qtyField = new TextField("1");

		grid.add(new Label("Tipo de Kit:"), 0, 0);
		grid.add(typeBox, 1, 0);
		grid.add(new Label("Cantidad:"), 0, 1);
		grid.add(qtyField, 1, 1);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				try {
					controller.registerKit(typeBox.getValue(), Integer.parseInt(qtyField.getText()));
				} catch (NumberFormatException e) {
					new Alert(Alert.AlertType.ERROR, "Cantidad invalida").show();
				}
			}
		});
	}

	/**
	 * Retorna el contenedor principal de la vista. * @return VBox contenedor.
	 */
	public VBox getView() {
		return rootContainer;
	}
}