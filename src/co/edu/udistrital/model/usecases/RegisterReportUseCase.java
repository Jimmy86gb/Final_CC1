package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.enums.CriticFactory;
import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.repositories.ReportRepository;

/**
 * Caso de uso que orquesta el registro de un nuevo siniestro y lo asigna a la
 * cola de prioridad correspondiente.
 *
 * @author Juan David Diaz Perez
 */
public class RegisterReportUseCase {

	/**
	 * Instancia privada del factory de zona
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();

	/**
	 * Instancia privada del factory de especialidad de tecnico, en este caso el
	 * tipo de tecnico requerido
	 */
	private final TechnicianFactory specialtyFactory = new TechnicianFactory();

	/**
	 * Instancia privada del factory de prioridad de solicitud
	 */
	private final CriticFactory priorityFactory = new CriticFactory();

	/**
	 * Repositorio privado de clientes para encontrar el cliente seleccionado
	 */
	private final ClientRepository clientRepository;

	/**
	 * Repositorio de reportes para manejar la memoria de estos
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta los repositorios de clientes y de reportes
	 * 
	 * @param clientRepository Repositorio de cliente
	 * @param reportRepository Repositorio de reportes
	 */
	public RegisterReportUseCase(ClientRepository clientRepository, ReportRepository reportRepository) {
		this.clientRepository = clientRepository;
		this.reportRepository = reportRepository;
	}

	/**
	 * Ejecuta el registro del nuevo siniestro. * @param clientId ID del cliente
	 * extraído desde el DTO seleccionado en la vista.
	 * 
	 * @param description Qué ocurrió.
	 * @param type        Tipo de problema (Especialidad).
	 * @param priority    Criticidad elegida.
	 * @param zone        Zona del incidente.
	 * @return DTO con el resultado de la operación.
	 */
	public ResponseDTO execute(String clientId, String description, String type, String priority, String zone) {
		try {

			Client actualClient = clientRepository.getClientByID(clientId);

			if (actualClient == null) {
				return new ResponseDTO(false, "El cliente seleccionado no existe o no es válido.");
			}

			OperationZone operationZone = zoneFactory.generateOperationZone(zone);
			TechnicianSpecialty problemType = specialtyFactory.generaTechnicianSpecialty(type);
			CriticLevel criticLevel = priorityFactory.generateCriticLevel(priority);

			if ((actualClient.getType() == ClientType.INSURANCE
					|| actualClient.getType() == ClientType.TRANSPORT_COMPANY) && criticLevel == CriticLevel.LOW) {
				criticLevel = CriticLevel.MEDIUM;
			}

			Report newReport = new Report(actualClient, description, problemType, criticLevel, operationZone);

			boolean isSaved = reportRepository.saveReport(newReport);

			if (isSaved) {
				return new ResponseDTO(true, "Siniestro registrado exitosamente. Asignado a la cola de prioridad: "
						+ criticLevel.getDisplayName());
			} else {
				return new ResponseDTO(false, "No se pudo registrar el siniestro por que este ya esta registrado");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos seleccionados: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al registrar el siniestro.");
		}
	}
}