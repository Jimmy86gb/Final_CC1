package co.edu.udistrital.view;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainView {
    private BorderPane root;
    private StackPane contentArea;

    public MainView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F3F4F6;");

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));

        setupTopNav();
        root.setCenter(contentArea);
        
        showDashboard();
    }

    private void setupTopNav() {
        HBox topNav = new HBox(15);
        topNav.setPadding(new Insets(15, 30, 15, 30));
        topNav.setStyle("-fx-background-color: #1F2937; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        topNav.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("AutoRescate 24/7");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #F9FAFB;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnDashboard = createMenuButton("Dashboard");
        Button btnRequests = createMenuButton("Solicitudes");
        Button btnUnits = createMenuButton("Unidades");
        Button btnTechnicians = createMenuButton("Técnicos");
        Button btnKits = createMenuButton("Kits");

        btnDashboard.setOnAction(e -> showDashboard());
        btnRequests.setOnAction(e -> showRequests());
        btnUnits.setOnAction(e -> showUnits());
        btnTechnicians.setOnAction(e -> showTechnicians());
        btnKits.setOnAction(e -> showKits());
        
        topNav.getChildren().addAll(title, spacer, btnDashboard, btnRequests, btnUnits, btnTechnicians, btnKits);
        root.setTop(topNav);
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        String baseStyle = "-fx-background-color: transparent; -fx-text-fill: #D1D5DB; -fx-cursor: hand; -fx-padding: 8 15 8 15; -fx-background-radius: 5;";
        String hoverStyle = "-fx-background-color: #374151; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 15 8 15; -fx-background-radius: 5;";
        
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        
        return btn;
    }

    
    public void showDashboard() {
    }

    public void showRequests() {
    }

    public void showUnits() {
    }

    public void showTechnicians() {
    	
    }
    
    public void showKits() {
    	
    }
    public Scene getScene() {
        return new Scene(root, 1100, 650);
    }
}