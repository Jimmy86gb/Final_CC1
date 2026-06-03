package co.edu.udistrital.model.dtos;

import java.io.Serializable;

import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.Profile;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.structures.Queue;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.Stack;

/**
 * Objeto DTO que encapsula todo el estado de la memoria del sistema. Actúa como
 * un "Memento" o "Snapshot" para ser guardado en un solo archivo binario,
 * preservando las referencias de memoria compartidas.
 *
 * @author Juan David Diaz Perez
 */
public class DatabaseSnapshot implements Serializable {

	/**
	 * Identificador de versión para la serialización.
	 */
	private static final long serialVersionUID = 1L;

	private SimpleList<Profile> usersList;
	private SimpleList<Client> clientList;
	private SimpleList<Technician> technicianList;
	private SimpleList<ServiceUnit> unitList;
	private Stack<ServiceUnit> toConfirmUnitStack;
	private SimpleList<Kit> kitList;
	private SimpleList<Report> allHistoricalReports;
	private Stack<Kit> maintenanceKitStack;
	private Queue<Report> undoQueue;
	private Queue<Report> highPriorityQueue;
	private Queue<Report> mediumPriorityQueue;
	private Queue<Report> lowPriorityQueue;
	private Stack<Report> onGoingReportStack;
	private Stack<Report> toConfirmReportStack;

	/**
	 * Constructor vacío utilizado para crear una instancia del snapshot
	 * antes de cargar o asignar la información almacenada.
	 */
	public DatabaseSnapshot() {
	}

	/**
	 * @return Retorna la lista de usuarios guadada en el archivo
	 */
	public SimpleList<Profile> getUsersList() {
		return usersList;
	}

	/**
	 * @param usersList Lista de usuarios a almacenar en el snapshot.
	 */
	public void setUsersList(SimpleList<Profile> usersList) {
		this.usersList = usersList;
	}

	/**
	 * @return Retorna la lista de clientes almacenada en el snapshot.
	 */
	public SimpleList<Client> getClientList() {
		return clientList;
	}

	/**
	 * @param clientList Lista de clientes a almacenar en el snapshot.
	 */
	public void setClientList(SimpleList<Client> clientList) {
		this.clientList = clientList;
	}

	/**
	 * @return Retorna la lista de técnicos almacenada en el snapshot.
	 */
	public SimpleList<Technician> getTechnicianList() {
		return technicianList;
	}

	/**
	 * @param technicianList Lista de técnicos a almacenar en el snapshot.
	 */
	public void setTechnicianList(SimpleList<Technician> technicianList) {
		this.technicianList = technicianList;
	}

	/**
	 * @return Retorna la lista de unidades de servicio almacenada en el snapshot.
	 */
	public SimpleList<ServiceUnit> getUnitList() {
		return unitList;
	}

	/**
	 * @param unitList Lista de unidades de servicio a almacenar en el snapshot.
	 */
	public void setUnitList(SimpleList<ServiceUnit> unitList) {
		this.unitList = unitList;
	}

	/**
	 * @return Retorna la pila de unidades pendientes por confirmar.
	 */
	public Stack<ServiceUnit> getToConfirmUnitStack() {
		return toConfirmUnitStack;
	}

	/**
	 * @param toConfirmUnitStack Pila de unidades pendientes por confirmar.
	 */
	public void setToConfirmUnitStack(Stack<ServiceUnit> toConfirmUnitStack) {
		this.toConfirmUnitStack = toConfirmUnitStack;
	}

	/**
	 * @return Retorna la lista de kits almacenada en el snapshot.
	 */
	public SimpleList<Kit> getKitList() {
		return kitList;
	}

	/**
	 * @param kitList Lista de kits a almacenar en el snapshot.
	 */
	public void setKitList(SimpleList<Kit> kitList) {
		this.kitList = kitList;
	}

	/**
	 * @return Retorna la pila de kits en mantenimiento.
	 */
	public Stack<Kit> getMaintenanceKitStack() {
		return maintenanceKitStack;
	}

	/**
	 * @param maintenanceKitStack Pila de kits en mantenimiento.
	 */
	public void setMaintenanceKitStack(Stack<Kit> maintenanceKitStack) {
		this.maintenanceKitStack = maintenanceKitStack;
	}

	/**
	 * @return Retorna la cola utilizada para la funcionalidad de deshacer.
	 */
	public Queue<Report> getUndoQueue() {
		return undoQueue;
	}

	/**
	 * @param undoQueue Cola utilizada para la funcionalidad de deshacer.
	 */
	public void setUndoQueue(Queue<Report> undoQueue) {
		this.undoQueue = undoQueue;
	}

	/**
	 * @return Retorna la cola de reportes de prioridad alta.
	 */
	public Queue<Report> getHighPriorityQueue() {
		return highPriorityQueue;
	}

	/**
	 * @param highPriorityQueue Cola de reportes de prioridad alta.
	 */
	public void setHighPriorityQueue(Queue<Report> highPriorityQueue) {
		this.highPriorityQueue = highPriorityQueue;
	}

	/**
	 * @return Retorna la cola de reportes de prioridad media.
	 */
	public Queue<Report> getMediumPriorityQueue() {
		return mediumPriorityQueue;
	}

	/**
	 * @param mediumPriorityQueue Cola de reportes de prioridad media.
	 */
	public void setMediumPriorityQueue(Queue<Report> mediumPriorityQueue) {
		this.mediumPriorityQueue = mediumPriorityQueue;
	}

	/**
	 * @return Retorna la cola de reportes de prioridad baja.
	 */
	public Queue<Report> getLowPriorityQueue() {
		return lowPriorityQueue;
	}

	/**
	 * @param lowPriorityQueue Cola de reportes de prioridad baja.
	 */
	public void setLowPriorityQueue(Queue<Report> lowPriorityQueue) {
		this.lowPriorityQueue = lowPriorityQueue;
	}

	/**
	 * @return Retorna la pila de reportes que se encuentran en atención.
	 */
	public Stack<Report> getOnGoingReportStack() {
		return onGoingReportStack;
	}

	/**
	 * @param onGoingReportStack Pila de reportes que se encuentran en atención.
	 */
	public void setOnGoingReportStack(Stack<Report> onGoingReportStack) {
		this.onGoingReportStack = onGoingReportStack;
	}

	/**
	 * @return Retorna la pila de reportes pendientes por confirmar.
	 */
	public Stack<Report> getToConfirmReportStack() {
		return toConfirmReportStack;
	}

	/**
	 * @param toConfirmReportStack Pila de reportes pendientes por confirmar.
	 */
	public void setToConfirmReportStack(Stack<Report> toConfirmReportStack) {
		this.toConfirmReportStack = toConfirmReportStack;
	}
	
	public SimpleList<Report> getAllHistoricalReports() {
		return allHistoricalReports;
	}

	public void setAllHistoricalReports(SimpleList<Report> allHistoricalReports) {
		this.allHistoricalReports = allHistoricalReports;
	}
}