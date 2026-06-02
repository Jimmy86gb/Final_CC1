package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.EntityItem;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ProfileType;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RequestsView {
	private VBox rootContainer;
	private ProfileType role;
	private AppController appController;

	// Contenedores directos
	private VBox pendingCasesContainer;
	private VBox ongoingCasesContainer;
	private VBox confirmCasesContainer;

	public RequestsView(ProfileType role) {
		this.role = role;
		rootContainer = new VBox(25);
		rootContainer.setPadding(new Insets(20));

		HBox headerBox = new HBox();
		headerBox.setAlignment(Pos.CENTER_LEFT);

		Label lblTitle = new Label("Despacho y Control de Emergencias");
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
		lblTitle.setStyle("-fx-text-fill: #111827;");

		Button btnNewRequest = new Button("+ Nueva Solicitud");
		btnNewRequest.setStyle(
				"-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
		btnNewRequest.setOnAction(e -> showInitRequestDialog());

		Button btnAssignManual = new Button("⚡ Asignar Siguiente Siniestro");
		btnAssignManual.setStyle(
				"-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
		btnAssignManual.setOnAction(e -> {
			ReportDTO next = appController.getNextPendingReport();
			if (next != null) {
				showAssignManualDialog(next);
			} else {
				new Alert(Alert.AlertType.INFORMATION, "No hay emergencias en espera.").showAndWait();
			}
		});

		if (role == ProfileType.ADMIN) {
			btnNewRequest.setDisable(true);
			btnAssignManual.setDisable(true);
		}

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		headerBox.getChildren().addAll(lblTitle, spacer, btnNewRequest, new Label("  "), btnAssignManual);

		// Inicializamos los contenedores internos con espacio entre tarjetas
		pendingCasesContainer = new VBox(12);
		ongoingCasesContainer = new VBox(12);
		confirmCasesContainer = new VBox(12);

		HBox columnsContainer = new HBox(20);

		// Creamos las columnas pasandole su respectivo contenedor
		VBox pendingPanel = createColumn("Cola de Espera (FIFO)", "#FEE2E2", pendingCasesContainer);
		VBox ongoingPanel = createColumn("En Progreso (Pila LIFO)", "#FEF3C7", ongoingCasesContainer);
		VBox confirmPanel = createColumn("Confirmación Final", "#D1FAE5", confirmCasesContainer);

		columnsContainer.getChildren().addAll(pendingPanel, ongoingPanel, confirmPanel);
		HBox.setHgrow(pendingPanel, Priority.ALWAYS);
		HBox.setHgrow(ongoingPanel, Priority.ALWAYS);
		HBox.setHgrow(confirmPanel, Priority.ALWAYS);

		rootContainer.getChildren().addAll(headerBox, columnsContainer);
	}

	public void setController(AppController controller) {
		this.appController = controller;
	}

	private VBox createColumn(String title, String bgColor, VBox internalContainer) {
		VBox col = new VBox(15);
		col.setPadding(new Insets(15));
		col.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");

		Label lbl = new Label(title);
		lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
		lbl.setStyle("-fx-text-fill: #1F2937;");
		col.getChildren().add(lbl);

		// CORRECCIÓN CSS: Hacemos que el VBox interno y el ScrollPane sean
		// transparentes
		// para que no parezcan tarjetas blancas gigantes cuando están vacíos.
		internalContainer.setStyle("-fx-background-color: transparent;");
		internalContainer.setPadding(new Insets(0, 5, 0, 0));

		ScrollPane scroll = new ScrollPane(internalContainer);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background: " + bgColor
				+ "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
		scroll.setBorder(Border.EMPTY);

		VBox.setVgrow(scroll, Priority.ALWAYS);
		col.getChildren().add(scroll);

		return col;
	}

	public void clearPanels() {
		pendingCasesContainer.getChildren().clear();
		ongoingCasesContainer.getChildren().clear();
		confirmCasesContainer.getChildren().clear();
	}

	public void addReportCard(ReportDTO report, String targetPanel) {
		VBox card = new VBox(10);
		card.setPadding(new Insets(15));
		card.setMinHeight(Region.USE_PREF_SIZE); // CORRECCIÓN: Evita que la tarjeta se aplaste
		card.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

		// VALIDACIONES NULL-SAFE: Si el DTO viene incompleto, muestra N/A en vez de
		// dañar la tarjeta
		String ticket = report.getTicketID() != null ? report.getTicketID().toString() : "N/A";
		if (ticket.length() > 8) {
			ticket = ticket.substring(0, 8);
		}

		String priority = report.getPriority() != null ? report.getPriority() : "Normal";
		String clientName = report.getClientName() != null ? report.getClientName() : "Desconocido";
		String zone = report.getReportZone() != null ? report.getReportZone() : "N/A";
		String spec = report.getProblemType() != null ? report.getProblemType() : "N/A";
		String desc = report.getProblemDescription() != null ? report.getProblemDescription() : "Sin descripción";

		HBox header = new HBox();
		Label lblTicket = new Label("TKT: " + ticket);
		lblTicket.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
		lblTicket.setStyle("-fx-text-fill: #111827;");

		Label lblPriority = new Label(priority);
		lblPriority.setPadding(new Insets(4, 8, 4, 8));
		lblPriority.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

		if (priority.equalsIgnoreCase("Alta")) {
			lblPriority.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-background-radius: 12;");
		} else {
			lblPriority.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-background-radius: 12;");
		}

		Region spc = new Region();
		HBox.setHgrow(spc, Priority.ALWAYS);
		header.getChildren().addAll(lblTicket, spc, lblPriority);

		// Se asignan colores oscuros forzados para que el texto sea legible
		Label lblClient = new Label("👤 Cliente: " + clientName);
		lblClient.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
		lblClient.setStyle("-fx-text-fill: #374151;");

		Label lblZone = new Label("📍 Zona: " + zone);
		lblZone.setStyle("-fx-text-fill: #4B5563;");

		Label lblSpec = new Label("🔧 Requerimiento: " + spec);
		lblSpec.setStyle("-fx-text-fill: #4B5563;");

		Label lblDesc = new Label("📝 " + desc);
		lblDesc.setWrapText(true);
		lblDesc.setStyle("-fx-text-fill: #6B7280; -fx-font-style: italic;");

		HBox actions = new HBox(10);
		actions.setAlignment(Pos.CENTER_RIGHT);
		actions.setPadding(new Insets(10, 0, 0, 0));

		if (targetPanel.equals("PENDING") && role == ProfileType.OPERATOR) {
			Button btnCancel = new Button("Cancelar Cita");
			btnCancel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
			btnCancel.setOnAction(e -> appController.cancelReport(report.getTicketID().toString()));
			actions.getChildren().add(btnCancel);
		}

		if (targetPanel.equals("ONGOING") && role == ProfileType.OPERATOR) {
			if (report.canUndo()) {
				Button btnUndo = new Button("Deshacer ↩");
				btnUndo.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-cursor: hand;");
				btnUndo.setOnAction(e -> appController.rejectReport());
				actions.getChildren().add(btnUndo);
			}
			if (report.canFinish()) {
				Button btnFinish = new Button("Finalizar ✔");
				btnFinish.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-cursor: hand;");
				btnFinish.setOnAction(e -> appController.finishReport(report.getTicketID().toString()));
				actions.getChildren().add(btnFinish);
			}
		}

		if (targetPanel.equals("CONFIRM") && role == ProfileType.ADMIN && report.canConfirm()) {
			Button btnApprove = new Button("Aprobar");
			btnApprove.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-cursor: hand;");
			btnApprove.setOnAction(e -> appController.approveReport());

			Button btnReject = new Button("Rechazar");
			btnReject.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
			btnReject.setOnAction(e -> appController.rejectReport());
			actions.getChildren().addAll(btnApprove, btnReject);
		}

		card.getChildren().addAll(header, lblClient, lblZone, lblSpec, lblDesc, actions);

		// Se agrega directamente a la lista en vez de usar lookup
		if (targetPanel.equals("PENDING")) {
			pendingCasesContainer.getChildren().add(card);
		} else if (targetPanel.equals("ONGOING")) {
			ongoingCasesContainer.getChildren().add(card);
		} else if (targetPanel.equals("CONFIRM")) {
			confirmCasesContainer.getChildren().add(card);
		}
	}

	// CAMBIADO POR OPTIMIZAR
	private void showAssignManualDialog(ReportDTO report) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Análisis de Asignación");
		dialog.setHeaderText(
				"Despacho sugerido para Zona: " + report.getReportZone() + "\nRequiere: " + report.getProblemType());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
        // Se agrega directamente a la lista en vez de usar lookup
        if (targetPanel.equals("PENDING")) pendingCasesContainer.getChildren().add(card);
        else if (targetPanel.equals("ONGOING")) ongoingCasesContainer.getChildren().add(card);
        else if (targetPanel.equals("CONFIRM")) confirmCasesContainer.getChildren().add(card);
    }

    private void showAssignManualDialog(ReportDTO report) {
        // NULL SAFETY ADICIONAL PARA LA VISTA:
        String safeZone = report.getReportZone() != null ? report.getReportZone() : "Desconocida";
        String safeSpec = report.getProblemType() != null ? report.getProblemType() : "Desconocida";

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Análisis de Asignación");
        dialog.setHeaderText("Despacho sugerido para Zona: " + safeZone + "\nRequiere: " + safeSpec);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

		// 1. TÉCNICOS: Carga dinámica desde el backend
		ComboBox<EntityItem> techBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> tIt = appController
				.getSuggestedTechnicians(report.getReportZone(), report.getProblemType()).iterador();
		while (tIt.hasNext()) {
			techBox.getItems().add(tIt.Next());
		}
		techBox.getSelectionModel().selectFirst();
        ComboBox<EntityItem> techBox = new ComboBox<>();
        SimpleList.Iterator<EntityItem> tIt = appController.getSuggestedTechnicians(safeZone, safeSpec).iterador();
        while(tIt.hasNext()) techBox.getItems().add(tIt.Next());
        techBox.getSelectionModel().selectFirst();

        ComboBox<EntityItem> unitBox = new ComboBox<>();
        SimpleList.Iterator<EntityItem> uIt = appController.getSuggestedUnits(safeZone).iterador();
        while(uIt.hasNext()) unitBox.getItems().add(uIt.Next());
        unitBox.getSelectionModel().selectFirst();

		// 3. KITS (CORREGIDO): Ahora le pasamos el tipo de problema para que traiga el
		// Kit exacto
		ComboBox<EntityItem> kitBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> kIt = appController.getSuggestedKits(report.getProblemType()).iterador();
		while (kIt.hasNext()) {
			kitBox.getItems().add(kIt.Next());
		}
		kitBox.getSelectionModel().selectFirst();
        ComboBox<EntityItem> kitBox = new ComboBox<>();
        SimpleList.Iterator<EntityItem> kIt = appController.getAvailableKitsForUI(safeSpec).iterador();
        while(kIt.hasNext()) kitBox.getItems().add(kIt.Next());
        kitBox.getSelectionModel().selectFirst();

		grid.add(new Label("Técnico Sugerido:"), 0, 0);
		grid.add(techBox, 1, 0);
		grid.add(new Label("Unidad Sugerida:"), 0, 1);
		grid.add(unitBox, 1, 1);
		grid.add(new Label("Kit Disponible:"), 0, 2);
		grid.add(kitBox, 1, 2);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK && techBox.getValue() != null && unitBox.getValue() != null
					&& kitBox.getValue() != null) {
				// Todo el despacho se maneja con puros IDs en texto plano
				appController.assignReportResources(techBox.getValue().getId(), unitBox.getValue().getId(),
						kitBox.getValue().getId());
			} else if (res == ButtonType.OK) {
				new Alert(Alert.AlertType.ERROR, "Faltan recursos disponibles para la asignación.").showAndWait();
			}
		});
	}

	private void showInitRequestDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nueva Solicitud");
		dialog.setHeaderText("Validación de Cliente");
		dialog.setContentText("Ingrese la cédula o NIT del cliente:");
		dialog.showAndWait().ifPresent(id -> appController.processNewRequest(id));
	}

	public void showCreateReportDialog(Client client) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Registrar Emergencia");
		dialog.setHeaderText("Cliente: " + client.getName());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<String> typeBox = new ComboBox<>();
		typeBox.getItems().addAll("Mecanico General", "Electrico Automotriz", "Cerrajero de Vehiculos",
				"Operador de Grua", "Operario Montallantas");
		typeBox.getSelectionModel().selectFirst();

		ComboBox<String> priorityBox = new ComboBox<>();
		priorityBox.getItems().addAll("Alta", "Media", "Baja");
		priorityBox.getSelectionModel().selectFirst();

		ComboBox<String> zoneBox = new ComboBox<>();
		zoneBox.getItems().addAll("Usaquen", "Chapinero", "Santa Fe", "Suba", "Kennedy", "Fontibon", "Bosa");
		zoneBox.getSelectionModel().selectFirst();

		TextArea descArea = new TextArea();
		descArea.setPrefRowCount(3);

		grid.add(new Label("Especialidad Requerida:"), 0, 0);
		grid.add(typeBox, 1, 0);
		grid.add(new Label("Prioridad:"), 0, 1);
		grid.add(priorityBox, 1, 1);
		grid.add(new Label("Zona del Siniestro:"), 0, 2);
		grid.add(zoneBox, 1, 2);
		grid.add(new Label("Descripción del Evento:"), 0, 3);
		grid.add(descArea, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				appController.submitReportCreation(client.getId(), descArea.getText(), typeBox.getValue(),
						priorityBox.getValue(), zoneBox.getValue());
			}
		});
	}

	public VBox getView() {
		return rootContainer;
	}
}