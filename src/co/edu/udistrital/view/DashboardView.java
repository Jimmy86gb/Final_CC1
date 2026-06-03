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

/**
 * Clase encargada de renderizar el panel de control principal o "Dashboard".
 * Proporciona una vista de alto nivel del estado del sistema mediante tarjetas 
 * de resumen (KPIs) y agrupa las acciones globales como la exportacion de reportes.
 * Su diseño es netamente visual, delegando toda la logica de calculo al controlador.
 * * @author Jimmy86gb
 */
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

	/**
	 * Constructor de la vista del Dashboard.
	 * Ensambla los contenedores principales, las tarjetas numericas y, dependiendo 
	 * del rol del usuario, despliega las herramientas de administracion globales.
	 * * @param role Perfil de seguridad del usuario autenticado (determina la visibilidad de botones).
	 * @param controller Enlace directo al sub-controlador del dashboard.
	 */
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

	/**
	 * Actualiza los contadores visuales del panel de control.
	 * Este metodo es llamado por el controlador una vez se han tabulado 
	 * y calculado las metricas correspondientes en la logica de negocio.
	 * * @param active Cantidad en texto de unidades en terreno disponibles o ocupadas.
	 * @param critical Cantidad en texto de siniestros de prioridad alta.
	 * @param maint Cantidad en texto de kits y unidades en estado de reparacion.
	 * @param total Total historico o diario de solicitudes procesadas.
	 */
	public void updateStatistics(String active, String critical, String maint, String total) {
		lblActiveUnits.setText(active);
		lblCriticalCases.setText(critical);
		lblMaintenance.setText(maint);
		lblTotalRequests.setText(total);
	}

	/**
	 * Actualiza el ListView del historial de acciones con los registros recientes.
	 * Recorre la estructura SimpleList usando su iterador y agrega cada descripcion.
	 * @param logs Estructura SimpleList con las descripciones de las acciones.
	 */
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

	/**
	 * Metodo de factoria visual (Factory Method) para construir tarjetas estandarizadas.
	 * Aplica estilos consistentes (sombras, bordes, tipografias) a los componentes 
	 * para unificar la presentacion de los indicadores de desempeño.
	 * * @param title Titulo descriptivo de la metrica.
	 * @param lblValue Referencia en memoria al Label que contendra el numero dinamico.
	 * @param hexColor Codigo de color en hexadecimal para resaltar el valor numerico.
	 * @return Un contenedor VBox completamente estilizado en formato de tarjeta.
	 */
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

	/**
	 * Retorna el contenedor raiz de esta vista para ser acoplado en la interfaz principal.
	 * * @return Objeto VBox principal de la pantalla de resumen.
	 */
	public VBox getView() {
		return rootContainer;
	}
}