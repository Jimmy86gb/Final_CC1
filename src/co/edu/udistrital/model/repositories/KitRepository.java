package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.structures.Stack;

/**
 * Clase que representa la memoria y administracion referente a los kits en el
 * sistema
 *
 * @author Juan David Diaz Perez
 */
public class KitRepository {

	/**
	 * Lista estatica de kits que almacenara a todos en memoria
	 */
	private final SimpleList<Kit> kitList = new SimpleList<Kit>();

	/**
	 * Pila que representa los kits que estan en mantenimiento
	 */
	private final Stack<Kit> maintenanceKitStack = new Stack<Kit>();

	/**
	 * Metodo que guarda un kit dentro de la lista de kits del sistema
	 * 
	 * @param kit Objeto kit
	 * @return Si la operacion fue exitosa o no
	 */
	public boolean saveKit(Kit kit) {
		if (kitList.contains(kit)) {
			return false;
		}

		kitList.add(kit);
		return true;
	}

	/**
	 * Metodo que elimina un kit de la lista de kits del sistema
	 * 
	 * @param kit Objeto kit
	 * @return Si la operacion fue exitosa o no
	 */
	public boolean deleteKit(Kit kit) {
		return kitList.delete(kit);
	}

	/**
	 * Metodo que actualiza la informacion del kit seleccionado
	 * 
	 * @param currentKit Objeto del kit con la informacion actual
	 * @param newKit     Objeto del kit con la informacion nueva
	 * @return Si la operacion fue exitosa o no
	 */
	public boolean updatekit(Kit currentKit, Kit newKit) {
		return kitList.update(currentKit, newKit);
	}

	/**
	 * Metodo que retorna todos los kits del sistema
	 * 
	 * @return La lista de todos los kits del sistema
	 */
	public SimpleList<Kit> getAllKits() {
		SimpleList<Kit> copyList = new SimpleList<>();
		Iterator<Kit> iterator = kitList.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	/**
	 * Metodo que retorna todos los kits disponibles de un tipo en especifico
	 * 
	 * @param kitType Tipo de kit buscado
	 * @return Lista filtrada
	 */
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

	/**
	 * Metodo que retorna todos los kits de un status especifico
	 * 
	 * @param unitStatus Status del kit buscado
	 * @return Lista filtrada
	 */
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

	/**
	 * Metodo que guarda en mantenimiento un kit ya usado
	 * 
	 * @param kit Kit usado para mantener
	 */
	public void maintainKit(Kit kit) {
		maintenanceKitStack.push(kit);
	}

	/**
	 * Metodo que retorna a servicio el ultimo kit mantenido
	 * 
	 * @return El objeto del kit mantenido
	 */
	public Kit dispatchKit() {
		return maintenanceKitStack.pop();
	}

	/**
	 * Metodo que devuelve los integrantes de la pila (kit en mantenimiento) en
	 * orden
	 * 
	 * @return La lista de los kits en mantenimiento
	 */
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
}
