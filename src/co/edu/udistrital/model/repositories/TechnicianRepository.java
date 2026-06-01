package co.edu.udistrital.model.repositories;

import java.util.UUID;

import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase que representa la memoria y administracion referente a los tecnicos en
 * el sistema
 *
 * @author Juan David Diaz Perez
 */
public class TechnicianRepository {

	/**
	 * Lista estatica de tecnicos que almacenara a todos en memoria
	 */
	private final SimpleList<Technician> technicianList = new SimpleList<>();

	/**
	 * Metodo que guarda un nuevo tecnico en la lista de tecnicos
	 * 
	 * @param technician Objeto de tecnico
	 * @return si la operacion fue un exito o no
	 */
	public boolean saveTechnician(Technician technician) {
		if (technicianList.contains(technician)) {
			return false;
		}
		technicianList.add(technician);
		return true;
	}

	/**
	 * Metodo que actualiza la informacion de un tecnico seleccionado
	 * 
	 * @param actualTechnician Objeto de tecnico con la informacion actual
	 * @param newTechnician    Objeto de tecnico con la informacion nueva
	 * @return Si la operacion fue exitosa o no
	 */
	public boolean update(Technician actualTechnician, Technician newTechnician) {
		return technicianList.update(actualTechnician, newTechnician);
	}

	/**
	 * Metodo que retorna si exite un elemento tecnico de la lista segun una
	 * busqueda por ID
	 * 
	 * @param id Id para buscar la lista
	 * @return Elemento tecnico con la id dada
	 */
	public Technician getTechnicianByID(UUID id) {
		Iterator<Technician> iterator = this.technicianList.iterador();

		while (iterator.hasNext()) {
			Technician currentTechnician = iterator.Next();

			if (currentTechnician.getId().equals(id)) {
				return currentTechnician;
			}
		}

		return null;
	}

	/**
	 * Metodo que retorna a todos los tecnicos del sistema
	 * 
	 * @return la lista de todos los tecnicos
	 */
	public SimpleList<Technician> getAllTechnicians() {
		SimpleList<Technician> copyList = new SimpleList<>();
		Iterator<Technician> iterator = technicianList.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	/**
	 * Metodo que retorna una lista de todos los tecnicos disponibles en una zona y
	 * con la especialidad requerida
	 * 
	 * @param zone      Zona donde se requiere el tecnico
	 * @param specialty Especialidad del tecnico
	 * @return La lista filtrada de los tecnicos
	 */
	public SimpleList<Technician> getAvailableTechnicians(OperationZone zone, TechnicianSpecialty specialty) {
		SimpleList<Technician> filteredList = new SimpleList<>();
		Iterator<Technician> iterator = technicianList.iterador();

		while (iterator.hasNext()) {
			Technician technician = iterator.Next();
			if (technician.getStatus() == TechnicianStatus.AVAILABLE && technician.getZone() == zone
					&& technician.getSpecialty() == specialty) {
				filteredList.add(technician);
			}
		}
		return filteredList;
	}

	/**
	 * Metodo que retorna una lista filtrada de los tecnicos por especialidad y
	 * status actual
	 * 
	 * @param technicianSpecialty Especialidad a filtar
	 * @param technicianStatus    Status a filtrar
	 * @return La lista filtrada de los tecnicos
	 */
	public SimpleList<Technician> getTechnicianBySpecialityAndStatuSimpleList(TechnicianSpecialty technicianSpecialty,
			TechnicianStatus technicianStatus) {

		SimpleList<Technician> filteredList = new SimpleList<>();
		Iterator<Technician> iterator = technicianList.iterador();

		while (iterator.hasNext()) {
			Technician technician = iterator.Next();
			if (technician.getStatus() == technicianStatus && technician.getSpecialty() == technicianSpecialty) {
				filteredList.add(technician);
			}
		}
		return filteredList;
	}
}
