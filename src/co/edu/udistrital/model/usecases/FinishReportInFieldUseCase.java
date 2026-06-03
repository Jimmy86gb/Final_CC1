package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;


public class FinishReportInFieldUseCase {

	
	private final ReportRepository reportRepository;

	
	public FinishReportInFieldUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	
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
