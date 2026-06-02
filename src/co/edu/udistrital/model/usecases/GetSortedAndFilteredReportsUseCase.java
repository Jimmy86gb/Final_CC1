package co.edu.udistrital.model.usecases;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista completa
 * ordenada con opciones de cancelar segun estados especificos en el caso de uso
 *
 * @author Juan David Diaz Perez
 */
public class GetSortedAndFilteredReportsUseCase {

	/**
	 * Instancia privada del repositorio de memoria de reportes
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta dentro del caso de uso el repositorio de reportes
	 * 
	 * @param reportRepository Repositorio de reportes
	 */
	public GetSortedAndFilteredReportsUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Metodo de apoyo optimizado que genera la lista ordenada final con base en el
	 * criterio del repositorio cruzando Tipo de Unidad y Estado.
	 * 
	 * @return Lista ordenada
	 */
	private SimpleList<Report> buildOrderedList() {
		SimpleList<Report> orderedList = new SimpleList<>();

		CriticLevel[] criticLevels = { CriticLevel.HIGH, CriticLevel.MEDIUM, CriticLevel.LOW };

		ReportStatus[] reportStatus = { ReportStatus.DONE, ReportStatus.ON_GOING, ReportStatus.PENDING,
				ReportStatus.CANCELLED };

		for (int i = 0; i < criticLevels.length; i++) {
			for (int j = 0; j < reportStatus.length; j++) {

				SimpleList<Report> subList = reportRepository.getReportsByCriticLevelAndStatus(criticLevels[i],
						reportStatus[j]);

				Iterator<Report> iterator = subList.iterador();

				while (iterator.hasNext()) {
					orderedList.add(iterator.Next());
				}
			}
		}

		return orderedList;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la vista ya ordenada
	 * 
	 * @return Lista basada en kitDTO
	 */
	public SimpleList<ReportDTO> execute() {

		SimpleList<ReportDTO> resultList = new SimpleList<>();
		SimpleList<Report> orderedList = buildOrderedList();
		Iterator<Report> iterator = orderedList.iterador();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
