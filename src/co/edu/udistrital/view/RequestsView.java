package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.EntityItem;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.enums.ProfileType;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RequestsView {
    private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    
    // Contenedores directos (Reemplazan el uso problemático de lookup)
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

        Button btnNewRequest = new Button("+ Nueva Solicitud");
        btnNewRequest.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
        btnNewRequest.setOnAction(e -> showInitRequestDialog());

        Button btnAssignManual = new Button("⚡ Asignar Siguiente Siniestro");
        btnAssignManual.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
        btnAssignManual.setOnAction(e -> {
            ReportDTO next = appController.getNextPendingReport();
            if (next != null) showAssignManualDialog(next);
            else new Alert(Alert.AlertType.INFORMATION, "No hay emergencias en espera.").showAndWait();
        });

        if (role == ProfileType.ADMIN) {
            btnNewRequest.setDisable(true);
            btnAssignManual.setDisable(true);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewRequest, new Label("  "), btnAssignManual);

        // Inicializamos los contenedores internos
        pendingCasesContainer = new VBox(10);
        ongoingCasesContainer = new VBox(10);
        confirmCasesContainer = new VBox(10);

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

    public void setController(AppController controller) { this.appController = controller; }

    // Ahora recibe el VBox interno directamente en vez de generarlo con ID
    private VBox createColumn(String title, String bgColor, VBox internalContainer) {
        VBox col = new VBox(15);
        col.setPadding(new Insets(15));
        col.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");
        
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        col.getChildren().add(lbl);

        ScrollPane scroll = new ScrollPane(internalContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        col.getChildren().add(scroll);
        
        return col;
    }

    // Se limpian las referencias directas, evitando NPE de CSS rendering
    public void clearPanels() {
        pendingCasesContainer.getChildren().clear();
        ongoingCasesContainer.getChildren().clear();
        confirmCasesContainer.getChildren().clear();
    }

    public void addReportCard(ReportDTO report, String targetPanel) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 6; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        HBox header = new HBox();
        Label lblTicket = new Label("TKT: " + report.getTicketID().toString().substring(0,8));
        lblTicket.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Label lblPriority = new Label(report.getPriority());
        lblPriority.setPadding(new Insets(2, 6, 2, 6));
        lblPriority.setStyle(report.getPriority().equals("Alta") ? "-fx-background-color: #EF4444; -fx-text-fill: white; -fx-background-radius: 4;" : "-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-background-radius: 4;");
        
        Region spc = new Region(); HBox.setHgrow(spc, Priority.ALWAYS);
        header.getChildren().addAll(lblTicket, spc, lblPriority);

        Label lblClient = new Label("👤 Cliente: " + report.getClientName());
        Label lblZone = new Label("📍 Zona: " + report.getReportZone());
        Label lblSpec = new Label("🔧 Especialidad: " + report.getProblemType());
        Label lblDesc = new Label("📝 " + report.getProblemDescription());
        lblDesc.setWrapText(true);
        lblDesc.setStyle("-fx-text-fill: #4B5563; -fx-font-style: italic;");

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        if (targetPanel.equals("PENDING") && role == ProfileType.OPERATOR) {
            Button btnCancel = new Button("Cancelar Cita");
            btnCancel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B;");
            btnCancel.setOnAction(e -> appController.cancelReport(report.getTicketID().toString()));
            actions.getChildren().add(btnCancel);
        }

        if (targetPanel.equals("ONGOING") && role == ProfileType.OPERATOR) {
            if (report.canUndo()) {
                Button btnUndo = new Button("Deshacer Asignación ↩");
                btnUndo.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309;");
                btnUndo.setOnAction(e -> appController.undoReport());
                actions.getChildren().add(btnUndo);
            }
            if (report.canFinish()) {
                Button btnFinish = new Button("Finalizar ✔");
                btnFinish.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;");
                btnFinish.setOnAction(e -> appController.finishReport(report.getTicketID().toString()));
                actions.getChildren().add(btnFinish);
            }
        }
        
        if (targetPanel.equals("CONFIRM") && role == ProfileType.ADMIN && report.canConfirm()) {
            Button btnApprove = new Button("Aprobar Cierre");
            btnApprove.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46;");
            btnApprove.setOnAction(e -> appController.approveReport());
            
            Button btnReject = new Button("Rechazar");
            btnReject.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B;");
            btnReject.setOnAction(e -> appController.rejectReport());
            actions.getChildren().addAll(btnApprove, btnReject);
        }

        card.getChildren().addAll(header, lblClient, lblZone, lblSpec, lblDesc, actions);

        // Se agregan directamente a las variables en lugar de hacer casting con el lookup
        if (targetPanel.equals("PENDING")) pendingCasesContainer.getChildren().add(card);
        else if (targetPanel.equals("ONGOING")) ongoingCasesContainer.getChildren().add(card);
        else if (targetPanel.equals("CONFIRM")) confirmCasesContainer.getChildren().add(card);
    }

    private void showAssignManualDialog(ReportDTO report) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Análisis de Asignación");
        dialog.setHeaderText("Despacho sugerido para Zona: " + report.getReportZone() + "\nRequiere: " + report.getProblemType());
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<EntityItem> techBox = new ComboBox<>();
        SimpleList.Iterator<EntityItem> tIt = appController.getSuggestedTechnicians(report.getReportZone(), report.getProblemType()).iterador();
        while(tIt.hasNext()) techBox.getItems().add(tIt.Next());
        techBox.getSelectionModel().selectFirst();

        ComboBox<EntityItem> unitBox = new ComboBox<>();
        SimpleList.Iterator<EntityItem> uIt = appController.getSuggestedUnits(report.getReportZone()).iterador();
        while(uIt.hasNext()) unitBox.getItems().add(uIt.Next());
        unitBox.getSelectionModel().selectFirst();

        ComboBox<EntityItem> kitBox = new ComboBox<>();
        SimpleList.Iterator<EntityItem> kIt = appController.getAvailableKitsForUI().iterador();
        while(kIt.hasNext()) kitBox.getItems().add(kIt.Next());
        kitBox.getSelectionModel().selectFirst();

        grid.add(new Label("Técnico Sugerido (Filtro Especialidad/Zona):"), 0, 0); grid.add(techBox, 1, 0);
        grid.add(new Label("Unidad Sugerida (Filtro Zona):"), 0, 1); grid.add(unitBox, 1, 1);
        grid.add(new Label("Kit Disponible:"), 0, 2); grid.add(kitBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if(res == ButtonType.OK && techBox.getValue() != null && unitBox.getValue() != null && kitBox.getValue() != null) {
                appController.assignReportResources(techBox.getValue().getId(), unitBox.getValue().getId(), kitBox.getValue().getId());
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
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Mecanico General", "Electrico Automotriz", "Cerrajero de Vehiculos", "Operador de Grua", "Operario Montallantas");
        typeBox.getSelectionModel().selectFirst();

        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("Alta", "Media", "Baja");
        priorityBox.getSelectionModel().selectFirst();

        ComboBox<String> zoneBox = new ComboBox<>();
        zoneBox.getItems().addAll("Usaquen", "Chapinero", "Santa Fe", "Suba", "Kennedy", "Fontibon", "Bosa");
        zoneBox.getSelectionModel().selectFirst();

        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);

        grid.add(new Label("Especialidad Requerida:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Prioridad:"), 0, 1); grid.add(priorityBox, 1, 1);
        grid.add(new Label("Zona del Siniestro:"), 0, 2); grid.add(zoneBox, 1, 2);
        grid.add(new Label("Descripción del Evento:"), 0, 3); grid.add(descArea, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) appController.submitReportCreation(client.getId(), descArea.getText(), typeBox.getValue(), priorityBox.getValue(), zoneBox.getValue());
        });
    }

    public VBox getView() { return rootContainer; }
}