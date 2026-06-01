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

/**
 * Clase que define la vista para la gestion de solicitudes de servicio.
 * Se encarga de la visualizacion de las colas de atencion y provee la interfaz
 * necesaria para el registro de nuevas solicitudes de emergencia.
 * 
 * @author Jimmy86gb
 */
public class RequestsView {
    private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    
    /**
     * Constructor de la clase.
     * Se inicializan los componentes graficos, se definen los paneles para la gestion
     * de casos criticos y ordinarios, y se configuran los permisos segun el rol del usuario.
     * 
     * @param role Perfil del usuario que accede a la vista.
     */
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

        // Se restringe la visibilidad del boton segun el rol del usuario
        if (role == ProfileType.ADMIN) {
            btnNewRequest.setVisible(false);
            btnNewRequest.setManaged(false);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewRequest);

        HBox panelsBox = new HBox(30);
        VBox criticalPanel = createQueuePanel("Casos Criticos (Alta Prioridad)", "#FEF2F2", "#EF4444");
        criticalPanel.setId("criticalPanel");
        
        VBox ordinaryPanel = createQueuePanel("Casos Ordinarios (FIFO)", "#F0F9FF", "#3B82F6");
        ordinaryPanel.setId("ordinaryPanel");

        panelsBox.getChildren().addAll(criticalPanel, ordinaryPanel);
        rootContainer.getChildren().addAll(headerBox, panelsBox);
    }

    /**
     * Se asigna el controlador encargado de gestionar la logica de la vista.
     * 
     * @param controller Instancia del controlador principal.
     */
    public void setController(AppController controller) {
        this.appController = controller;
    }

    /**
     * Se crea y configura un panel para la visualizacion de colas de casos.
     * 
     * @param queueTitle Titulo del panel.
     * @param bgColor Color de fondo del panel.
     * @param accentColor Color de acento para los bordes.
     * @return Contenedor VBox configurado.
     */
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

    /**
     * Se despliega un cuadro de dialogo para la busqueda de un cliente mediante su ID o cedula.
     */
    private void showClientSearchDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Cliente");
        dialog.setHeaderText("Nueva Emergencia");
        dialog.setContentText("Ingrese el ID / Cedula del cliente:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> appController.processNewRequest(id));
    }

    /**
     * Se presenta la interfaz para la creacion del reporte de emergencia asociado a un cliente.
     * 
     * @param client Objeto cliente seleccionado.
     */
    public void showCreateReportDialog(Client client) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Cliente Encontrado");
        info.setHeaderText("Creando reporte para: " + client.getName());
        info.setContentText("formulario final de creacion de Reporte (Desarrollo Pendiente)");
        info.showAndWait();
    }

    /**
     * Se agrega una tarjeta visual que representa una solicitud en la cola correspondiente.
     * Se gestionan los botones de accion segun el rol del usuario.
     * 
     * @param report Objeto DTO que contiene la informacion de la solicitud.
     */
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

        // Se configuran los botones de accion segun el perfil
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

        // Se localizan los contenedores de las colas para insertar la tarjeta
    	VBox criticalPanel = (VBox) rootContainer.lookup("#criticalPanel");
    	VBox ordinaryPanel = (VBox) rootContainer.lookup("#ordinaryPanel");

    	if (isCritical && criticalPanel != null) {
    		((VBox)criticalPanel.lookup("#casesContainer")).getChildren().add(card);
    	} else if (ordinaryPanel != null) {
    		((VBox)ordinaryPanel.lookup("#casesContainer")).getChildren().add(card);
    	}
    }

    /**
     * Retorna el contenedor principal de la vista.
     * 
     * @return Contenedor VBox de la vista.
     */
    public VBox getView() { 
    	return rootContainer; 
    }
}