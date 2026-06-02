package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.dtos.ResponseDTO;
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
 * Vista para la gestión de Kits.
 * Implementa el manejo de inventario y la pila LIFO estricta para mantenimiento.
 *
 * @author Jimmy Alejandro Granados Becerra
 */
public class KitView {
    private final Tab tab;
    private final AppController controller;
    private TableView<KitDTO> tableGeneral;
    private TableView<KitDTO> tableMaintenance;

    public KitView(AppController controller) {
        this.controller = controller;
        this.tab = new Tab("Gestión de Kits");
        this.tab.setClosable(false);
        this.tab.setContent(buildContent());
    }

    public Tab getTab() { return tab; }

    private ScrollPane buildContent() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(15));

        // === SECCIÓN 1: REGISTRO ===
        HBox form = new HBox(10);
        ComboBox<String> cmbType = new ComboBox<>();
        cmbType.setPromptText("Tipo de Kit");
        fillCombo(cmbType, controller.getKitTypeLabelsUseCase.execute().iterador());

        TextField txtQty = new TextField();
        txtQty.setPromptText("Cantidad");

        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.setPromptText("Estado (Actualización)");
        fillCombo(cmbStatus, controller.getKitStatusLabelsUseCase.execute().iterador());

        Button btnReg = new Button("Registrar Lote");
        Button btnUpdate = new Button("Actualizar Kit");

        btnReg.setOnAction(e -> {
            try {
                int qty = Integer.parseInt(txtQty.getText());
                ResponseDTO res = controller.registerKitUseCase.execute(cmbType.getValue(), qty);
                showAlert(res.isSuccess(), res.getMessage());
                refreshAll();
            } catch (NumberFormatException ex) {
                showAlert(false, "La cantidad debe ser numérica.");
            }
        });

        btnUpdate.setOnAction(e -> {
            KitDTO selected = tableGeneral.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(false, "Seleccione un Kit.");
                return;
            }
            ResponseDTO res = controller.updateKitUseCase.execute(selected.getId().toString(), cmbType.getValue(), cmbStatus.getValue());
            showAlert(res.isSuccess(), res.getMessage());
            refreshAll();
        });

        form.getChildren().addAll(cmbType, txtQty, btnReg, new Separator(), cmbStatus, btnUpdate);

        tableGeneral = new TableView<>();
        TableColumn<KitDTO, String> colId = new TableColumn<>("ID"); colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId().toString()));
        TableColumn<KitDTO, String> colType = new TableColumn<>("Tipo"); colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
        TableColumn<KitDTO, String> colStatus = new TableColumn<>("Estado"); colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        tableGeneral.getColumns().addAll(colId, colType, colStatus);
        
        tableGeneral.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                cmbType.setValue(n.getType());
                cmbStatus.setValue(n.getStatus());
            }
        });

        // === SECCIÓN 2: ESTANTERÍA DE REVISIÓN LIFO ===
        Label lblMaint = new Label("Estantería de Revisión / Mantenimiento (LIFO):");
        lblMaint.setStyle("-fx-font-weight: bold;");

        tableMaintenance = new TableView<>();
        TableColumn<KitDTO, String> mColId = new TableColumn<>("ID Kit"); mColId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId().toString()));
        TableColumn<KitDTO, String> mColType = new TableColumn<>("Tipo"); mColType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
        TableColumn<KitDTO, Void> colActions = new TableColumn<>("Acciones LIFO");
        
        colActions.setCellFactory(p -> new TableCell<>() {
            Button btnService = new Button("Devolver a Servicio");
            Button btnRetire = new Button("Dar de Baja");
            {
                btnService.setOnAction(e -> { 
                    ResponseDTO res = controller.returnKitUseCase.execute(); 
                    showAlert(res.isSuccess(), res.getMessage()); refreshAll(); 
                });
                btnRetire.setOnAction(e -> { 
                    ResponseDTO res = controller.retireKitUseCase.execute(); 
                    showAlert(res.isSuccess(), res.getMessage()); refreshAll(); 
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    KitDTO k = getTableView().getItems().get(getIndex());
                    HBox acts = new HBox(5);
                    if(k.isEditable()) acts.getChildren().addAll(btnService, btnRetire); // True solo en tope LIFO
                    setGraphic(acts);
                }
            }
        });

        tableMaintenance.getColumns().addAll(mColId, mColType, colActions);

        box.getChildren().addAll(form, tableGeneral, new Separator(), lblMaint, tableMaintenance);
        refreshAll();
        return new ScrollPane(box);
    }

    private void fillCombo(ComboBox<String> combo, Iterator<String> it) {
        while(it.hasNext()) combo.getItems().add(it.Next());
    }

    private void refreshAll() {
        ObservableList<KitDTO> obsGen = FXCollections.observableArrayList();
        Iterator<KitDTO> itGen = controller.getKitsUseCase.execute().iterador();
        while(itGen.hasNext()) obsGen.add(itGen.Next());
        tableGeneral.setItems(obsGen);

        ObservableList<KitDTO> obsMaint = FXCollections.observableArrayList();
        Iterator<KitDTO> itMaint = controller.getMaintenanceKitsUseCase.execute().iterador();
        while(itMaint.hasNext()) obsMaint.add(itMaint.Next());
        tableMaintenance.setItems(obsMaint);
    }

    private void showAlert(boolean success, String msg) {
        Alert a = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }
}