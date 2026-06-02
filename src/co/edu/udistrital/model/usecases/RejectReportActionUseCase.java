package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;

public class RejectReportActionUseCase {
    private final ReportRepository reportRepository;

    public RejectReportActionUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public ResponseDTO execute() {
        try {
            Report topReport = reportRepository.popOnConfirm();
            
            if (topReport == null) {
                return new ResponseDTO(false, "No hay reportes pendientes por rechazar.");
            }

            if (topReport.getStatus() == ReportStatus.CANCELLED) {
                topReport.setStatus(ReportStatus.PENDING);
                reportRepository.registerRevertedReport(topReport);
                return new ResponseDTO(true, "Rechazo de cancelación exitoso. El reporte volvió a la cola operativa.");
            } 
            else if (topReport.getStatus() == ReportStatus.DONE) {
                topReport.setStatus(ReportStatus.ON_GOING);
                reportRepository.pushToOnProgress(topReport);
                return new ResponseDTO(true, "Rechazo de cierre exitoso. El técnico debe volver a completar el trabajo.");
            }

            return new ResponseDTO(false, "Estado de reporte desconocido al intentar rechazar.");
        } catch (Exception e) {
            return new ResponseDTO(false, "Ocurrió un error inesperado al rechazar la acción del reporte.");
        }
    }
}