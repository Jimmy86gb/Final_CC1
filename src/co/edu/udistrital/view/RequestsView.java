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

public class RequestsView {
    private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    
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

        if (role == ProfileType.ADMIN) {
            btnNewRequest.setVisible(false);
            btnNewRequest.setManaged(false);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewRequest);

        HBox panelsBox = new HBox(30);
        VBox criticalPanel = createQueuePanel("Casos Críticos (Alta Prioridad)", "#FEF2F2", "#EF4444");
        criticalPanel.setId("criticalPanel");
        
        VBox ordinaryPanel = createQueuePanel("Casos Ordinarios (FIFO)", "#F0F9FF", "#3B82F6");
        ordinaryPanel.setId("ordinaryPanel");

        panelsBox.getChildren().addAll(criticalPanel, ordinaryPanel);
        rootContainer.getChildren().addAll(headerBox, panelsBox);
    }

    public void setController(AppController controller) {
        this.appController = controller;
    }

    private VBox createQueuePanel(String queueTitle, String bgColor, String accentColor) {
        VBox panel = new VBox(15);
        panel.setPrefWidth(450);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8; -fx-border-color: " + accentColor + " transparent transparent transparent; -fx-border-width: 4 0 0 0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 3);");

        Label title = new Label(queueTitle);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: #374151;");
        
        VBox casesContainer = new VBox(10);
        casesContainer.setId("casesContainer"); 
        panel.getChildren().addAll(title, casesContainer);
        return panel;
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
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Cliente Encontrado");
        info.setHeaderText("Creando reporte para: " + client.getName());
        info.setContentText("formulario final de creación de Reporte (Desarrollo Pendiente)");
        info.showAndWait();
    }

    public void addReportCard(ReportDTO report) {
    	VBox card = new VBox(8);

    	card.setPadding(new Insets(15));
    	card.setStyle("-fx-background-color: white; -fx-background-radius: 6; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

    	boolean isCritical = report.getPriority().equalsIgnoreCase("Alto");

    	Label lblTicket = new Label("Ticket: " + report.getTicketID().toString().substring(0, 8));
    	lblTicket.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
    	lblTicket.setStyle("-fx-text-fill: " + (isCritical ? "#EF4444" : "#3B82F6") + ";");

    	Label lblClient = new Label("Cliente: " + report.getClientName());
    	lblClient.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));


    	Label lblDesc = new Label(report.getProblemDescription());
    	lblDesc.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
    	lblDesc.setStyle("-fx-text-fill: #4B5563;");
    	lblDesc.setWrapText(true);

    	HBox actions = new HBox(10);

    	if(role == ProfileType.OPERATOR){
	    	Button btnAssign = new Button("Asignar");
	    	Button btnFinish = new Button("Finalizar");
	    	actions.getChildren().addAll(
		    	btnAssign,
		    	btnFinish
		    );
    	}

    	else{
	    	Button btnAssign = new Button("Asignar");
	    	Button btnFinish = new Button("Finalizar");
	    	Button btnRevert = new Button("Revertir");
	    	actions.getChildren().addAll(
		    	btnAssign,
		    	btnFinish,
		    	btnRevert
		    );
    	}

    	card.getChildren().add(actions);
    	card.getChildren().addAll(lblTicket, lblClient, lblDesc);

    	VBox criticalPanel = (VBox) rootContainer.lookup("#criticalPanel");
    	VBox ordinaryPanel = (VBox) rootContainer.lookup("#ordinaryPanel");

    	if (isCritical && criticalPanel != null) {
    		((VBox)criticalPanel.lookup("#casesContainer")).getChildren().add(card);
    	} else if (ordinaryPanel != null) {
    		((VBox)ordinaryPanel.lookup("#casesContainer")).getChildren().add(card);
    	}

    	
    }

    public VBox getView() { 
    	return rootContainer; 
    	}
}