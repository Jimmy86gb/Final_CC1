package co.edu.udistrital.model.repositories;

import java.util.UUID;

import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.structures.Stack;


public class KitRepository {

	
	private SimpleList<Kit> kitList;

	
	private Stack<Kit> maintenanceKitStack;

	
	public KitRepository() {
		this.kitList = new SimpleList<Kit>();
		this.maintenanceKitStack = new Stack<Kit>();
	}

	
	public boolean saveKit(Kit kit) {
		if (kitList.contains(kit)) {
			return false;
		}

		kitList.add(kit);
		return true;
	}

	
	public boolean updatekit(Kit currentKit, Kit newKit) {
		return kitList.update(currentKit, newKit);
	}

	
	public Kit getKitByID(UUID id) {
		Iterator<Kit> iterator = this.kitList.iterador();

		while (iterator.hasNext()) {
			Kit currentKit = iterator.Next();

			if (currentKit.getId().equals(id)) {
				return currentKit;
			}
		}

		return null;
	}

	
	public SimpleList<Kit> getAllKits() {
		SimpleList<Kit> copyList = new SimpleList<>();
		Iterator<Kit> iterator = kitList.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	
	public SimpleList<Kit> getAvailableKitsByType(KitType kitType) {
		SimpleList<Kit> filteredList = new SimpleList<>();
		Iterator<Kit> iterator = kitList.iterador();

		while (iterator.hasNext()) {
			Kit actualKit = iterator.Next();

			if (actualKit.getType() == kitType && actualKit.getStatus() == UnitStatus.AVAILABLE) {
				filteredList.add(actualKit);
			}
		}
		return filteredList;
	}

	
	public SimpleList<Kit> getKitsByStatus(UnitStatus unitStatus) {
		SimpleList<Kit> filteredList = new SimpleList<>();
		Iterator<Kit> iterator = kitList.iterador();

		while (iterator.hasNext()) {
			Kit actualKit = iterator.Next();

			if (actualKit.getStatus() == unitStatus) {
				filteredList.add(actualKit);
			}
		}
		return filteredList;
	}

	
	public void maintainKit(Kit kit) {
		maintenanceKitStack.push(kit);
	}

	
	public Kit dispatchKit() {
		return maintenanceKitStack.pop();
	}

	
	public SimpleList<Kit> getMaintenanceKits() {

		SimpleList<Kit> copyList = new SimpleList<>();
		Stack<Kit> tempStack = new Stack<>();

		while (!maintenanceKitStack.isEmpty()) {
			Kit currentKit = maintenanceKitStack.pop();

			copyList.add(currentKit);

			tempStack.push(currentKit);
		}

		while (!tempStack.isEmpty()) {
			maintenanceKitStack.push(tempStack.pop());
		}

		return copyList;
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
}
