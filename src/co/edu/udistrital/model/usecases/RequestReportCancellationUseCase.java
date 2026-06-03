package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;


public class RequestReportCancellationUseCase {

	
	private final ReportRepository reportRepository;

	
	public RequestReportCancellationUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	
	public ResponseDTO execute(String ticketId) {
		try {

			UUID ticketID = UUID.fromString(ticketId);

			Report reportToCancel = reportRepository.extractPendingReport(ticketID);

			if (reportToCancel == null) {
				return new ResponseDTO(false,
						"El siniestro no se encontró en las filas de espera. Es posible que ya haya sido asignado a un técnico o cancelado.");
			}

			if (reportToCancel.getStatus() != ReportStatus.PENDING) {
				return new ResponseDTO(false, "Error: Solo se pueden cancelar siniestros que estén pendientes.");
			}

			reportToCancel.setStatus(ReportStatus.CANCELLED);

			reportRepository.pushToConfirm(reportToCancel);

			return new ResponseDTO(true,
					"La solicitud de cancelación del siniestro ha sido enviada a la bandeja del administrador.");

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "El formato del ID del reporte es inválido.");
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al solicitar la cancelación.");
		}
	}
}