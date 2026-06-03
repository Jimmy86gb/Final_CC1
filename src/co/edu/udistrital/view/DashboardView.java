package co.edu.udistrital.view;

import co.edu.udistrital.controller.DashboardController;
import co.edu.udistrital.model.structures.SimpleList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class DashboardView {
	private VBox rootContainer;
	private String role;
	private DashboardController controller;

	private Label lblActiveUnits;
	private Label lblCriticalCases;
	private Label lblMaintenance;
	private Label lblTotalRequests;
	private ListView<String> logListView;
	private Button btnGlobalUndo;

	
	public DashboardView(String role, DashboardController controller) {
		this.role = role;
		this.controller = controller;
		rootContainer = new VBox(20);
		rootContainer.setPadding(new Insets(20));

		Label lblTitle = new Label("Resumen General");
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

		HBox cardsContainer = new HBox(20);
		lblActiveUnits = new Label("0");
		lblCriticalCases = new Label("0");
		lblMaintenance = new Label("0");
		lblTotalRequests = new Label("0");

		cardsContainer.getChildren().addAll(
				createCard("Unidades Activas", lblActiveUnits, "#10B981"),
				createCard("Casos Críticos", lblCriticalCases, "#EF4444"),
				createCard("En Mantenimiento", lblMaintenance, "#F59E0B"),
				createCard("Total Solicitudes", lblTotalRequests, "#3B82F6")
		);

		HBox actionsContainer = new HBox(15);
		actionsContainer.setAlignment(Pos.CENTER_LEFT);

		
		Button btnGlobalUndo = new Button("↩ Deshacer Última Acción");
		btnGlobalUndo.setStyle("-fx-background-color: #DC2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
		btnGlobalUndo.setOnAction(e -> {
			if (controller != null) {
				controller.undoLastGlobalAction();
			}
		});

		if (role.equals("ADMIN")) {
			Button btnCSV = new Button("Exportar reporte diario (CSV)");
			btnCSV.setStyle("-fx-background-color:#10B981; -fx-text-fill:white; -fx-padding: 10 20; -fx-cursor: hand;");
			btnCSV.setOnAction(e -> {
				if (controller != null) {
					controller.exportDailyReport(); 
				}
			});
			actionsContainer.getChildren().add(btnCSV);
		} else {
			btnGlobalUndo.setVisible(false);
			btnGlobalUndo.setManaged(false); 
		}

		logListView = new ListView<>();
		logListView.setPrefHeight(220);
		logListView.setMaxWidth(Double.MAX_VALUE);

		VBox logPanel = new VBox(12);
		logPanel.setPadding(new Insets(20));
		logPanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");
		
		Label lblLogTitle = new Label("Historial de acciones");
		lblLogTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));
		
		logPanel.getChildren().addAll(lblLogTitle, logListView, btnGlobalUndo);

		rootContainer.getChildren().addAll(lblTitle, cardsContainer, actionsContainer, logPanel);
	}

	
	public void updateStatistics(String active, String critical, String maint, String total) {
		lblActiveUnits.setText(active);
		lblCriticalCases.setText(critical);
		lblMaintenance.setText(maint);
		lblTotalRequests.setText(total);
	}

	
	public void updateLogPanel(SimpleList<String> logs) {
		logListView.getItems().clear();
		if (logs == null) {
			return;
		}
		SimpleList.Iterator<String> logIterator = logs.iterador();
		while (logIterator.hasNext()) {
			logListView.getItems().add(logIterator.Next());
		}
	}

	
	private VBox createCard(String title, Label lblValue, String hexColor) {
		VBox card = new VBox(10);
		card.setPadding(new Insets(20));
		card.setPrefSize(250, 120);
		card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

		Label lblTitle = new Label(title);
		lblTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
		lblValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
		lblValue.setStyle("-fx-text-fill: " + hexColor + ";");

		card.getChildren().addAll(lblTitle, lblValue);
		return card;
	}

	
	public VBox getView() {
		return rootContainer;
	}
}