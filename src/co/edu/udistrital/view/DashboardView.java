package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ResponseDTO;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class DashboardView {
    private final BorderPane root;
    private final AppController controller;

    public DashboardView(AppController controller, String role) {
        this.controller = controller;
        this.root = new BorderPane();

        TabPane tabPane = new TabPane();

        // ADMINISTRADOR (Supervisor)
        if ("ADMIN".equals(role)) {
            tabPane.getTabs().add(new AuditView(controller).getTab());
            tabPane.getTabs().add(new ClientView(controller).getTab());
            tabPane.getTabs().add(new TechnicianView(controller).getTab());
            tabPane.getTabs().add(new ServiceUnitView(controller).getTab());
            tabPane.getTabs().add(new KitView(controller).getTab());
            
            // Botón de exportación superior
            Button btnExport = new Button("Exportar CSV del Día");
            btnExport.setOnAction(e -> {
                ResponseDTO res = controller.generateDailyCSVUseCase.execute(System.getProperty("user.home"));
                showAlert(res.isSuccess(), res.getMessage());
            });
            HBox topBar = new HBox(btnExport);
            topBar.setStyle("-fx-padding: 10px; -fx-alignment: center-right;");
            root.setTop(topBar);
        } 
        // OPERARIO (Despachador)
        else {
            tabPane.getTabs().add(new OperationView(controller).getTab());
        }

        root.setCenter(tabPane);
    }

    public BorderPane getView() { return root; }

    private void showAlert(boolean success, String msg) {
        Alert a = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.show();
    }
}