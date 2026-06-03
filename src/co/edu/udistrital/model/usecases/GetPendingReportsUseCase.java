package co.edu.udistrital.model.usecases;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Caso de uso encargado de consultar la fila completa de siniestros pendientes
 * ordenados por prioridad y transformarlos a DTOs para la interfaz.
 *
 * @author Juan David Diaz Perez
 */
public class GetPendingReportsUseCase {

	private final ReportRepository reportRepository;

	public GetPendingReportsUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Ejecuta la extracción de todos los reportes pendientes. * @return Una lista
	 * de ReportDTOs lista para la vista.
	 */
	public SimpleList<ReportDTO> execute() {

		SimpleList<ReportDTO> resultList = new SimpleList<>();

		SimpleList<Report> pendingReports = reportRepository.getAllPendingReportsOrdered();

		Iterator<Report> iterator = pendingReports.iterador();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		while (iterator.hasNext()) {

			Report report = iterator.Next();

			String ticketID = report.getTicketID().toString();
			String clientID = report.getClient().getId();
			String clientName = report.getClient().getName();
			String clientContact = report.getClient().getContactInfo();
			String problemDesc = report.getProblemDescription();
			String problemType = report.getProblemType().getDisplayName();
			String priority = report.getPriority().getDisplayName();
			String zone = report.getReportZone().getDisplayName();
			String time = report.getReportTime().format(formatter);
			String status = report.getStatus().getDisplayName();

			UUID techID = null;
			String techName = "Sin asignar";
			String techSpec = "N/A";
			UUID unitID = null;
			String unitType = "N/A";
			UUID kitID = null;

			if (report.getAssignedTechnician() != null) {
				techID = report.getAssignedTechnician().getId();
				techName = report.getAssignedTechnician().getName();
				techSpec = report.getAssignedTechnician().getSpecialty().getDisplayName();
			}

			if (report.getAssignedUnit() != null) {
				unitID = report.getAssignedUnit().getId();
				unitType = report.getAssignedUnit().getType().getDisplayName();
			}

			if (report.getAssignedKit() != null) {
				kitID = report.getAssignedKit().getId();
			}

			boolean canCancel = (report.getStatus() == ReportStatus.PENDING);
			boolean canUndo = false;
			boolean canFinish = false;
			boolean canConfirm = false;

			ReportDTO dto = new ReportDTO(ticketID, clientID, clientName, clientContact, problemDesc, problemType,
					priority, zone, time, status, techID, techName, techSpec, unitID, unitType, kitID, canCancel,
					canUndo, canFinish, canConfirm);

			resultList.add(dto);
		}

		return resultList;
	}
}