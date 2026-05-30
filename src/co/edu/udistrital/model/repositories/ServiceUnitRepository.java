package co.edu.udistrital.model.repositories;

import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

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
}
