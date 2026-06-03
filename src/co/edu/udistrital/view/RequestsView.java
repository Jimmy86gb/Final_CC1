package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.dtos.EntityItem;
import co.edu.udistrital.model.dtos.ReportDTO;
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
	private String role;
	private AppController appController;

	private VBox pendingCasesContainer;
	private VBox ongoingCasesContainer;
	private VBox confirmCasesContainer;

	public RequestsView(String role) {
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
			if (next != null && next.getTicketID() != null && !next.getTicketID().toString().trim().isEmpty()) {
				showAssignManualDialog(next);
			} else {
				new Alert(Alert.AlertType.INFORMATION, "No hay emergencias válidas en espera.").showAndWait();
			}
		});

		if (role.equals("ADMIN")) {
			btnNewRequest.setDisable(true);
			btnAssignManual.setDisable(true);
		}

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		headerBox.getChildren().addAll(lblTitle, spacer, btnNewRequest, new Label("  "), btnAssignManual);

		pendingCasesContainer = new VBox(12);
		ongoingCasesContainer = new VBox(12);
		confirmCasesContainer = new VBox(12);

		HBox columnsContainer = new HBox(20);
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

		internalContainer.setStyle("-fx-background-color: transparent;");
		internalContainer.setPadding(new Insets(0, 5, 0, 0));

		ScrollPane scroll = new ScrollPane(internalContainer);
		scroll.setFitToWidth(true);
		scroll.setFitToHeight(true);
		scroll.setStyle("-fx-background: " + bgColor
				+ "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
		scroll.setBorder(Border.EMPTY);

		VBox.setVgrow(scroll, Priority.ALWAYS); // Altura dinámica garantizada
		col.getChildren().addAll(lbl, scroll);

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
		card.setMinHeight(Region.USE_PREF_SIZE);
		card.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

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

		Label lblPriority = new Label(priority);
		lblPriority.setPadding(new Insets(4, 8, 4, 8));
		lblPriority.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

		if (priority.equalsIgnoreCase("Alta")) {
			lblPriority.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-background-radius: 12;");
		} else if (priority.equalsIgnoreCase("Media")) {
			lblPriority.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-background-radius: 12;");
		} else {
			lblPriority.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-background-radius: 12;");
		}

		Region spc = new Region();
		HBox.setHgrow(spc, Priority.ALWAYS);
		header.getChildren().addAll(lblTicket, spc, lblPriority);

		Label lblClient = new Label("👤 Cliente: " + clientName);
		lblClient.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

		Label lblZone = new Label("📍 Zona: " + zone);
		Label lblSpec = new Label("🔧 Requerimiento: " + spec);
		Label lblDesc = new Label("📝 " + desc);
		lblDesc.setWrapText(true);
		lblDesc.setStyle("-fx-text-fill: #6B7280; -fx-font-style: italic;");

		HBox actions = new HBox(10);
		actions.setAlignment(Pos.CENTER_RIGHT);
		actions.setPadding(new Insets(10, 0, 0, 0));

		if (targetPanel.equals("PENDING") && role.equals("OPERATOR")) {
			Button btnCancel = new Button("Cancelar Cita");
			btnCancel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
			btnCancel.setOnAction(e -> appController.cancelReport(report.getTicketID().toString()));
			actions.getChildren().add(btnCancel);
		}

		if (targetPanel.equals("ONGOING") && role.equals("OPERATOR")) {
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

		if (targetPanel.equals("CONFIRM") && role.equals("ADMIN") && report.canConfirm()) {
			Button btnApprove = new Button("Aprobar");
			btnApprove.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-cursor: hand;");
			btnApprove.setOnAction(e -> appController.approveReport());

			Button btnReject = new Button("Rechazar");
			btnReject.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
			btnReject.setOnAction(e -> appController.rejectReport());
			actions.getChildren().addAll(btnApprove, btnReject);
		}

		card.getChildren().addAll(header, lblClient, lblZone, lblSpec, lblDesc, actions);

		if (targetPanel.equals("PENDING")) {
			pendingCasesContainer.getChildren().add(card);
		} else if (targetPanel.equals("ONGOING")) {
			ongoingCasesContainer.getChildren().add(card);
		} else if (targetPanel.equals("CONFIRM")) {
			confirmCasesContainer.getChildren().add(card);
		}
	}

	private void showAssignManualDialog(ReportDTO report) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Análisis de Asignación");
		dialog.setHeaderText(
				"Despacho sugerido para Zona: " + report.getReportZone() + "\nRequiere: " + report.getProblemType());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<EntityItem> techBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> tIt = appController
				.getSuggestedTechnicians(report.getReportZone(), report.getProblemType()).iterador();
		while (tIt.hasNext()) {
			techBox.getItems().add(tIt.Next());
		}
		if (!techBox.getItems().isEmpty()) {
			techBox.getSelectionModel().selectFirst();
		}

		ComboBox<EntityItem> unitBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> uIt = appController.getSuggestedUnits(report.getReportZone()).iterador();
		while (uIt.hasNext()) {
			unitBox.getItems().add(uIt.Next());
		}
		if (!unitBox.getItems().isEmpty()) {
			unitBox.getSelectionModel().selectFirst();
		}

		ComboBox<EntityItem> kitBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> kIt = appController.getSuggestedKits(report.getProblemType()).iterador();
		while (kIt.hasNext()) {
			kitBox.getItems().add(kIt.Next());
		}
		if (!kitBox.getItems().isEmpty()) {
			kitBox.getSelectionModel().selectFirst();
		}

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
				appController.assignReportResources(techBox.getValue().getId(), unitBox.getValue().getId(),
						kitBox.getValue().getId());
			} else if (res == ButtonType.OK) {
				new Alert(Alert.AlertType.ERROR,
						"Faltan recursos disponibles (Técnico, Unidad o Kit) para la asignación en esta zona.")
						.showAndWait();
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

	public void showCreateReportDialog(ClientDTO client) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Registrar Emergencia");
		dialog.setHeaderText("Cliente: " + client.getName() + " (" + client.getType() + ")");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<String> typeBox = new ComboBox<>();
		SimpleList.Iterator<String> sIt = appController.getSpecialityLabels().iterador();
		while (sIt.hasNext()) {
			typeBox.getItems().add(sIt.Next());
		}
		typeBox.getSelectionModel().selectFirst();

		ComboBox<String> zoneBox = new ComboBox<>();
		SimpleList.Iterator<String> zIt = appController.getZoneLabels().iterador();
		while (zIt.hasNext()) {
			zoneBox.getItems().add(zIt.Next());
		}
		zoneBox.getSelectionModel().selectFirst();

		// Solo Alta y Baja para el operador (Requisito)
		ComboBox<String> priorityBox = new ComboBox<>();
		priorityBox.getItems().addAll("Alta", "Baja");
		priorityBox.getSelectionModel().selectFirst();

		TextArea descArea = new TextArea();
		descArea.setPrefRowCount(3);

		grid.add(new Label("Especialidad Requerida:"), 0, 0);
		grid.add(typeBox, 1, 0);
		grid.add(new Label("Prioridad Solicitada:"), 0, 1);
		grid.add(priorityBox, 1, 1);
		grid.add(new Label("Zona del Siniestro:"), 0, 2);
		grid.add(zoneBox, 1, 2);
		grid.add(new Label("Descripción del Evento:"), 0, 3);
		grid.add(descArea, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				String finalPriority = priorityBox.getValue();
				String clientType = client.getType();

				// Conversión inteligente a Media
				if (finalPriority.equals("Baja")
						&& (clientType.equalsIgnoreCase("Seguros") || clientType.equalsIgnoreCase("Empresarial"))) {
					finalPriority = "Media";
					new Alert(Alert.AlertType.INFORMATION,
							"La prioridad se ajustó automáticamente a 'Media' por políticas del tipo de cliente.")
							.showAndWait();
				}

				appController.submitReportCreation(client.getId(), descArea.getText(), typeBox.getValue(),
						finalPriority, zoneBox.getValue());
			}
		});
	}

	public VBox getView() {
		return rootContainer;
	}
}