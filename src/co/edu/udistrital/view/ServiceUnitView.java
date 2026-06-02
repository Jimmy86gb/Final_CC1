package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.dtos.ServiceUnitDTO;
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
 * Vista para Unidades de Servicio.
 * Integra el CRUD general y el monitor LIFO para auditar cambios de estado.
 *
 * @author Jimmy Alejandro Granados Becerra
 */
public class ServiceUnitView {
    private final Tab tab;
    private final AppController controller;
    private TableView<ServiceUnitDTO> tableGeneral;
    private TableView<ServiceUnitDTO> tableConfirmations;

    public ServiceUnitView(AppController controller) {
        this.controller = controller;
        this.tab = new Tab("Unidades de Servicio");
        this.tab.setClosable(false);
        this.tab.setContent(buildContent());
    }

    public Tab getTab() { return tab; }

    private ScrollPane buildContent() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(15));

        // === SECCIÓN 1: REGISTRO Y GESTIÓN ===
        HBox form = new HBox(10);
        ComboBox<String> cmbType = new ComboBox<>();
        cmbType.setPromptText("Tipo de Unidad");
        fillCombo(cmbType, controller.getUnitTypeLabelsUseCase.execute().iterador());

        ComboBox<String> cmbZone = new ComboBox<>();
        cmbZone.setPromptText("Zona");
        fillCombo(cmbZone, controller.getZoneLabelsUseCase.execute().iterador());

        TextField txtQty = new TextField();
        txtQty.setPromptText("Cantidad a Registrar");

        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.setPromptText("Estado (Para Actualizar)");
        fillCombo(cmbStatus, controller.getUnitStatusLabelsUseCase.execute().iterador());

        Button btnReg = new Button("Registrar Lote");
        Button btnUpdate = new Button("Actualizar Seleccionada");

        btnReg.setOnAction(e -> {
            try {
                int qty = Integer.parseInt(txtQty.getText());
                ResponseDTO res = controller.registerUnitUseCase.execute(cmbType.getValue(), cmbZone.getValue(), qty);
                showAlert(res.isSuccess(), res.getMessage());
                refreshAll();
            } catch (NumberFormatException ex) {
                showAlert(false, "La cantidad debe ser un número entero.");
            }
        });

        btnUpdate.setOnAction(e -> {
            ServiceUnitDTO selected = tableGeneral.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(false, "Seleccione una unidad de la tabla.");
                return;
            }
            ResponseDTO res = controller.updateUnitUseCase.execute(selected.getId().toString(), cmbType.getValue(), cmbStatus.getValue(), cmbZone.getValue());
            showAlert(res.isSuccess(), res.getMessage());
            refreshAll();
        });

        form.getChildren().addAll(cmbType, cmbZone, txtQty, btnReg, new Separator(), cmbStatus, btnUpdate);

        tableGeneral = new TableView<>();
        TableColumn<ServiceUnitDTO, String> colId = new TableColumn<>("ID"); colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId().toString()));
        TableColumn<ServiceUnitDTO, String> colType = new TableColumn<>("Tipo"); colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
        TableColumn<ServiceUnitDTO, String> colZone = new TableColumn<>("Zona"); colZone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getZone()));
        TableColumn<ServiceUnitDTO, String> colStatus = new TableColumn<>("Estado"); colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        tableGeneral.getColumns().addAll(colId, colType, colZone, colStatus);
        
        tableGeneral.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                cmbType.setValue(n.getType());
                cmbZone.setValue(n.getZone());
                cmbStatus.setValue(n.getStatus());
            }
        });

        // === SECCIÓN 2: PILA LIFO DE CONFIRMACIONES ===
        VBox auditBox = new VBox(10);
        Label lblAudit = new Label("Pila de Confirmación de Cambios de Estado (LIFO):");
        lblAudit.setStyle("-fx-font-weight: bold;");

        tableConfirmations = new TableView<>();
        TableColumn<ServiceUnitDTO, String> cColId = new TableColumn<>("ID Unidad"); cColId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId().toString()));
        TableColumn<ServiceUnitDTO, String> cColStatus = new TableColumn<>("Estado Propuesto"); cColStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        TableColumn<ServiceUnitDTO, Void> colActions = new TableColumn<>("Auditoría LIFO");
        
        colActions.setCellFactory(p -> new TableCell<>() {
            Button btnOk = new Button("Aprobar");
            Button btnNo = new Button("Rechazar");
            {
                btnOk.setOnAction(e -> { 
                    ResponseDTO res = controller.approveUnitStatusUseCase.execute(); 
                    showAlert(res.isSuccess(), res.getMessage()); refreshAll(); 
                });
                btnNo.setOnAction(e -> { 
                    ResponseDTO res = controller.rejectUnitStatusUseCase.execute(); 
                    showAlert(res.isSuccess(), res.getMessage()); refreshAll(); 
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    ServiceUnitDTO s = getTableView().getItems().get(getIndex());
                    HBox acts = new HBox(5);
                    if(s.isEditable()) acts.getChildren().addAll(btnOk, btnNo); // Solo Tope LIFO
                    setGraphic(acts);
                }
            }
        });

        tableConfirmations.getColumns().addAll(cColId, cColStatus, colActions);

        box.getChildren().addAll(new Label("Inventario General"), form, tableGeneral, new Separator(), auditBox, lblAudit, tableConfirmations);
        refreshAll();
        return new ScrollPane(box);
    }

    private void fillCombo(ComboBox<String> combo, Iterator<String> it) {
        while(it.hasNext()) combo.getItems().add(it.Next());
    }

    private void refreshAll() {
        // Refrescar tabla general
        ObservableList<ServiceUnitDTO> obsGen = FXCollections.observableArrayList();
        Iterator<ServiceUnitDTO> itGen = controller.getUnitsUseCase.execute().iterador();
        while(itGen.hasNext()) obsGen.add(itGen.Next());
        tableGeneral.setItems(obsGen);

        // Refrescar pila de confirmaciones
        ObservableList<ServiceUnitDTO> obsConf = FXCollections.observableArrayList();
        Iterator<ServiceUnitDTO> itConf = controller.getToConfirmUnitsUseCase.execute().iterador();
        while(itConf.hasNext()) obsConf.add(itConf.Next());
        tableConfirmations.setItems(obsConf);
    }

    private void showAlert(boolean success, String msg) {
        Alert a = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }
}