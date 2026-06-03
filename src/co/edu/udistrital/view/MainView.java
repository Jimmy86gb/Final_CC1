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


public class MainView {
    private BorderPane rootPane;
    private StackPane contentArea;
    private AppController appController;
    private String role;

    
    public MainView(String role) {
        this.role = role;

        rootPane = new BorderPane();
        rootPane.setStyle("-fx-background-color: #F3F4F6;");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        setupTopNavigation();

        rootPane.setCenter(contentArea);
    }

    
    public void setNavigationController(AppController controller) {
        this.appController = controller;
        this.appController.navigateToDashboard();
    }

    
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

    
    public void setContent(VBox viewNode) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(viewNode);
    }

    
    public Scene getScene() {
        return new Scene(rootPane, 1150, 700);
    }

    
    public String getRole() {
        return role;
    }
}