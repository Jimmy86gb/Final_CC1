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

public class AuditView {
    private final Tab tab;
    private final AppController controller;
    private TableView<ReportDTO> tableAudits;

    public AuditView(AppController controller) {
        this.controller = controller;
        this.tab = new Tab("Auditoría (Gerencia)");
        this.tab.setClosable(false);
        this.tab.setContent(buildContent());
    }

    public Tab getTab() { return tab; }

    private VBox buildContent() {
        VBox box = new VBox(10);
        
        tableAudits = new TableView<>();
        TableColumn<ReportDTO, String> colTicket = new TableColumn<>("Ticket (Pila Confirmación)");
        colTicket.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTicketID().toString()));
        TableColumn<ReportDTO, String> colStatus = new TableColumn<>("Estado Propuesto");
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getReportStatus()));

        TableColumn<ReportDTO, Void> colActions = new TableColumn<>("Aprobar/Rechazar");
        colActions.setCellFactory(p -> new TableCell<>() {
            Button btnOk = new Button("Aprobar");
            Button btnNo = new Button("Rechazar");
            {
                btnOk.setOnAction(e -> { controller.approveReportUseCase.execute(); refreshTable(); });
                btnNo.setOnAction(e -> { controller.rejectReportUseCase.execute(); refreshTable(); });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    ReportDTO r = getTableView().getItems().get(getIndex());
                    HBox actionBox = new HBox(5);
                    // isCanConfirm viene del DTO solo como 'true' para el Tope de la pila
                    if(r.isCanConfirm()) actionBox.getChildren().addAll(btnOk, btnNo);
                    setGraphic(actionBox);
                }
            }
        });

        tableAudits.getColumns().addAll(colTicket, colStatus, colActions);
        refreshTable();
        box.getChildren().add(tableAudits);
        return box;
    }

    private void refreshTable() {
        ObservableList<ReportDTO> obs = FXCollections.observableArrayList();
        Iterator<ReportDTO> it = controller.getToConfirmReportsUseCase.execute().iterador();
        while(it.hasNext()) obs.add(it.Next());
        tableAudits.setItems(obs);
    }
}