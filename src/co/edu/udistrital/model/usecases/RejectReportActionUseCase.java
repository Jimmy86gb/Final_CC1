package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;

/**
 * Caso de uso para terminar deshacer un cambio de estado de reporte y hacerle
 * pop en la fila de confirmaciones
 *
 * @author Juan David Diaz Perez
 */
public class RejectReportActionUseCase {

	/**
	 * Instancia privada del repositorio de memoria de reportes
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta dentro del caso de uso el repositorio de reportes
	 * 
	 * @param reportRepository Repositorio de reportes
	 */
	public RejectReportActionUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Método que se encarga de deshacer estados de cambio en los reportes y los
	 * devuleve a su sitio original
	 * 
	 * @return DTO de muestra en la vista con el resultado de la operación.
	 */
	public ResponseDTO execute() {
		try {

			Report confirmReport = reportRepository.popOnConfirm();

			if (confirmReport == null) {
				return new ResponseDTO(false, "No hay reportes pendientes en la pila de confirmaciones.");
			}

			if (confirmReport.getStatus() == ReportStatus.DONE) {

				confirmReport.setStatus(ReportStatus.ON_GOING);

				reportRepository.pushToOnProgress(confirmReport);

				return new ResponseDTO(true, "Finalizacion del reporte deshecha con exito.");
			} else if (confirmReport.getStatus() == ReportStatus.CANCELLED) {

				confirmReport.setStatus(ReportStatus.PENDING);

				reportRepository.registerReport(confirmReport);

				return new ResponseDTO(true, "Solicitud de cancelamiento rechazada con exito.");
			}

			return new ResponseDTO(false, "El reporte extraído tiene un estado no reconocible para esta acción.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al intentar auditar el reporte.");
		}
	}
}
