package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.ReportRepository;

/**
 * Caso de uso para desaprobar los recursos asignados a un reporte y liberarlos
 * para otros reportes
 *
 * @author Juan David Diaz Perez
 */
public class UndoReportResourcesUseCase {

	/**
	 * Instancia privada del repositorio de memoria de reportes
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta al caso de uso todos los repositotios necesarios en
	 * la logica
	 * 
	 * @param reportRepository Repositorio de reporte
	 */
	public UndoReportResourcesUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Método que se encarga de liberar los recursos cuando se deshace la orden.
	 * 
	 * @return DTO de muestra en la vista con el resultado de la operación.
	 */
	public ResponseDTO execute() {
		try {

			Report topReport = reportRepository.popOnProgress();

			if (topReport == null) {
				return new ResponseDTO(false, "No hay reportes activos para deshacer.");
			}

			if (topReport.getAssignedTechnician() != null) {
				topReport.getAssignedTechnician().setStatus(TechnicianStatus.AVAILABLE);
			}
			if (topReport.getAssignedUnit() != null) {
				topReport.getAssignedUnit().setStatus(UnitStatus.AVAILABLE);
			}
			if (topReport.getAssignedKit() != null) {
				topReport.getAssignedKit().setStatus(UnitStatus.AVAILABLE);
			}

			topReport.setAssignedTechnician(null);
			topReport.setAssignedUnit(null);
			topReport.setAssignedKit(null);

			topReport.setStatus(ReportStatus.PENDING);

			reportRepository.registerRevertedReport(topReport);

			return new ResponseDTO(true,
					"Asignación deshecha exitosamente. Los recursos están libres y el reporte ha vuelto a la fila prioritaria.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al intentar deshacer la asignación.");
		}
	}
}
