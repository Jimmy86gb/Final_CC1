package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.ActionType;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.TechnicianStatusFactory;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ActionLogRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;

/**
 * Clase de caso de uso que se encarga de actualizar la informacion de los
 * tecnicos recibida desde la vista
 *
 * @author Juan David Diaz Perez
 */
public class UpdateTechnicianUseCase {

	/**
	 * Instancia del factory de la especializacion del tecnico
	 */
	private final TechnicianFactory technicianFactory = new TechnicianFactory();

	/**
	 * Instancia del factory de la zona de las entidades
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();

	/**
	 * Instancia del factory de el status del tecnico
	 */
	private final TechnicianStatusFactory technicianStatusFactory = new TechnicianStatusFactory();

	/**
	 * Instancia del repositorio que maneja la memoria de los técnicos
	 */
	private final TechnicianRepository technicianRepository;

	/**
	 * Repositorio global de historial de acciones (Memento)
	 */
	private final ActionLogRepository logRepo;

	/**
	 * Constructor que inyecta el repositorio centralizado del sistema
	 * 
	 * @param technicianRepository El DAO de técnicos instanciado en el sistema
	 */
	public UpdateTechnicianUseCase(TechnicianRepository technicianRepository, ActionLogRepository logRepo) {
		this.technicianRepository = technicianRepository;
		this.logRepo = logRepo;
	}

	/**
	 * Metodo que ejecuta el caso de uso de actualizacion de datos de tenico
	 * 
	 * @param idTechnician El ID de ambos tecnicos
	 * @param name         El nombre de ambos tecnicos
	 * @param specialty    La especialidad de ambos tecnicos
	 * @param zone         La nueva zona del tecnico
	 * @param status       El nuevo status del tecnico
	 * @return DTO de respuesta frente a la accion realizada
	 */
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

			logRepo.logAction(new ActionRecord("Actualización de Técnico: " + actualTechnician.getName(),
				ActionType.UPDATE_TECHNICIAN, actualTechnician.getId().toString(), actualTechnician.getName(),
				actualTechnician.getSpecialty().name(), actualTechnician.getZone().name(),
				actualTechnician.getStatus().name()));

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
