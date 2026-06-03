package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.TechnicianRepository;


public class RegisterTechnicianUseCase {

	
	private final TechnicianFactory technicianFactory = new TechnicianFactory();

	
	private final ZoneFactory zoneFactory = new ZoneFactory();

	
	private final TechnicianRepository technicianRepository;

	
	public RegisterTechnicianUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	
	public ResponseDTO execute(String name, String speciality, String zone) {

		try {
			TechnicianSpecialty technicianSpecialty = technicianFactory.generaTechnicianSpecialty(speciality);
			OperationZone operationZone = zoneFactory.generateOperationZone(zone);

			TechnicianStatus initialStatus = TechnicianStatus.AVAILABLE;

			Technician newTechnician = new Technician(name, technicianSpecialty, operationZone, initialStatus);

			boolean isSaved = technicianRepository.saveTechnician(newTechnician);

			if (isSaved) {
				return new ResponseDTO(true, "El técnico " + name + " fue registrado exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo registrar. El técnico ya existe en el sistema.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al registrar el técnico.");
		}
	}
}