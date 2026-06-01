package co.edu.udistrital.model.repositories;

import java.util.UUID;

import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.structures.Stack;

/**
 * Clase que representa la memoria y administracion referente a las unidades de
 * servicio en el sistema
 *
 * @author Juan David Diaz Perez
 */
public class ServiceUnitRepository {

	/**
	 * Lista estatica de unidades de servicio que almacenara a todos en memoria
	 */
	private final SimpleList<ServiceUnit> unitList = new SimpleList<>();

	/**
	 * Pila estatica para confirmar cambios de estado de la unidad de servicio
	 */
	private final Stack<ServiceUnit> toConfirmStack = new Stack<ServiceUnit>();

	/**
	 * Metodo que guarda una nueva unidad de servicios en la lista de unidades
	 * 
	 * @param unit Objeto de la unidad
	 * @return si la operacion fue un exito o no
	 */
	public boolean saveUnit(ServiceUnit unit) {
		if (unitList.contains(unit)) {
			return false;
		}
		unitList.add(unit);
		return true;
	}

	/**
	 * Metodo que elimina una unidad de servicio en la memoria de unidades de
	 * servicio
	 * 
	 * @param unit Objeto de unidad de servicio a eliminar
	 * @return Si la operacion fue un exito o no
	 */
	public boolean deleteUnit(ServiceUnit unit) {
		return unitList.delete(unit);
	}

	/**
	 * Metodo que actualiza la informacion de una unidad de servicio selecionada
	 * 
	 * @param actualUnit Objeto unidad con la informacion actual
	 * @param newUnit    Objeto unidad con la informacion nueva
	 * @return Si la operacion fue exitosa
	 */
	public boolean update(ServiceUnit actualUnit, ServiceUnit newUnit) {
		return unitList.update(actualUnit, newUnit);
	}

	/**
	 * Metodo que retorna si exite un elemento unidad de servicio de la lista segun
	 * una busqueda por ID
	 * 
	 * @param id Id para buscar la lista
	 * @return Elemento unidad de servicio con la id dada
	 */
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

	/**
	 * Metodo que retorna toda la lista de unidades de servicio
	 * 
	 * @return La lista de unidades de servicio
	 */
	public SimpleList<ServiceUnit> getAllUnits() {
		SimpleList<ServiceUnit> copyList = new SimpleList<>();
		Iterator<ServiceUnit> iterator = unitList.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	/**
	 * Metodo que retorna las unidades de servicio disponibles por zona
	 * 
	 * @param targetZone Zona de filtrado
	 * @return La lista filtrada
	 */
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

	/**
	 * Metodo que retorna las unidades de servicio por status y tipo de unidad
	 * 
	 * @param unitStatus Estatus de unidad a filtrar
	 * @param unitType   Tipo de unidad a filtrar
	 * @return La lista filtrada
	 */
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

	/**
	 * Metodo que añade una nueva unidad de servicio a confirmar su cambio de estado
	 * 
	 * @param serviceUnit Unidad de servicio a cambiar el estado
	 */
	public void pushToConfirm(ServiceUnit serviceUnit) {
		toConfirmStack.push(serviceUnit);
	}

	/**
	 * Metodo que saca la unidad de servicio que encontraba pendiente la
	 * confirmacion de sus cambios
	 * 
	 * @return Unidad de servicio fuera de la fila
	 */
	public ServiceUnit popOnConfirm() {
		return toConfirmStack.pop();
	}

	/**
	 * Metodo que muestra todas las unidades de servicio a confirmar su cambio de
	 * estado
	 * 
	 * @return La lista de todos los reportes tratados en el momento
	 */
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
}
