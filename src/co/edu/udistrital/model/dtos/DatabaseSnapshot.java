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


public class DatabaseSnapshot implements Serializable {

	
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

	
	public DatabaseSnapshot() {
	}

	
	public SimpleList<Profile> getUsersList() {
		return usersList;
	}

	
	public void setUsersList(SimpleList<Profile> usersList) {
		this.usersList = usersList;
	}

	
	public SimpleList<Client> getClientList() {
		return clientList;
	}

	
	public void setClientList(SimpleList<Client> clientList) {
		this.clientList = clientList;
	}

	
	public SimpleList<Technician> getTechnicianList() {
		return technicianList;
	}

	
	public void setTechnicianList(SimpleList<Technician> technicianList) {
		this.technicianList = technicianList;
	}

	
	public SimpleList<ServiceUnit> getUnitList() {
		return unitList;
	}

	
	public void setUnitList(SimpleList<ServiceUnit> unitList) {
		this.unitList = unitList;
	}

	
	public Stack<ServiceUnit> getToConfirmUnitStack() {
		return toConfirmUnitStack;
	}

	
	public void setToConfirmUnitStack(Stack<ServiceUnit> toConfirmUnitStack) {
		this.toConfirmUnitStack = toConfirmUnitStack;
	}

	
	public SimpleList<Kit> getKitList() {
		return kitList;
	}

	
	public void setKitList(SimpleList<Kit> kitList) {
		this.kitList = kitList;
	}

	
	public Stack<Kit> getMaintenanceKitStack() {
		return maintenanceKitStack;
	}

	
	public void setMaintenanceKitStack(Stack<Kit> maintenanceKitStack) {
		this.maintenanceKitStack = maintenanceKitStack;
	}

	
	public Queue<Report> getUndoQueue() {
		return undoQueue;
	}

	
	public void setUndoQueue(Queue<Report> undoQueue) {
		this.undoQueue = undoQueue;
	}

	
	public Queue<Report> getHighPriorityQueue() {
		return highPriorityQueue;
	}

	
	public void setHighPriorityQueue(Queue<Report> highPriorityQueue) {
		this.highPriorityQueue = highPriorityQueue;
	}

	
	public Queue<Report> getMediumPriorityQueue() {
		return mediumPriorityQueue;
	}

	
	public void setMediumPriorityQueue(Queue<Report> mediumPriorityQueue) {
		this.mediumPriorityQueue = mediumPriorityQueue;
	}

	
	public Queue<Report> getLowPriorityQueue() {
		return lowPriorityQueue;
	}

	
	public void setLowPriorityQueue(Queue<Report> lowPriorityQueue) {
		this.lowPriorityQueue = lowPriorityQueue;
	}

	
	public Stack<Report> getOnGoingReportStack() {
		return onGoingReportStack;
	}

	
	public void setOnGoingReportStack(Stack<Report> onGoingReportStack) {
		this.onGoingReportStack = onGoingReportStack;
	}

	
	public Stack<Report> getToConfirmReportStack() {
		return toConfirmReportStack;
	}

	
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