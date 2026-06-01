package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.structures.Queue;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.structures.Stack;

/**
 * Clase que representa la memoria y administracion referente a los reportes en
 * el sistema
 *
 * @author Juan David Diaz Perez
 */
public class ReportRepository {

	/**
	 * Cola de la mas alta prioridad para los reportes sacados de la stack ongoing
	 */
	private final Queue<Report> undoQueue = new Queue<Report>();

	/**
	 * Cola de alta prioridad para alto impacto en transito o gravedad del accidente
	 */
	private final Queue<Report> highPriorityQueue = new Queue<Report>();

	/**
	 * Cola de prioridad media para clientes empresariales
	 */
	private final Queue<Report> mediumPriorityQueue = new Queue<Report>();

	/**
	 * Cola de baja prioridad para personas comunes
	 */
	private final Queue<Report> lowPriorityQueue = new Queue<Report>();

	/**
	 * Pila de reportes en proceso de ser completados
	 */
	private final Stack<Report> onGoingReportStack = new Stack<Report>();

	/**
	 * Pila de reportes para confirmar si se cerro un caso
	 */
	private final Stack<Report> toConfirmStack = new Stack<Report>();

	/**
	 * Lista de todos los reportes sin importar su estado
	 */
	private final SimpleList<Report> allHistoricalReports = new SimpleList<Report>();

	/**
	 * Metodo para guardar un nuevo reporte
	 * 
	 * @param report Objeto de reporte
	 * @return Si la operacion fue exitosa
	 */
	public boolean saveReport(Report report) {
		if (allHistoricalReports.contains(report)) {
			return false;
		}

		allHistoricalReports.add(report);

		switch (report.getPriority()) {
		case HIGH -> highPriorityQueue.enqueue(report);
		case MEDIUM -> mediumPriorityQueue.enqueue(report);
		case LOW -> lowPriorityQueue.enqueue(report);
		}

		return true;
	}

	/**
	 * Metodo para actualizar un reporte a lo largo del proceso
	 * 
	 * @param actualReport Objeto con la informacion actual de reporte
	 * @param newReport    Objeto con la informacion nueca de reporte
	 * @return Si la operacion fue exitosa
	 */
	public boolean updateReport(Report actualReport, Report newReport) {
		return allHistoricalReports.update(actualReport, newReport);
	}

	/**
	 * Metodo que retorna todos los reportes registrados en el sistema
	 * 
	 * @return La lista de todos los reportes
	 */
	public SimpleList<Report> getAllReports() {
		SimpleList<Report> copyList = new SimpleList<>();
		Iterator<Report> iterator = allHistoricalReports.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	/**
	 * Metodo que filtra todos los reportes por estado y nivel critico
	 * 
	 * @param criticLevel  EL nivel critico a filtrar
	 * @param reportStatus El estado a filtrar
	 * @return La lista filtrada
	 */
	public SimpleList<Report> getReportsByCriticLevelAndStatus(CriticLevel criticLevel, ReportStatus reportStatus) {
		SimpleList<Report> filteredList = new SimpleList<>();
		Iterator<Report> iterator = allHistoricalReports.iterador();

		while (iterator.hasNext()) {
			Report actualReport = iterator.Next();

			if (actualReport.getPriority() == criticLevel && actualReport.getStatus() == reportStatus) {
				filteredList.add(actualReport);
			}
		}
		return filteredList;
	}

	/**
	 * Metodo que añade un nuevo reporte a los que estan en progresa
	 * 
	 * @param report Reporte a poner en progreso
	 */
	public void pushToOnProgress(Report report) {
		onGoingReportStack.push(report);
	}

	/**
	 * Metodo que saca el ultimo reporte dentro de los que estan en progreso
	 * 
	 * @return Reporte fuera de la pila
	 */
	public Report popOnProgress() {
		return onGoingReportStack.pop();
	}

	/**
	 * Metodo que muestra todos los reportes que estan siendo tratados en el momento
	 * 
	 * @return La lista de todos los reportes tratados en el momento
	 */
	public SimpleList<Report> getOnGoingReports() {

		SimpleList<Report> copyList = new SimpleList<>();
		Stack<Report> tempStack = new Stack<>();

		while (!onGoingReportStack.isEmpty()) {
			Report currenReport = onGoingReportStack.pop();

			copyList.add(currenReport);

			tempStack.push(currenReport);
		}

		while (!tempStack.isEmpty()) {
			onGoingReportStack.push(tempStack.pop());
		}

		return copyList;
	}

	/**
	 * Metodo que añade un nuevo reporte a los que se van a confirmar para cerrar
	 * 
	 * @param report Reporte a poner en progreso
	 */
	public void pushToConfirm(Report report) {
		toConfirmStack.push(report);
	}

	/**
	 * Metodo que saca el ultimo reporte de la pila de confirmaciones de acciones
	 * 
	 * @return Reporte fuera de la pila
	 */
	public Report popOnConfirm() {
		return toConfirmStack.pop();
	}

	/**
	 * Metodo que muestra todos los reportes a confirmar su cierre
	 * 
	 * @return La lista de todos los reportes tratados en el momento
	 */
	public SimpleList<Report> getToConfirmReports() {

		SimpleList<Report> copyList = new SimpleList<>();
		Stack<Report> tempStack = new Stack<>();

		while (!toConfirmStack.isEmpty()) {
			Report currenReport = toConfirmStack.pop();

			copyList.add(currenReport);

			tempStack.push(currenReport);
		}

		while (!tempStack.isEmpty()) {
			toConfirmStack.push(tempStack.pop());
		}

		return copyList;
	}

	/**
	 * Metodo que añade a los pendientes los reportes desechos de la pila
	 * 
	 * @param report El reporte que salio de la pila
	 */
	public void registerRevertedReport(Report report) {
		undoQueue.enqueue(report);
	}

	/**
	 * Metodo que retorna el siguiente reporte a procesar segun el orden de
	 * prioridades
	 * 
	 * @return El siguiente reporte
	 */
	public Report getNextPendingReport() {
		Report nextReport = undoQueue.dequeue();
		if (nextReport != null) {
			return nextReport;
		}

		nextReport = highPriorityQueue.dequeue();
		if (nextReport != null) {
			return nextReport;
		}

		nextReport = mediumPriorityQueue.dequeue();
		if (nextReport != null) {
			return nextReport;
		}

		return lowPriorityQueue.dequeue();
	}

	/**
	 * Metodo que remueve un reporte especifico de la pila de progreso cuando este
	 * ha finalizado, sin alterar el orden del resto.
	 * 
	 * @param finishedReport El reporte que ya se completó
	 */
	public void removeFinishedReportFromStack(Report finishedReport) {
		Stack<Report> tempStack = new Stack<>();
		boolean removed = false;

		while (!onGoingReportStack.isEmpty()) {
			Report currentReport = onGoingReportStack.pop();

			if (currentReport.equals(finishedReport) && !removed) {
				removed = true;
			} else {
				tempStack.push(currentReport);
			}
		}

		// 2. Restaurar la pila original
		while (!tempStack.isEmpty()) {
			onGoingReportStack.push(tempStack.pop());
		}
	}
}
