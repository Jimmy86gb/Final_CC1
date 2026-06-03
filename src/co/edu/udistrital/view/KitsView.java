package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class KitsView {
    private VBox rootContainer;
    private VBox availableContainer;
    private VBox maintenanceContainer;
    private AppController appController;
    private String role;

    public KitsView(String role) {
        this.role = role;
        rootContainer = new VBox(25);
        rootContainer.setPadding(new Insets(20));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = new Label("Inventario y Mantenimiento de Kits");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        
        Button btnNewKit = new Button("+ Registrar Kit");
        btnNewKit.setStyle("-fx-background-color: #8B5CF6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
        btnNewKit.setOnAction(e -> showAddKitDialog());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(lblTitle, spacer, btnNewKit);

        HBox columnsContainer = new HBox(20);
        availableContainer = new VBox(12);
        maintenanceContainer = new VBox(12);
        
        VBox colAvailable = createColumn("✅ Kits Disponibles / Inactivos", "#D1FAE5", availableContainer);
        VBox colMaintenance = createColumn("🛠 Kits en Mantenimiento (Pila)", "#FEE2E2", maintenanceContainer);
        
        columnsContainer.getChildren().addAll(colAvailable, colMaintenance);
        HBox.setHgrow(colAvailable, Priority.ALWAYS);
        HBox.setHgrow(colMaintenance, Priority.ALWAYS);

        rootContainer.getChildren().addAll(headerBox, columnsContainer);
    }

    private VBox createColumn(String title, String bgColor, VBox internalContainer) {
        VBox col = new VBox(15);
        col.setPadding(new Insets(15));
        col.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        
        Label lblSection = new Label(title);
        lblSection.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        
        internalContainer.setStyle("-fx-background-color: transparent;");
        internalContainer.setPadding(new Insets(5));
        
        ScrollPane scroll = new ScrollPane(internalContainer);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: " + bgColor + "; -fx-background-color: transparent; -fx-control-inner-background: transparent;");
        scroll.setBorder(Border.EMPTY);
        
        VBox.setVgrow(scroll, Priority.ALWAYS);
        col.getChildren().addAll(lblSection, scroll);
        return col;
    }

    public void setController(AppController controller) { this.appController = controller; }

    public void clearTable() {
        availableContainer.getChildren().clear();
        maintenanceContainer.getChildren().clear();
    }

    public void addKit(KitDTO kit) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1;");

        Label lblId = new Label("ID: " + kit.getId().toString().substring(0, 8));
        lblId.setFont(Font.font("Consolas", 11));
        
        Label lblType = new Label(kit.getType());
        lblType.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Label lblStatus = new Label("Estado: " + kit.getStatus());

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCopy = new Button("📋 ID");
        btnCopy.setOnAction(e -> Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{ putString(kit.getId().toString()); }}));
        actions.getChildren().add(btnCopy);

        // CORRECCIÓN 1: Unificamos la validación del estado para evitar discrepancias de texto
        boolean isMaintenance = kit.getStatus().equalsIgnoreCase("En mantenimiento") || kit.getStatus().equalsIgnoreCase("Mantenimiento");

        // 1. LÓGICA DE MANTENIMIENTO (PILA LIFO)
        if (isMaintenance) {
            if (role.equals("ADMIN")) {
                Button btnReturn = new Button("Retornar (Pila)");
                btnReturn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-cursor: hand;");
                btnReturn.setOnAction(e -> appController.returnKitToService());

                Button btnRetire = new Button("Baja (Pila)");
                btnRetire.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-cursor: hand;");
                btnRetire.setOnAction(e -> appController.retireKitFromMaintenance());
                
                // CORRECCIÓN 2: Aplicamos la regla LIFO. Si no es el tope, se bloquean los botones.
                if (!kit.isEditable()) {
                    btnReturn.setDisable(true);
                    btnRetire.setDisable(true);
                }
                
                actions.getChildren().addAll(btnReturn, btnRetire);
            }
        } 
        // 2. LÓGICA DE ACTIVACIÓN 
        else {
            // ADMIN controla el Toggle de Activar/Desactivar
            if (role.equals("ADMIN")) {
                boolean isAvailable = kit.getStatus().equalsIgnoreCase("Disponible");
                Button btnToggle = new Button(isAvailable ? "Desactivar" : "Activar");
                btnToggle.setStyle(isAvailable ? "-fx-background-color: #6B7280; -fx-text-fill: white;" : "-fx-background-color: #3B82F6; -fx-text-fill: white;");
                
                btnToggle.setOnAction(e -> {
                    String newStatus = isAvailable ? "Inactivo" : "Disponible";
                    appController.updateKitStatus(kit.getId().toString(), kit.getType(), newStatus);
                });
                actions.getChildren().add(btnToggle);
            }
        }

        card.getChildren().addAll(lblId, lblType, lblStatus, actions);

        // CORRECCIÓN 3: Reasignamos el contenedor usando la validación segura
        if (isMaintenance) {
            maintenanceContainer.getChildren().add(card);
        } else {
            availableContainer.getChildren().add(card);
        }
    }

    private void showAddKitDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Kit");
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        ComboBox<String> typeBox = new ComboBox<>();
        SimpleList.Iterator<String> it = appController.getKitTypeLabels().iterador();
        while(it.hasNext()) typeBox.getItems().add(it.Next());
        typeBox.getSelectionModel().selectFirst();

        TextField qtyField = new TextField("1");

        // CORRECCIÓN: Se agrega la fila del Tipo de Kit al GridPane (columna 0 y 1, fila 0)
        grid.add(new Label("Tipo de Kit:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Cantidad:"), 0, 1);    grid.add(qtyField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                try { 
                    appController.registerKit(typeBox.getValue(), Integer.parseInt(qtyField.getText())); 
                } catch (NumberFormatException e) { 
                    new Alert(Alert.AlertType.ERROR, "Cantidad inválida").show(); 
                }
            }
        });
    }

    public VBox getView() { return rootContainer; }
}