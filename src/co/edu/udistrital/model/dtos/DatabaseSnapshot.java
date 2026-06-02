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

//Completar java doc
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
	private Stack<Kit> maintenanceKitStack;
	private Queue<Report> undoQueue;
	private Queue<Report> highPriorityQueue;
	private Queue<Report> mediumPriorityQueue;
	private Queue<Report> lowPriorityQueue;
	private Stack<Report> onGoingReportStack;
	private Stack<Report> toConfirmReportStack;

	public DatabaseSnapshot() {
	}

	/**
	 * @return Retorna la lista de usuarios guadada en el archivo
	 */
	public SimpleList<Profile> getUsersList() {
		return usersList;
	}

	/**
	 * @param usersList
	 */
	public void setUsersList(SimpleList<Profile> usersList) {
		this.usersList = usersList;
	}

	/**
	 *
	 * @return
	 */
	public SimpleList<Client> getClientList() {
		return clientList;
	}

	/**
	 *
	 * @param clientList
	 */
	public void setClientList(SimpleList<Client> clientList) {
		this.clientList = clientList;
	}

	/**
	 *
	 * @return
	 */
	public SimpleList<Technician> getTechnicianList() {
		return technicianList;
	}

	/**
	 *
	 * @param technicianList
	 */
	public void setTechnicianList(SimpleList<Technician> technicianList) {
		this.technicianList = technicianList;
	}

	/**
	 *
	 * @return
	 */
	public SimpleList<ServiceUnit> getUnitList() {
		return unitList;
	}

	/**
	 *
	 * @param unitList
	 */
	public void setUnitList(SimpleList<ServiceUnit> unitList) {
		this.unitList = unitList;
	}

	/**
	 *
	 * @return
	 */
	public Stack<ServiceUnit> getToConfirmUnitStack() {
		return toConfirmUnitStack;
	}

	/**
	 *
	 * @param toConfirmUnitStack
	 */
	public void setToConfirmUnitStack(Stack<ServiceUnit> toConfirmUnitStack) {
		this.toConfirmUnitStack = toConfirmUnitStack;
	}

	/**
	 *
	 * @return
	 */
	public SimpleList<Kit> getKitList() {
		return kitList;
	}

	/**
	 *
	 * @param kitList
	 */
	public void setKitList(SimpleList<Kit> kitList) {
		this.kitList = kitList;
	}

	/**
	 *
	 * @return
	 */
	public Stack<Kit> getMaintenanceKitStack() {
		return maintenanceKitStack;
	}

	/**
	 *
	 * @param maintenanceKitStack
	 */
	public void setMaintenanceKitStack(Stack<Kit> maintenanceKitStack) {
		this.maintenanceKitStack = maintenanceKitStack;
	}

	/**
	 *
	 * @return
	 */
	public Queue<Report> getUndoQueue() {
		return undoQueue;
	}

	/**
	 *
	 * @param undoQueue
	 */
	public void setUndoQueue(Queue<Report> undoQueue) {
		this.undoQueue = undoQueue;
	}

	/**
	 *
	 * @return
	 */
	public Queue<Report> getHighPriorityQueue() {
		return highPriorityQueue;
	}

	/**
	 *
	 * @param highPriorityQueue
	 */
	public void setHighPriorityQueue(Queue<Report> highPriorityQueue) {
		this.highPriorityQueue = highPriorityQueue;
	}

	/**
	 *
	 * @return
	 */
	public Queue<Report> getMediumPriorityQueue() {
		return mediumPriorityQueue;
	}

	/**
	 *
	 * @param mediumPriorityQueue
	 */
	public void setMediumPriorityQueue(Queue<Report> mediumPriorityQueue) {
		this.mediumPriorityQueue = mediumPriorityQueue;
	}

	/**
	 *
	 * @return
	 */
	public Queue<Report> getLowPriorityQueue() {
		return lowPriorityQueue;
	}

	/**
	 *
	 * @param lowPriorityQueue
	 */
	public void setLowPriorityQueue(Queue<Report> lowPriorityQueue) {
		this.lowPriorityQueue = lowPriorityQueue;
	}

	/**
	 *
	 * @return
	 */
	public Stack<Report> getOnGoingReportStack() {
		return onGoingReportStack;
	}

	/**
	 *
	 * @param onGoingReportStack
	 */
	public void setOnGoingReportStack(Stack<Report> onGoingReportStack) {
		this.onGoingReportStack = onGoingReportStack;
	}

	/**
	 *
	 * @return
	 */
	public Stack<Report> getToConfirmReportStack() {
		return toConfirmReportStack;
	}

	/**
	 *
	 * @param toConfirmReportStack
	 */
	public void setToConfirmReportStack(Stack<Report> toConfirmReportStack) {
		this.toConfirmReportStack = toConfirmReportStack;
	}
}
