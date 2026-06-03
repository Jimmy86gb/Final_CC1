package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;


public class RejectReportActionUseCase {

	private final ReportRepository reportRepository;

	public RejectReportActionUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

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

				
				if (confirmReport.getPriority() == CriticLevel.HIGH) {
					reportRepository.getHighPriorityQueue().enqueue(confirmReport);
				} else if (confirmReport.getPriority() == CriticLevel.MEDIUM) {
					reportRepository.getMediumPriorityQueue().enqueue(confirmReport);
				} else {
					reportRepository.getLowPriorityQueue().enqueue(confirmReport);
				}

				return new ResponseDTO(true, "Solicitud de cancelación rechazada. El siniestro ha vuelto a la cola de atención.");
			}

			return new ResponseDTO(false, "El reporte extraído tiene un estado no reconocible para esta acción.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al intentar auditar el reporte.");
		}
	}
}