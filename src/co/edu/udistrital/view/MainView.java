package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Clase que define la vista principal (contenedor raiz) de la aplicacion.
 * Gestiona la estructura base de la interfaz, incluyendo la barra de navegacion 
 * superior y el area central dinamica donde se visualizan los modulos del sistema.
 * * @author Jimmy86gb
 */
public class MainView {
    private BorderPane rootPane;
    private StackPane contentArea;
    private AppController appController;
    private String role;

    /**
     * Constructor de la clase.
     * Inicializa el contenedor raiz de tipo BorderPane y configura el area de 
     * contenido principal mediante un StackPane para el intercambio dinamico de vistas.
     * * @param role Perfil del usuario autenticado para la configuracion de permisos.
     */
    public MainView(String role) {
        this.role = role;

        rootPane = new BorderPane();
        rootPane.setStyle("-fx-background-color: #F3F4F6;");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        setupTopNavigation();

        rootPane.setCenter(contentArea);
    }

    /**
     * Se asigna el controlador de navegacion encargado de gestionar el flujo entre vistas.
     * Al recibir el controlador, se invoca la carga inicial del panel de control (Dashboard).
     * * @param controller Instancia del controlador principal de la aplicacion.
     */
    public void setNavigationController(AppController controller) {
        this.appController = controller;
        this.appController.navigateToDashboard();
    }

    /**
     * Se configura la barra de navegacion superior.
     * Instancia los botones de menu, los espaciadores elasticos y los controles 
     * de cierre de sesion integrados en la parte superior del layout.
     */
    private void setupTopNavigation() {
        HBox topNav = new HBox(15);
        topNav.setPadding(new Insets(15, 30, 15, 30));
        topNav.setStyle("-fx-background-color: #1F2937; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        topNav.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = new Label("AutoRescate 24/7");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblTitle.setStyle("-fx-text-fill: #F9FAFB;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnDashboard = createMenuButton("Dashboard");
        Button btnRequests = createMenuButton("Solicitudes");
        Button btnUnits = createMenuButton("Unidades");
        Button btnTechnicians = createMenuButton("Tecnicos");
        Button btnKits = createMenuButton("Kits");
        Button btnClients = createMenuButton("Clientes");

        btnDashboard.setOnAction(e -> appController.navigateToDashboard());
        btnRequests.setOnAction(e -> appController.navigateToRequests());
        btnUnits.setOnAction(e -> appController.navigateToUnits());
        btnTechnicians.setOnAction(e -> appController.navigateToTechnicians());
        btnKits.setOnAction(e -> appController.navigateToKits());
        btnClients.setOnAction(e -> appController.navigateToClients());
        
        topNav.getChildren().add(lblTitle);
        topNav.getChildren().add(spacer);

        topNav.getChildren().add(btnDashboard);
        topNav.getChildren().add(btnRequests);
        topNav.getChildren().add(btnUnits);
        topNav.getChildren().add(btnTechnicians);
        topNav.getChildren().add(btnKits);
        topNav.getChildren().add(btnClients);

        Label lblRole = new Label(
                role.equals("OPERATOR")
                        ? "Despachador"
                        : "ADMIN"
        );

        lblRole.setStyle("-fx-text-fill:white;");
        Button btnLogout = new Button("Cerrar Sesion");
        btnLogout.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnLogout.setStyle("-fx-background-color: #DC2626; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4; -fx-padding: 6 12;");
        btnLogout.setOnAction(e -> appController.logout());
        
        topNav.getChildren().add(lblRole);
        topNav.getChildren().add(btnLogout);
        rootPane.setTop(topNav);
    }

    /**
     * Se crea un boton de navegacion con estilos definidos para la interaccion visual.
     * Implementa efectos de cambio de color al pasar el cursor (hover).
     * * @param text Texto a visualizar en el boton.
     * @return Objeto Button configurado.
     */
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        String baseStyle = "-fx-background-color: transparent; -fx-text-fill: #D1D5DB; -fx-cursor: hand; -fx-padding: 8 15 8 15; -fx-background-radius: 5;";
        String hoverStyle = "-fx-background-color: #374151; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 15 8 15; -fx-background-radius: 5;";
        
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        return button;
    }

    /**
     * Reemplaza el contenido actual en el area central por un nuevo componente.
     * Limpia la pila del StackPane antes de añadir la nueva vista.
     * * @param viewNode Componente de interfaz a visualizar.
     */
    public void setContent(VBox viewNode) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(viewNode);
    }

    /**
     * Retorna la escena configurada con el contenedor raiz y las dimensiones definidas.
     * * @return Objeto Scene.
     */
    public Scene getScene() {
        return new Scene(rootPane, 1150, 700);
    }

    /**
     * Obtiene el perfil del usuario autenticado.
     * * @return String con el rol (ADMIN/OPERATOR).
     */
    public String getRole() {
        return role;
    }
}