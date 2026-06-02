package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardView {
    private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    
    private Label lblActiveUnits;
    private Label lblCriticalCases;
    private Label lblMaintenance;

    public DashboardView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(30);
        
        Label lblTitle = new Label("Resumen General");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #111827;");

        HBox cardsContainer = new HBox(20);
        
        // Se inicializan las etiquetas numericas en cero
        lblActiveUnits = new Label("0");
        lblCriticalCases = new Label("0");
        lblMaintenance = new Label("0");
        
        VBox card1 = createCard("Unidades Activas", lblActiveUnits, "#10B981");
        VBox card2 = createCard("Casos Criticos", lblCriticalCases, "#EF4444");
        VBox card3 = createCard("En Mantenimiento", lblMaintenance, "#F59E0B");
        
        cardsContainer.getChildren().addAll(card1, card2, card3);
        rootContainer.getChildren().addAll(lblTitle, cardsContainer);

        if (role == ProfileType.ADMIN) {
            Button btnCSV = new Button("Exportar reporte diario (CSV)");
            btnCSV.setStyle("-fx-background-color:#10B981; -fx-text-fill:white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            
            btnCSV.setOnAction(e -> {
                if(appController != null) {
                    appController.exportDailyReport();
                }
            });
            
            rootContainer.getChildren().add(btnCSV);
        }
    }

    /**
     * Se asigna el controlador encargado de gestionar la logica de la vista.
     * * @param controller Instancia del controlador principal.
     */
    public void setController(AppController controller) {
        this.appController = controller;
    }

    /**
     * Se actualizan los valores estadisticos mostrados en el dashboard.
     * * @param active Cantidad de unidades activas en formato de texto.
     * @param critical Cantidad de casos criticos en formato de texto.
     * @param maint Cantidad de recursos en mantenimiento en formato de texto.
     */
    public void updateStatistics(String active, String critical, String maint) {
        lblActiveUnits.setText(active);
        lblCriticalCases.setText(critical);
        lblMaintenance.setText(maint);
    }

    /**
     * Se crea un contenedor visual (tarjeta) para presentar una metrica especifica.
     * * @param title Titulo de la metrica.
     * @param lblValue Componente Label que contiene el valor numerico.
     * @param hexColor Codigo de color hexadecimal para resaltar el valor.
     * @return Contenedor VBox configurado.
     */
    private VBox createCard(String title, Label lblValue, String hexColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefSize(250, 120);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lblTitle.setStyle("-fx-text-fill: #6B7280;");

        lblValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        lblValue.setStyle("-fx-text-fill: " + hexColor + ";");

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    /**
     * Retorna el contenedor principal de la vista.
     * * @return Contenedor VBox de la vista.
     */
    public VBox getView() { return rootContainer; }
}