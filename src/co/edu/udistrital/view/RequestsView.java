package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Optional;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Optional;

public class RequestsView {
	private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    private VBox pendingPanel;
    private VBox ongoingPanel;
    private VBox confirmPanel;
    
    public RequestsView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(25);
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = new Label("Despacho de Solicitudes");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #111827;");

        Button btnNewRequest = new Button("+ Nueva Solicitud");
        btnNewRequest.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btnNewRequest.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNewRequest.setOnAction(e -> showClientSearchDialog());

     // Botón de asignación manual de recursos
        Button btnAssignManual = new Button("Asignar Siguiente Siniestro");
        btnAssignManual.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        btnAssignManual.setOnAction(e -> showAssignManualDialog());
        
        if (role == ProfileType.ADMIN) {
            btnNewRequest.setVisible(false);
            btnNewRequest.setManaged(false);
            btnNewRequest.setVisible(false);
            btnNewRequest.setManaged(false);
            btnAssignManual.setVisible(false);
            btnAssignManual.setManaged(false);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnAssignManual, btnNewRequest);

     // Paneles que representan las Colas y Pilas
        HBox panelsBox = new HBox(20);
        pendingPanel = createQueuePanel("Cola de Espera (Priorizadas)", "#FEF2F2", "#EF4444");
        ongoingPanel = createQueuePanel("Pila En Progreso (On-Going)", "#F0F9FF", "#3B82F6");
        confirmPanel = createQueuePanel("Pila de Confirmación (Juez)", "#F0FDF4", "#10B981");

