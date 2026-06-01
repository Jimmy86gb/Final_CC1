package co.edu.udistrital.model.usecases;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.repositories.ReportRepository;

/**
 * Caso de uso que retorna la informacion en un DTO de el siguiente reporte a
 * procesar
 *
 * @author Juan David Diaz Perez
 */
public class GetNextPendingReportUseCase {

	/**
	 * Instancia privada del repositorio de reportes en memoria
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta dentro del caso de uso el repositorio de reportes
	 * 
	 * @param reportRepository El repositorio de reportes
	 */
	public GetNextPendingReportUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Ejecuta el caso de uso y retorna el DTO del reporte más urgente.
	 * 
	 * @return ReportDTO listo para la vista, o null si no hay emergencias.
	 */
	public ReportDTO execute() {

		Report nextReport = reportRepository.peekNextPendingReport();

		if (nextReport == null) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		UUID ticketID = nextReport.getTicketID();
		String clientID = nextReport.getClient().getId();
		String clientName = nextReport.getClient().getName();
		String clientContact = nextReport.getClient().getContactInfo();
		String problemDesc = nextReport.getProblemDescription();
		String problemType = nextReport.getProblemType().getDisplayName();
		String priority = nextReport.getPriority().getDisplayName();
		String zone = nextReport.getReportZone().getDisplayName();
		String time = nextReport.getReportTime().format(formatter);
		String status = nextReport.getStatus().getDisplayName();

		UUID techID = nextReport.getAssignedTechnician() != null ? nextReport.getAssignedTechnician().getId() : null;
		String techName = nextReport.getAssignedTechnician() != null ? nextReport.getAssignedTechnician().getName()
				: "Sin asignar";
		String techSpec = nextReport.getAssignedTechnician() != null
				? nextReport.getAssignedTechnician().getSpecialty().getDisplayName()
				: "N/A";
		UUID unitID = nextReport.getAssignedUnit() != null ? nextReport.getAssignedUnit().getId() : null;
		UUID kitID = nextReport.getAssignedKit() != null ? nextReport.getAssignedKit().getId() : null;

		return new ReportDTO(ticketID, clientID, clientName, clientContact, problemDesc, problemType, priority, zone,
				time, status, techID, techName, techSpec, unitID, kitID, false, false, false, false);
	}
}
