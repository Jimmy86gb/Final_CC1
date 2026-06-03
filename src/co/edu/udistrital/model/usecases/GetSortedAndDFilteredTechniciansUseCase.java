package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetSortedAndDFilteredTechniciansUseCase {

	
	private final TechnicianRepository technicianRepository;

	
	public GetSortedAndDFilteredTechniciansUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	
	private SimpleList<Technician> buildOrderedList() {
		SimpleList<Technician> orderedList = new SimpleList<Technician>();

		
		
		TechnicianSpecialty[] specialtiesOrder = { TechnicianSpecialty.AUTOMOTIVE_ELECTRICITY,
				TechnicianSpecialty.GENERAL_MECHANICS, TechnicianSpecialty.VEHICULAR_LOCKSMITH,
				TechnicianSpecialty.CRANE_OPERATION, TechnicianSpecialty.PLUMBING };

		
		TechnicianStatus[] statusOrder = { TechnicianStatus.AVAILABLE, TechnicianStatus.INACTIVE,
				TechnicianStatus.BUSY };

		
		for (int i = 0; i < specialtiesOrder.length; i++) {
			for (int j = 0; j < statusOrder.length; j++) {

				
				SimpleList<Technician> subList = technicianRepository
						.getTechnicianBySpecialityAndStatuSimpleList(specialtiesOrder[i], statusOrder[j]);

				Iterator<Technician> iterator = subList.iterador();

				
				while (iterator.hasNext()) {
					orderedList.add(iterator.Next());
				}
			}
		}

		return orderedList;
	}

	
	public SimpleList<TechnicianDTO> execute() {

		SimpleList<TechnicianDTO> resultList = new SimpleList<>();

		SimpleList<Technician> orderedList = buildOrderedList();

		Iterator<Technician> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Technician technician = iterator.Next();

			boolean canEdit = (technician.getStatus() == TechnicianStatus.AVAILABLE
					|| technician.getStatus() == TechnicianStatus.INACTIVE);

			String speciality = technician.getSpecialty().getDisplayName();
			String status = technician.getStatus().getDisplayName();
			String zone = technician.getZone().getDisplayName();

			TechnicianDTO dto = new TechnicianDTO(technician.getId().toString(), technician.getName(), speciality, zone,
					status, canEdit);

			resultList.add(dto);
		}

		return resultList;
	}
}
