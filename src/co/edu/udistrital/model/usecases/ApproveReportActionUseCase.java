package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ReportRepository;


public class ApproveReportActionUseCase {

	
	private final ReportRepository reportRepository;

	
	private final KitRepository kitRepository;

	
	public ApproveReportActionUseCase(ReportRepository reportRepository, KitRepository kitRepository) {
		this.reportRepository = reportRepository;
		this.kitRepository = kitRepository;
	}

	
	public ResponseDTO execute() {
		try {

			Report confirmReport = reportRepository.popOnConfirm();

			if (confirmReport == null) {
				return new ResponseDTO(false, "No hay reportes pendientes en la pila de confirmaciones.");
			}

			if (confirmReport.getStatus() == ReportStatus.DONE) {

				if (confirmReport.getAssignedTechnician() != null) {
					confirmReport.getAssignedTechnician().setStatus(TechnicianStatus.AVAILABLE);
				}

				if (confirmReport.getAssignedUnit() != null) {
					confirmReport.getAssignedUnit().setStatus(UnitStatus.AVAILABLE);
				}

				if (confirmReport.getAssignedKit() != null) {
					Kit usedKit = confirmReport.getAssignedKit();
					usedKit.setStatus(UnitStatus.MAINTENANCE);
					kitRepository.maintainKit(usedKit);
				}

				return new ResponseDTO(true,
						"Reporte aprobado como DONE. Recursos liberados y Kit enviado a mantenimiento.");
			} else if (confirmReport.getStatus() == ReportStatus.CANCELLED) {
				return new ResponseDTO(true, "Cancelación aprobada. El siniestro ha sido archivado como CANCELLED.");
			}

			return new ResponseDTO(false, "El reporte extraído tiene un estado no reconocible para esta acción.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al intentar auditar el reporte.");
		}
	}
}
