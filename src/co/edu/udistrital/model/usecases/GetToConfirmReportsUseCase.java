package co.edu.udistrital.model.usecases;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class GetToConfirmReportsUseCase {

	
	private final ReportRepository reportRepository;

	
	public GetToConfirmReportsUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	
	public SimpleList<ReportDTO> execute() {

		SimpleList<ReportDTO> resultList = new SimpleList<ReportDTO>();

		SimpleList<Report> stackList = reportRepository.getToConfirmReports();
		Iterator<Report> iterator = stackList.iterador();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		boolean isTopElement = true;

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

			boolean canCancel = false;
			boolean canUndo = false;
			boolean canFinish = false;

			ReportDTO dto = new ReportDTO(ticketID, clientID, clientName, clientContact, problemDesc, problemType,
					priority, zone, time, status, techID, techName, techSpec, unitID, unitType, kitID, canCancel,
					canUndo, canFinish, isTopElement);

			resultList.add(dto);

			isTopElement = false;
		}

		return resultList;
	}
}
