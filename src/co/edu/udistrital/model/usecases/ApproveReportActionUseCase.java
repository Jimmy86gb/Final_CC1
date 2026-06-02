package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
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
            Report topReport = reportRepository.popOnConfirm();
            
            if (topReport == null) {
                return new ResponseDTO(false, "No hay reportes pendientes de confirmación.");
            }
            
            if (topReport.getStatus() == ReportStatus.CANCELLED) {
                return new ResponseDTO(true, "Cancelación aprobada y auditada.");
            } 
            else if (topReport.getStatus() == ReportStatus.DONE) {
                if (topReport.getAssignedTechnician() != null) {
                    topReport.getAssignedTechnician().setStatus(TechnicianStatus.AVAILABLE);
                }
                if (topReport.getAssignedUnit() != null) {
                    topReport.getAssignedUnit().setStatus(UnitStatus.AVAILABLE);
                }
                if (topReport.getAssignedKit() != null) {
                    topReport.getAssignedKit().setStatus(UnitStatus.MAINTENANCE);
                    kitRepository.maintainKit(topReport.getAssignedKit());
                }
                return new ResponseDTO(true, "Trabajo aprobado. Recursos liberados y kit despachado a mantenimiento.");
            }
            return new ResponseDTO(false, "Estado de reporte desconocido.");
        } catch (Exception e) {
            return new ResponseDTO(false, "Ocurrió un error inesperado al aprobar la acción del reporte.");
        }
    }
}