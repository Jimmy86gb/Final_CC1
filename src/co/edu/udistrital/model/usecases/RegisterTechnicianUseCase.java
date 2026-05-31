package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.TechnicianRepository;

/**
 * Clase que representa el caso de uso de registrar un nuevo tecnico en el
 * sistema.
 *
 * @author Juan David Diaz Perez
 */
public class RegisterTechnicianUseCase {

	/**
	 * Instancia del factory de la especializacion del tecnico
	 */
	private final TechnicianFactory technicianFactory = new TechnicianFactory();

	/**
	 * Instancia del factory de la zona de las entidades
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();

	/**
	 * Instancia del repositorio que maneja la memoria de los técnicos
	 */
	private final TechnicianRepository technicianRepository;

	/**
	 * Constructor que inyecta el repositorio centralizado del sistema
	 * 
	 * @param technicianRepository El DAO de técnicos instanciado en el sistema
	 */
	public RegisterTechnicianUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	/**
	 * Ejecuta el caso de uso para registrar un técnico
	 * 
	 * @param name       Nombre del técnico
	 * @param speciality Especialidad capturada en la vista (String)
	 * @param zone       Zona capturada en la vista (String)
	 * @return Objeto ResponseDTO con el estado de la operacion y el mensaje para la
	 *         UI
	 */
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