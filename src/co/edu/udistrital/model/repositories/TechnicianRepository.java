package co.edu.udistrital.model.repositories;

import java.util.UUID;

import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class TechnicianRepository {

	
	private SimpleList<Technician> technicianList = new SimpleList<>();

	
	public boolean saveTechnician(Technician technician) {
		if (technicianList.contains(technician)) {
			return false;
		}
		technicianList.add(technician);
		return true;
	}

	
	public boolean update(Technician actualTechnician, Technician newTechnician) {
		return technicianList.update(actualTechnician, newTechnician);
	}

	
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

	
	public SimpleList<Technician> getAllTechnicians() {
		SimpleList<Technician> copyList = new SimpleList<>();
		Iterator<Technician> iterator = technicianList.iterador();

		while (iterator.hasNext()) {
			copyList.add(iterator.Next());
		}
		return copyList;
	}

	
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

	
	public SimpleList<Technician> getTechnicianList() {
		return technicianList;
	}

	
	public void setTechnicianList(SimpleList<Technician> technicianList) {
		this.technicianList = technicianList;
	}

}