        panelsBox.getChildren().addAll(pendingPanel, ongoingPanel, confirmPanel);
        rootContainer.getChildren().addAll(headerBox, panelsBox);
    }

    public void setController(AppController controller) {
        this.appController = controller;
    }

    private VBox createQueuePanel(String queueTitle, String bgColor, String accentColor) {
        VBox panel = new VBox(15);
        panel.setPrefWidth(350);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8; -fx-border-color: " + accentColor + " transparent transparent transparent; -fx-border-width: 4 0 0 0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 3);");

        Label title = new Label(queueTitle);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: #374151;");
        
        VBox casesContainer = new VBox(10);
        casesContainer.setId("casesContainer"); 
        panel.getChildren().addAll(title, casesContainer);
        return panel;
    }
    
    public void clearPanels() {
        ((VBox) pendingPanel.lookup("#casesContainer")).getChildren().clear();
        ((VBox) ongoingPanel.lookup("#casesContainer")).getChildren().clear();
        ((VBox) confirmPanel.lookup("#casesContainer")).getChildren().clear();
    }

    private void showClientSearchDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Cliente");
        dialog.setHeaderText("Nueva Emergencia");
        dialog.setContentText("Ingrese el ID / Cédula del cliente:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> appController.processNewRequest(id));
    }

    public void showCreateReportDialog(Client client) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registro de Siniestro");
        dialog.setHeaderText("Creando reporte para: " + client.getName());
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField descField = new TextField();
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Mecanico General", "Electrico Automotriz", "Cerrajero de Vehiculos", "Operador de Grua", "Operario Montallantas");
        typeBox.getSelectionModel().selectFirst();
        
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("Alta", "Media", "Baja");
        priorityBox.getSelectionModel().selectFirst();

        ComboBox<String> zoneBox = new ComboBox<>();
        zoneBox.getItems().addAll("Usaquen", "Chapinero", "Santa Fe", "Suba", "Kennedy", "Fontibon", "Bosa");
        zoneBox.getSelectionModel().selectFirst();

        grid.add(new Label("Descripción:"), 0, 0); grid.add(descField, 1, 0);
        grid.add(new Label("Especialidad Req:"), 0, 1); grid.add(typeBox, 1, 1);
        grid.add(new Label("Prioridad (Inicial):"), 0, 2); grid.add(priorityBox, 1, 2);
        grid.add(new Label("Zona:"), 0, 3); grid.add(zoneBox, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appController.submitReportCreation(client.getId(), descField.getText(), typeBox.getValue(), priorityBox.getValue(), zoneBox.getValue());
        }
    }
    
    private void showAssignManualDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Despachar Recursos");
        dialog.setHeaderText("Atendiendo la emergencia más prioritaria de la cola");
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField techIdField = new TextField();
        techIdField.setPromptText("UUID Técnico Libre");
        TextField unitIdField = new TextField();
        unitIdField.setPromptText("UUID Unidad Libre");
        TextField kitIdField = new TextField();
        kitIdField.setPromptText("UUID Kit Libre");

        grid.add(new Label("Técnico Asignado:"), 0, 0); grid.add(techIdField, 1, 0);
        grid.add(new Label("Unidad Asignada:"), 0, 1); grid.add(unitIdField, 1, 1);
        grid.add(new Label("Kit Asignado:"), 0, 2); grid.add(kitIdField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appController.assignReportResources(techIdField.getText().trim(), unitIdField.getText().trim(), kitIdField.getText().trim());
        }
    }

    public void addReportCard(ReportDTO report, String targetPanel) {
    	VBox card = new VBox(8);
    	card.setPadding(new Insets(15));
    	card.setStyle("-fx-background-color: white; -fx-background-radius: 6; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

    	Label lblTicket = new Label("Ticket: " + report.getTicketID().toString().substring(0, 8));
    	lblTicket.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
    	lblTicket.setStyle("-fx-text-fill: #374151;");

    	Label lblClient = new Label(report.getClientName() + " (" + report.getPriority() + ")");
    	lblClient.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

    	Label lblDesc = new Label(report.getProblemDescription());
    	lblDesc.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
    	lblDesc.setStyle("-fx-text-fill: #6B7280;");
    	lblDesc.setWrapText(true);

    	HBox actions = new HBox(8);
        
        // El renderizado dinamico con las banderas booleanas de la arquitectura
        if (report.canCancel()) {
            Button btnCancel = new Button("Cancelar Siniestro");
            btnCancel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
            btnCancel.setOnAction(e -> appController.cancelReport(report.getTicketID().toString()));
            actions.getChildren().add(btnCancel);
        }
        
        if (report.canUndo()) {
            Button btnUndo = new Button("Deshacer (Ctrl+Z)");
            btnUndo.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-cursor: hand;");
            btnUndo.setOnAction(e -> appController.undoReport());
            actions.getChildren().add(btnUndo);
        }
        
        if (report.canFinish()) {
            Button btnFinish = new Button("Terminar Tareas");
            btnFinish.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-cursor: hand;");
            btnFinish.setOnAction(e -> appController.finishReport(report.getTicketID().toString()));
            actions.getChildren().add(btnFinish);
        }
        
        if (report.canConfirm() && role == ProfileType.ADMIN) {
            Button btnApprove = new Button("Aprobar");
            btnApprove.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-cursor: hand;");
            btnApprove.setOnAction(e -> appController.approveReport());
            
            Button btnReject = new Button("Rechazar");
            btnReject.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-cursor: hand;");
            btnReject.setOnAction(e -> appController.rejectReport());
            
            actions.getChildren().addAll(btnApprove, btnReject);
        }

    	card.getChildren().addAll(lblTicket, lblClient, lblDesc, actions);

        if (targetPanel.equals("PENDING")) {
            ((VBox) pendingPanel.lookup("#casesContainer")).getChildren().add(card);
        } else if (targetPanel.equals("ONGOING")) {
            ((VBox) ongoingPanel.lookup("#casesContainer")).getChildren().add(card);
        } else if (targetPanel.equals("CONFIRM")) {
            ((VBox) confirmPanel.lookup("#casesContainer")).getChildren().add(card);
        }
    }

    public VBox getView() { 
    	return rootContainer; 
    }
}