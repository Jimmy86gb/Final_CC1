package co.edu.udistrital.view;

import co.edu.udistrital.controller.RequestsController;
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
	private RequestsController controller;

	private VBox pendingCasesContainer;
	private VBox ongoingCasesContainer;
	private VBox confirmCasesContainer;
	private VBox summaryContainer;

	
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
			ReportDTO next = controller.getNextPendingReport();
			if (next != null && next.getTicketID() != null && !next.getTicketID().toString().trim().isEmpty()) {
				showAssignManualDialog(next);
			} else {
				new Alert(Alert.AlertType.INFORMATION, "No hay emergencias validas en espera.").showAndWait();
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
		VBox confirmPanel = createColumn("Confirmacion Final", "#D1FAE5", confirmCasesContainer);

		columnsContainer.getChildren().addAll(pendingPanel, ongoingPanel, confirmPanel);
		HBox.setHgrow(pendingPanel, Priority.ALWAYS);
		HBox.setHgrow(ongoingPanel, Priority.ALWAYS);
		HBox.setHgrow(confirmPanel, Priority.ALWAYS);

		
		Label lblSummaryTitle = new Label("Resumen Historico de Solicitudes");
		lblSummaryTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

		summaryContainer = new VBox(5);
		summaryContainer.setPadding(new Insets(10));
		summaryContainer.setStyle(
				"-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1;");

		HBox headerRow = new HBox(15);
		headerRow.setPadding(new Insets(10));
		headerRow.setStyle("-fx-background-color: #F3F4F6; -fx-font-weight: bold; -fx-background-radius: 5;");

		Label hId = new Label("ID Tiquete");
		hId.setPrefWidth(80);
		hId.setMinWidth(80);
		hId.setMaxWidth(80);
		Label hClient = new Label("Cliente");
		hClient.setPrefWidth(150);
		hClient.setMinWidth(150);
		hClient.setMaxWidth(150);
		Label hType = new Label("Tipo Siniestro");
		hType.setPrefWidth(150);
		hType.setMinWidth(150);
		hType.setMaxWidth(150);
		Label hStatus = new Label("Estado");
		hStatus.setPrefWidth(100);
		hStatus.setMinWidth(100);
		hStatus.setMaxWidth(100);
		Label hDate = new Label("Fecha y Hora");
		hDate.setPrefWidth(130);
		hDate.setMinWidth(130);
		hDate.setMaxWidth(130);

		headerRow.getChildren().addAll(hId, hClient, hType, hStatus, hDate);

		VBox summaryWrapper = new VBox(headerRow, summaryContainer);

		ScrollPane summaryScroll = new ScrollPane(summaryWrapper);
		summaryScroll.setFitToWidth(true);
		summaryScroll.setPrefHeight(250);
		summaryScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

		rootContainer.getChildren().addAll(headerBox, columnsContainer, lblSummaryTitle, summaryScroll);
	}

	
	public void setController(RequestsController controller) {
		this.controller = controller;
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

		VBox.setVgrow(scroll, Priority.ALWAYS);
		col.getChildren().addAll(lbl, scroll);

		return col;
	}

	
	public void clearPanels() {
		pendingCasesContainer.getChildren().clear();
		ongoingCasesContainer.getChildren().clear();
		confirmCasesContainer.getChildren().clear();
		summaryContainer.getChildren().clear();
	}

	
	public void addReportToSummary(ReportDTO report) {
		HBox row = new HBox(15);
		row.setPadding(new Insets(10));
		row.setStyle("-fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");
		row.setAlignment(Pos.CENTER_LEFT);

		String rawId = report.getTicketID();
		String safeId = (rawId != null && rawId.length() >= 8) ? rawId.substring(0, 8)
				: (rawId != null ? rawId : "---");

		String safeClient = (report.getClientName() != null && !report.getClientName().trim().isEmpty())
				? report.getClientName()
				: "---";
		String safeType = (report.getProblemType() != null && !report.getProblemType().trim().isEmpty())
				? report.getProblemType()
				: "---";
		String safeStatus = (report.getReportStatus() != null && !report.getReportStatus().trim().isEmpty())
				? report.getReportStatus()
				: "---";
		String safeDate = (report.getReportTime() != null && !report.getReportTime().trim().isEmpty())
				? report.getReportTime()
				: "---";

		Label lblId = new Label(safeId);
		lblId.setPrefWidth(80);
		lblId.setMinWidth(80);
		lblId.setMaxWidth(80);
		lblId.setStyle("-fx-text-fill: #1F2937;");
		lblId.setFont(Font.font("Consolas", 12));

		Label lblClient = new Label(safeClient);
		lblClient.setPrefWidth(150);
		lblClient.setMinWidth(150);
		lblClient.setMaxWidth(150);
		lblClient.setStyle("-fx-text-fill: #1F2937;");

		Label lblType = new Label(safeType);
		lblType.setPrefWidth(150);
		lblType.setMinWidth(150);
		lblType.setMaxWidth(150);
		lblType.setStyle("-fx-text-fill: #1F2937;");

		Label lblStatus = new Label(safeStatus);
		lblStatus.setPrefWidth(100);
		lblStatus.setMinWidth(100);
		lblStatus.setMaxWidth(100);
		lblStatus.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

		String statusText = safeStatus.toLowerCase();
		if (statusText.contains("pendiente")) {
			lblStatus.setStyle("-fx-text-fill: #D97706;");
		} else if (statusText.contains("progreso")) {
			lblStatus.setStyle("-fx-text-fill: #2563EB;");
		} else if (statusText.contains("atendida")) {
			lblStatus.setStyle("-fx-text-fill: #10B981;");
		} else if (statusText.contains("cancelada")) {
			lblStatus.setStyle("-fx-text-fill: #DC2626;");
		} else {
			lblStatus.setStyle("-fx-text-fill: #1F2937;");
		}

		Label lblDate = new Label(safeDate);
		lblDate.setPrefWidth(130);
		lblDate.setMinWidth(130);
		lblDate.setMaxWidth(130);
		lblDate.setStyle("-fx-text-fill: #1F2937;");

		row.getChildren().addAll(lblId, lblClient, lblType, lblStatus, lblDate);
		summaryContainer.getChildren().add(row);
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
		String desc = report.getProblemDescription() != null ? report.getProblemDescription() : "Sin descripcion";

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
			btnCancel.setOnAction(e -> controller.cancelReport(report.getTicketID().toString()));
			actions.getChildren().add(btnCancel);
		}

		if (targetPanel.equals("ONGOING") && role.equals("OPERATOR")) {
			if (report.canUndo()) {
				Button btnUndo = new Button("Deshacer ↩");
				btnUndo.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-cursor: hand;");
				btnUndo.setOnAction(e -> controller.undoResourses());
				actions.getChildren().add(btnUndo);
			}
			if (report.canFinish()) {
				Button btnFinish = new Button("Finalizar ✔");
				btnFinish.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-cursor: hand;");
				btnFinish.setOnAction(e -> controller.finishReport(report.getTicketID().toString()));
				actions.getChildren().add(btnFinish);
			}
		}

		if (targetPanel.equals("CONFIRM") && role.equals("ADMIN") && report.canConfirm()) {
			Button btnApprove = new Button("Aprobar");
			btnApprove.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-cursor: hand;");
			btnApprove.setOnAction(e -> controller.approveReport());

			Button btnReject = new Button("Rechazar");
			btnReject.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
			btnReject.setOnAction(e -> controller.rejectReport());
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
		dialog.setTitle("Analisis de Asignacion");
		dialog.setHeaderText(
				"Despacho sugerido para Zona: " + report.getReportZone() + "\nRequiere: " + report.getProblemType());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<EntityItem> techBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> tIt = controller
				.getSuggestedTechnicians(report.getReportZone(), report.getProblemType()).iterador();
		while (tIt.hasNext()) {
			techBox.getItems().add(tIt.Next());
		}
		if (!techBox.getItems().isEmpty()) {
			techBox.getSelectionModel().selectFirst();
		}

		ComboBox<EntityItem> unitBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> uIt = controller.getSuggestedUnits(report.getReportZone()).iterador();
		while (uIt.hasNext()) {
			unitBox.getItems().add(uIt.Next());
		}
		if (!unitBox.getItems().isEmpty()) {
			unitBox.getSelectionModel().selectFirst();
		}

		ComboBox<EntityItem> kitBox = new ComboBox<>();
		SimpleList.Iterator<EntityItem> kIt = controller.getSuggestedKits(report.getProblemType()).iterador();
		while (kIt.hasNext()) {
			kitBox.getItems().add(kIt.Next());
		}
		if (!kitBox.getItems().isEmpty()) {
			kitBox.getSelectionModel().selectFirst();
		}

		grid.add(new Label("Tecnico Sugerido:"), 0, 0);
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
				controller.assignReportResources(techBox.getValue().getId(), unitBox.getValue().getId(),
						kitBox.getValue().getId());
			} else if (res == ButtonType.OK) {
				new Alert(Alert.AlertType.ERROR,
						"Faltan recursos disponibles (Tecnico, Unidad o Kit) para la asignacion en esta zona.")
						.showAndWait();
			}
		});
	}

	
	private void showInitRequestDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nueva Solicitud");
		dialog.setHeaderText("Validacion de Cliente");
		dialog.setContentText("Ingrese la cedula o NIT del cliente:");
		dialog.showAndWait().ifPresent(id -> controller.processNewRequest(id));
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
		SimpleList.Iterator<String> sIt = controller.getSpecialityLabels().iterador();
		while (sIt.hasNext()) {
			typeBox.getItems().add(sIt.Next());
		}
		typeBox.getSelectionModel().selectFirst();

		ComboBox<String> zoneBox = new ComboBox<>();
		SimpleList.Iterator<String> zIt = controller.getZoneLabels().iterador();
		while (zIt.hasNext()) {
			zoneBox.getItems().add(zIt.Next());
		}
		zoneBox.getSelectionModel().selectFirst();

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
		grid.add(new Label("Descripcion del Evento:"), 0, 3);
		grid.add(descArea, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.showAndWait().ifPresent(res -> {
			if (res == ButtonType.OK) {
				String finalPriority = priorityBox.getValue();
				String clientType = client.getType();
				if (finalPriority.equals("Baja")
						&& (clientType.equalsIgnoreCase("Seguros") || clientType.equalsIgnoreCase("Empresarial"))) {
					finalPriority = "Media";
					new Alert(Alert.AlertType.INFORMATION,
							"La prioridad se ajusto automaticamente a 'Media' por politicas del tipo de cliente.")
							.showAndWait();
				}
				controller.submitReportCreation(client.getId(), descArea.getText(), typeBox.getValue(), finalPriority,
						zoneBox.getValue());
			}
		});
	}

	public VBox getView() {
		return rootContainer;
	}
}