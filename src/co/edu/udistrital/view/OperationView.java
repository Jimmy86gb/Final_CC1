package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OperationView {
    private final Tab tab;
    private final AppController controller;
    private TableView<ReportDTO> tableOnGoing;

    public OperationView(AppController controller) {
        this.controller = controller;
        this.tab = new Tab("Despacho y Operación");
        this.tab.setClosable(false);
        this.tab.setContent(buildContent());
    }

    public Tab getTab() { return tab; }

    private VBox buildContent() {
        VBox box = new VBox(20);
        
        // 1. REGISTRO DE SINIESTROS
        HBox regBox = new HBox(10);
        TextField txtClientId = new TextField(); txtClientId.setPromptText("ID Cliente");
        TextField txtDesc = new TextField(); txtDesc.setPromptText("Descripción");
        ComboBox<String> cmbCrit = new ComboBox<>(); fillCombo(cmbCrit, controller.getCriticLevelLabelsUseCase.execute().iterador());
        ComboBox<String> cmbZone = new ComboBox<>(); fillCombo(cmbZone, controller.getZoneLabelsUseCase.execute().iterador());
        ComboBox<String> cmbProb = new ComboBox<>(); fillCombo(cmbProb, controller.getTechSpecialityLabelsUseCase.execute().iterador());
        
        Button btnReg = new Button("Reportar Siniestro");
        btnReg.setOnAction(e -> {
            controller.registerReportUseCase.execute(txtClientId.getText(), txtDesc.getText(), cmbProb.getValue(), cmbCrit.getValue(), cmbZone.getValue());
            refreshAll();
        });
        regBox.getChildren().addAll(new Label("Nuevo:"), txtClientId, txtDesc, cmbProb, cmbCrit, cmbZone, btnReg);

        // 2. DESPACHO (Asignar Recursos)
        VBox dispatchBox = new VBox(10);
        Label lblNext = new Label("Siguiente Emergencia en Cola: Ninguna");
        ComboBox<String> cmbTech = new ComboBox<>();
        ComboBox<String> cmbUnit = new ComboBox<>();
        ComboBox<String> cmbKit = new ComboBox<>();
        
        Button btnAssign = new Button("Despachar");
        btnAssign.setOnAction(e -> {
            controller.assignResourcesUseCase.execute(cmbTech.getValue(), cmbUnit.getValue(), cmbKit.getValue());
            refreshAll();
        });
        dispatchBox.getChildren().addAll(lblNext, new HBox(10, new Label("Técnico ID:"), cmbTech, new Label("Unidad ID:"), cmbUnit, new Label("Kit ID:"), cmbKit, btnAssign));

        // 3. ON-GOING (En progreso LIFO)
        tableOnGoing = new TableView<>();
        TableColumn<ReportDTO, String> colId = new TableColumn<>("Siniestro ID"); colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTicketID().toString()));
        TableColumn<ReportDTO, Void> colActions = new TableColumn<>("Acciones LIFO");
        colActions.setCellFactory(p -> new TableCell<>() {
            Button btnUndo = new Button("Deshacer");
            Button btnFinish = new Button("Finalizar");
            {
                btnUndo.setOnAction(e -> { controller.undoReportUseCase.execute(); refreshAll(); });
                btnFinish.setOnAction(e -> { 
                    controller.finishReportUseCase.execute(getTableView().getItems().get(getIndex()).getTicketID().toString()); 
                    refreshAll(); 
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    ReportDTO r = getTableView().getItems().get(getIndex());
                    HBox actionBox = new HBox(5);
                    if(r.isCanUndo()) actionBox.getChildren().add(btnUndo); // Solo true para el Top de la pila
                    if(r.isCanFinish()) actionBox.getChildren().add(btnFinish);
                    setGraphic(actionBox);
                }
            }
        });
        tableOnGoing.getColumns().addAll(colId, colActions);

        box.getChildren().addAll(regBox, new Separator(), dispatchBox, new Separator(), new Label("Operaciones Activas (LIFO):"), tableOnGoing);
        refreshAll();
        return box;
    }

    private void fillCombo(ComboBox<String> combo, Iterator<String> iterator) {
        while(iterator.hasNext()) combo.getItems().add(iterator.Next());
    }

    private void refreshAll() {
        ObservableList<ReportDTO> obs = FXCollections.observableArrayList();
        Iterator<ReportDTO> it = controller.getOnGoingReportsUseCase.execute().iterador();
        while(it.hasNext()) obs.add(it.Next());
        tableOnGoing.setItems(obs);
    }
}