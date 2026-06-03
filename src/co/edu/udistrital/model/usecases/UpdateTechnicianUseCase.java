package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.TechnicianStatusFactory;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.TechnicianRepository;


public class UpdateTechnicianUseCase {

	
	private final TechnicianFactory technicianFactory = new TechnicianFactory();

	
	private final ZoneFactory zoneFactory = new ZoneFactory();

	
	private final TechnicianStatusFactory technicianStatusFactory = new TechnicianStatusFactory();

	
	private final TechnicianRepository technicianRepository;

	
	public UpdateTechnicianUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	
	public ResponseDTO execute(String idTechnician, String name, String specialty, String zone, String status) {
		try {

			UUID id = UUID.fromString(idTechnician);

			TechnicianSpecialty technicianSpecialty = technicianFactory.generaTechnicianSpecialty(specialty);
			OperationZone operationZone = zoneFactory.generateOperationZone(zone);
			TechnicianStatus technicianStatus = technicianStatusFactory.generaTechnicianStatus(status);

			Technician actualTechnician = technicianRepository.getTechnicianByID(id);

			if (actualTechnician == null) {
				return new ResponseDTO(false,
						"El tecnico no se encontró en el sistema. Es posible que haya sido eliminado.");
			}

			Technician newTechnician = new Technician(name, technicianSpecialty, operationZone, technicianStatus);

			newTechnician.setId(id);

			boolean isUpdated = technicianRepository.update(actualTechnician, newTechnician);

			if (isUpdated) {
				return new ResponseDTO(true, "La información del tecnico " + name + " fue actualizada exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo actualizar la información del tecnico.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al actualizar los datos del tecnico.");
		}
	}
}
