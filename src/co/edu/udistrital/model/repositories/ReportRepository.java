package co.edu.udistrital.model.repositories;

import java.util.UUID;

import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.structures.Queue;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.structures.Stack;



public class ReportRepository {

	
	private Queue<Report> undoQueue = new Queue<Report>();

	
	private Queue<Report> highPriorityQueue = new Queue<Report>();

	
	private Queue<Report> mediumPriorityQueue = new Queue<Report>();

	
	private Queue<Report> lowPriorityQueue = new Queue<Report>();

	
	private Stack<Report> onGoingReportStack = new Stack<Report>();

	
	private Stack<Report> toConfirmStack = new Stack<Report>();

	
	private SimpleList<Report> allHistoricalReports = new SimpleList<Report>();

	
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

	
	public boolean updateReport(Report actualReport, Report newReport) {
		return allHistoricalReports.update(actualReport, newReport);
	}

	
	public SimpleList<Report> getAllReports() {
		SimpleList<Report> copyList = new SimpleList<>();
		Iterator<Report> iterator = allHistoricalReports.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	
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

	
	public void pushToOnProgress(Report report) {
		onGoingReportStack.push(report);
	}

	
	public Report popOnProgress() {
		return onGoingReportStack.pop();
	}

	
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

	
	public void pushToConfirm(Report report) {
		toConfirmStack.push(report);
	}

	
	public Report popOnConfirm() {
		return toConfirmStack.pop();
	}

	
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

	
	public void registerRevertedReport(Report report) {
		undoQueue.enqueue(report);
	}

	
	public void registerReport(Report report) {
		switch (report.getPriority()) {
		case HIGH -> highPriorityQueue.enqueue(report);
		case MEDIUM -> mediumPriorityQueue.enqueue(report);
		case LOW -> lowPriorityQueue.enqueue(report);
		}
	}

	
	public Report peekNextPendingReport() {

		if (!undoQueue.isEmpty()) {
			return undoQueue.peek();
		}
		if (!highPriorityQueue.isEmpty()) {
			return highPriorityQueue.peek();
		}
		if (!mediumPriorityQueue.isEmpty()) {
			return mediumPriorityQueue.peek();
		}
		if (!lowPriorityQueue.isEmpty()) {
			return lowPriorityQueue.peek();
		}

		return null;
	}

	
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

	
	public Report removeFinishedReportFromStack(UUID id) {

		Stack<Report> tempStack = new Stack<>();
		Report currentReport = null;
		boolean removed = false;

		while (!onGoingReportStack.isEmpty()) {
			currentReport = onGoingReportStack.pop();

			if (currentReport.getTicketID().equals(id) && !removed) {
				removed = true;
			} else {
				tempStack.push(currentReport);
			}
		}

		while (!tempStack.isEmpty()) {
			onGoingReportStack.push(tempStack.pop());
		}

		return currentReport;
	}

	
	private Report extractFromQueue(Queue<Report> originalQueue, UUID targetID) {
		Queue<Report> tempQueue = new Queue<>();
		Report foundReport = null;

		
		while (!originalQueue.isEmpty()) {
			Report current = originalQueue.dequeue();

			if (current.getTicketID().equals(targetID)) {
				foundReport = current; 
			} else {
				tempQueue.enqueue(current); 
			}
		}

		
		while (!tempQueue.isEmpty()) {
			originalQueue.enqueue(tempQueue.dequeue());
		}

		return foundReport;
	}

	
	public Report extractPendingReport(UUID ticketID) {
		Report target = extractFromQueue(undoQueue, ticketID);
		if (target != null) {
			return target;
		}

		target = extractFromQueue(highPriorityQueue, ticketID);
		if (target != null) {
			return target;
		}

		target = extractFromQueue(mediumPriorityQueue, ticketID);
		if (target != null) {
			return target;
		}

		target = extractFromQueue(lowPriorityQueue, ticketID);
		return target;
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

	
	public Stack<Report> getToConfirmStack() {
		return toConfirmStack;
	}

	
	public void setToConfirmStack(Stack<Report> toConfirmStack) {
		this.toConfirmStack = toConfirmStack;
	}

	
	public SimpleList<Report> getAllHistoricalReports() {
		return allHistoricalReports;
	}

	
	private void appendQueueToListSafely(Queue<Report> sourceQueue, SimpleList<Report> targetList) {
		Queue<Report> tempQueue = new Queue<>();

		while (!sourceQueue.isEmpty()) {
			Report current = sourceQueue.dequeue();
			targetList.add(current);
			tempQueue.enqueue(current);
		}

		
		while (!tempQueue.isEmpty()) {
			sourceQueue.enqueue(tempQueue.dequeue());
		}
	}

	
	public SimpleList<Report> getAllPendingReportsOrdered() {
		SimpleList<Report> allPending = new SimpleList<>();

		
		appendQueueToListSafely(undoQueue, allPending);
		appendQueueToListSafely(highPriorityQueue, allPending);
		appendQueueToListSafely(mediumPriorityQueue, allPending);
		appendQueueToListSafely(lowPriorityQueue, allPending);

		return allPending;
	}
	
	
	public void setAllHistoricalReports(SimpleList<Report> allHistoricalReports) {
		this.allHistoricalReports = allHistoricalReports;
	}
}
