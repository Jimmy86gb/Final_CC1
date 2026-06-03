package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetAvailableTechnicianByZoneAndProblemUseCase {

	
	private final TechnicianRepository technicianRepository;

	
	private final TechnicianFactory technicianFactory = new TechnicianFactory();

	
	private final ZoneFactory zoneFactory = new ZoneFactory();

	
	public GetAvailableTechnicianByZoneAndProblemUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	
	public SimpleList<TechnicianDTO> execute(String zone, String speciality) {

		SimpleList<TechnicianDTO> resultList = new SimpleList<>();

		OperationZone operationZone = zoneFactory.generateOperationZone(zone);
		TechnicianSpecialty technicianSpecialty = technicianFactory.generaTechnicianSpecialty(speciality);

		SimpleList<Technician> orderedList = technicianRepository.getAvailableTechnicians(operationZone,
				technicianSpecialty);

		Iterator<Technician> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Technician technician = iterator.Next();

			String status = technician.getStatus().getDisplayName();

			TechnicianDTO dto = new TechnicianDTO(technician.getId().toString(), technician.getName(), speciality, zone,
					status, false);

			resultList.add(dto);
		}
		
		

		return resultList;
	}
}
