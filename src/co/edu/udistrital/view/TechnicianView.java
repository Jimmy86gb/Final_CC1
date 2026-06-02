package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Vista para la gestión de Técnicos.
 * Permite registrar nuevos técnicos y actualizar sus zonas, especialidades o estados.
 *
 * @author Jimmy Alejandro Granados Becerra
 */
public class TechnicianView {
    private final Tab tab;
    private final AppController controller;
    private TableView<TechnicianDTO> table;

    public TechnicianView(AppController controller) {
        this.controller = controller;
        this.tab = new Tab("Técnicos");
        this.tab.setClosable(false);
        this.tab.setContent(buildContent());
    }

    public Tab getTab() { return tab; }

    private VBox buildContent() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));

        // --- FORMULARIO ---
        HBox form = new HBox(10);
        TextField txtName = new TextField(); txtName.setPromptText("Nombre del Técnico");
        
        ComboBox<String> cmbSpecialty = new ComboBox<>();
        cmbSpecialty.setPromptText("Especialidad");
        fillCombo(cmbSpecialty, controller.getTechSpecialityLabelsUseCase.execute().iterador());

        ComboBox<String> cmbZone = new ComboBox<>();
        cmbZone.setPromptText("Zona");
        fillCombo(cmbZone, controller.getZoneLabelsUseCase.execute().iterador());

        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.setPromptText("Estado (Solo Actualizar)");
        fillCombo(cmbStatus, controller.getTechStatusLabelsUseCase.execute().iterador());

        Button btnRegister = new Button("Registrar Nuevo");
        Button btnUpdate = new Button("Actualizar Seleccionado");

        btnRegister.setOnAction(e -> {
            ResponseDTO res = controller.registerTechnicianUseCase.execute(txtName.getText(), cmbSpecialty.getValue(), cmbZone.getValue());
            showAlert(res.isSuccess(), res.getMessage());
            refreshTable();
        });

        btnUpdate.setOnAction(e -> {
            TechnicianDTO selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(false, "Seleccione un técnico de la tabla para actualizar.");
                return;
            }
            ResponseDTO res = controller.updateTechnicianUseCase.execute(
                selected.getId().toString(), txtName.getText(), cmbSpecialty.getValue(), cmbZone.getValue(), cmbStatus.getValue()
            );
            showAlert(res.isSuccess(), res.getMessage());
            refreshTable();
        });

        form.getChildren().addAll(txtName, cmbSpecialty, cmbZone, cmbStatus, btnRegister, btnUpdate);

        // --- TABLA ---
        table = new TableView<>();
        TableColumn<TechnicianDTO, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId().toString()));
        TableColumn<TechnicianDTO, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        TableColumn<TechnicianDTO, String> colSpec = new TableColumn<>("Especialidad");
        colSpec.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSpecialty()));
        TableColumn<TechnicianDTO, String> colZone = new TableColumn<>("Zona");
        colZone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getZone()));
        TableColumn<TechnicianDTO, String> colStatus = new TableColumn<>("Estado");
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));

        table.getColumns().addAll(colId, colName, colSpec, colZone, colStatus);
        
        // Listener para llenar el formulario al hacer clic en la tabla
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtName.setText(newSelection.getName());
                cmbSpecialty.setValue(newSelection.getSpecialty());
                cmbZone.setValue(newSelection.getZone());
                cmbStatus.setValue(newSelection.getStatus());
            }
        });

        refreshTable();
        box.getChildren().addAll(new Label("Gestión Operativa de Técnicos"), form, table);
        return box;
    }

    private void fillCombo(ComboBox<String> combo, Iterator<String> it) {
        while(it.hasNext()) combo.getItems().add(it.Next());
    }

    private void refreshTable() {
        SimpleList<TechnicianDTO> data = controller.getTechniciansUseCase.execute();
        ObservableList<TechnicianDTO> obs = FXCollections.observableArrayList();
        Iterator<TechnicianDTO> it = data.iterador();
        while(it.hasNext()) obs.add(it.Next());
        table.setItems(obs);
    }

    private void showAlert(boolean success, String msg) {
        Alert a = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }
}