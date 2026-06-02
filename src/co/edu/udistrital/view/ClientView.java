package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.dtos.ClientDTO;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientView {
    private final Tab tab;
    private final AppController controller;
    private TableView<ClientDTO> table;

    public ClientView(AppController controller) {
        this.controller = controller;
        this.tab = new Tab("Clientes");
        this.tab.setClosable(false);
        this.tab.setContent(buildContent());
    }

    public Tab getTab() { return tab; }

    private VBox buildContent() {
        VBox box = new VBox(10);
        TextField txtId = new TextField(); txtId.setPromptText("ID");
        TextField txtName = new TextField(); txtName.setPromptText("Nombre");
        TextField txtContact = new TextField(); txtContact.setPromptText("Contacto");
        ComboBox<String> cmbType = new ComboBox<>();
        
        SimpleList.Iterator<String> itType = controller.getClientTypeLabelsUseCase.execute().iterador();
        while(itType.hasNext()) cmbType.getItems().add(itType.Next());

        Button btnReg = new Button("Registrar / Actualizar");
        btnReg.setOnAction(e -> {
            ResponseDTO res;
            // Intenta actualizar; si falla por no existir, lo registra. (Simplificación UI)
            res = controller.updateClientUseCase.execute(txtId.getText(), txtName.getText(), cmbType.getValue(), txtContact.getText());
            if(!res.isSuccess()) {
                res = controller.registerClientUseCase.ResponseDTO(txtId.getText(), txtName.getText(), cmbType.getValue(), txtContact.getText());
            }
            refreshTable();
        });

        HBox form = new HBox(10, txtId, txtName, cmbType, txtContact, btnReg);
        
        table = new TableView<>();
        TableColumn<ClientDTO, String> cId = new TableColumn<>("ID"); cId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        TableColumn<ClientDTO, String> cName = new TableColumn<>("Nombre"); cName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        TableColumn<ClientDTO, String> cType = new TableColumn<>("Tipo"); cType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
        table.getColumns().addAll(cId, cName, cType);
        
        refreshTable();
        box.getChildren().addAll(form, table);
        return box;
    }

    private void refreshTable() {
        SimpleList<ClientDTO> data = controller.getClientsUseCase.execute();
        ObservableList<ClientDTO> obs = FXCollections.observableArrayList();
        Iterator<ClientDTO> it = data.iterador();
        while(it.hasNext()) obs.add(it.Next());
        table.setItems(obs);
    }
}