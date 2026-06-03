package co.edu.udistrital.model.repositories;

import java.util.UUID;

import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.structures.Stack;


public class ServiceUnitRepository {

	
	private SimpleList<ServiceUnit> unitList = new SimpleList<>();

	
	private Stack<ServiceUnit> toConfirmStack = new Stack<ServiceUnit>();

	
	public boolean saveUnit(ServiceUnit unit) {
		if (unitList.contains(unit)) {
			return false;
		}
		unitList.add(unit);
		return true;
	}

	
	public boolean update(ServiceUnit actualUnit, ServiceUnit newUnit) {
		return unitList.update(actualUnit, newUnit);
	}

	
	public ServiceUnit getServiceUnitByID(UUID id) {
		Iterator<ServiceUnit> iterator = this.unitList.iterador();

		while (iterator.hasNext()) {
			ServiceUnit currentServiceUnit = iterator.Next();

			if (currentServiceUnit.getId().equals(id)) {
				return currentServiceUnit;
			}
		}

		return null;
	}

	
	public SimpleList<ServiceUnit> getAllUnits() {
		SimpleList<ServiceUnit> copyList = new SimpleList<>();
		Iterator<ServiceUnit> iterator = unitList.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	
	public SimpleList<ServiceUnit> getAvailableUnitsByZone(OperationZone targetZone) {
		SimpleList<ServiceUnit> filteredList = new SimpleList<>();
		Iterator<ServiceUnit> iterator = unitList.iterador();

		while (iterator.hasNext()) {
			ServiceUnit unit = iterator.Next();
			if (unit.getStatus() == UnitStatus.AVAILABLE && unit.getZone() == targetZone) {
				filteredList.add(unit);
			}
		}
		return filteredList;
	}

	
	public SimpleList<ServiceUnit> getUnitsByStatusAndType(UnitStatus unitStatus, UnitType unitType) {
		SimpleList<ServiceUnit> filteredList = new SimpleList<>();
		Iterator<ServiceUnit> iterator = unitList.iterador();

		while (iterator.hasNext()) {
			ServiceUnit unit = iterator.Next();
			if (unit.getStatus() == unitStatus && unit.getType() == unitType) {
				filteredList.add(unit);
			}
		}
		return filteredList;
	}

	
	public void pushToConfirm(ServiceUnit serviceUnit) {
		toConfirmStack.push(serviceUnit);
	}

	
	public ServiceUnit popOnConfirm() {
		return toConfirmStack.pop();
	}

	
	public SimpleList<ServiceUnit> getToConfirmUnits() {

		SimpleList<ServiceUnit> copyList = new SimpleList<>();
		Stack<ServiceUnit> tempStack = new Stack<>();

		while (!toConfirmStack.isEmpty()) {
			ServiceUnit currentServiceUnit = toConfirmStack.pop();

			copyList.add(currentServiceUnit);

			tempStack.push(currentServiceUnit);
		}

		while (!tempStack.isEmpty()) {
			toConfirmStack.push(tempStack.pop());
		}

		return copyList;
	}

	
	public SimpleList<ServiceUnit> getUnitList() {
		return unitList;
	}

	
	public void setUnitList(SimpleList<ServiceUnit> unitList) {
		this.unitList = unitList;
	}

	
	public Stack<ServiceUnit> getToConfirmStack() {
		return toConfirmStack;
	}

	
	public void setToConfirmStack(Stack<ServiceUnit> toConfirmStack) {
		this.toConfirmStack = toConfirmStack;
	}
}
