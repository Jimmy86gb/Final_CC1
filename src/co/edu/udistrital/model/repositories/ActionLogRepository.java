package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.Stack;

public class ActionLogRepository {

	// La Pila maestra de todo el sistema
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

	// Para mostrar el log en el Dashboard
	public SimpleList<String> getLogDescriptions() {
		SimpleList<String> logs = new SimpleList<>();
		Stack<ActionRecord> tempStack = new Stack<>();

		// Extraemos en orden LIFO (Lo más reciente primero)
		while (!globalHistoryStack.isEmpty()) {
			ActionRecord r = globalHistoryStack.pop();
			logs.add(r.getDescription());
			tempStack.push(r);
		}
		// Restauramos la pila original
		while (!tempStack.isEmpty()) {
			globalHistoryStack.push(tempStack.pop());
		}
		return logs;
	}
}