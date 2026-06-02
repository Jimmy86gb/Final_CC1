package co.edu.udistrital.model.usecases;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista de reportes
 * que estan en proceso con la opcion de terminar para todos, y la opcion de
 * deshacer para el tope
 *
 * @author Juan David Diaz Perez
 */
public class GetOnGoingReportsUseCase {

	/**
	 * Instancia privada del repositorio de memoria de reportes
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta dentro del caso de uso el repositorio de reportes
	 * 
	 * @param reportRepository Repositorio de reportes
	 */
	public GetOnGoingReportsUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Metodo encargado de retornar la lista de la pila a la vista. Solo el reporte
	 * en el tope se podra deshacer
	 * 
	 * @return Lista basada en ReportDTO
	 */
	public SimpleList<ReportDTO> execute() {

		SimpleList<ReportDTO> resultList = new SimpleList<ReportDTO>();

		SimpleList<Report> stackList = reportRepository.getOnGoingReports();
		Iterator<Report> iterator = stackList.iterador();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		boolean isTopElement = true;

		while (iterator.hasNext()) {
			Report report = iterator.Next();

			UUID ticketID = report.getTicketID();
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
			UUID kitID = null;

			if (report.getAssignedTechnician() != null) {
				techID = report.getAssignedTechnician().getId();
				techName = report.getAssignedTechnician().getName();
				techSpec = report.getAssignedTechnician().getSpecialty().getDisplayName();
			}

			if (report.getAssignedUnit() != null) {
				unitID = report.getAssignedUnit().getId();
			}

			if (report.getAssignedKit() != null) {
				kitID = report.getAssignedKit().getId();
			}

			boolean canCancel = false;
			boolean canFinish = true;
			boolean canConfirm = false;

			ReportDTO dto = new ReportDTO(ticketID, clientID, clientName, clientContact, problemDesc, problemType,
					priority, zone, time, status, techID, techName, techSpec, unitID, kitID, canCancel, isTopElement,
					canFinish, canConfirm);

			resultList.add(dto);

			isTopElement = false;
		}

		return resultList;
	}
}
