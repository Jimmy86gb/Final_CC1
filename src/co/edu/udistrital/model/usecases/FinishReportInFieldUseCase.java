package co.edu.udistrital.model.usecases;

import java.util.UUID;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;

/**
 * Caso de uso para terminar un reporte y mandarlo a la pila de confirmaciones
 *
 * @author Juan David Diaz Perez
 */
public class FinishReportInFieldUseCase {

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
	public FinishReportInFieldUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Método que se encarga de terminar una orden y mandalo a la pila de
	 * confirmaciones
	 * 
	 * @return DTO de muestra en la vista con el resultado de la operación.
	 */
	public ResponseDTO execute(String reportID) {
		try {

			UUID id = UUID.fromString(reportID);

			Report finishedReport = reportRepository.removeFinishedReportFromStack(id);

			if (finishedReport == null) {
				return new ResponseDTO(false, "El reporte no se encuentra en la pila on going");
			}

			finishedReport.setStatus(ReportStatus.DONE);

			reportRepository.pushToConfirm(finishedReport);

			return new ResponseDTO(true, "Reporte finalizado correctamente, redirigido a la pila de confirmacion");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al intentar terminar el reporte.");
		}
	}
}
