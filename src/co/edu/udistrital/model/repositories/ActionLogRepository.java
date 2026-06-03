package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.Stack;

public class ActionLogRepository {

	
	private final Stack<ActionRecord> globalHistoryStack = new Stack<>();

	public void logAction(ActionRecord record) {
		globalHistoryStack.push(record);
	}

	public ActionRecord popLastAction() {
		if (globalHistoryStack.isEmpty()) {
			return null;
		}
		return globalHistoryStack.pop();
	}

	
	public SimpleList<String> getLogDescriptions() {
		SimpleList<String> logs = new SimpleList<>();
		Stack<ActionRecord> tempStack = new Stack<>();

		
		while (!globalHistoryStack.isEmpty()) {
			ActionRecord r = globalHistoryStack.pop();
			logs.add(r.getDescription());
			tempStack.push(r);
		}
		
		while (!tempStack.isEmpty()) {
			globalHistoryStack.push(tempStack.pop());
		}
		return logs;
	}
}