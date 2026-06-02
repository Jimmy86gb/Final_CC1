package co.edu.udistrital.model.usecases;

import java.util.UUID;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

public class FinishReportInFieldUseCase {
    private final ReportRepository reportRepository;

    public FinishReportInFieldUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public ResponseDTO execute(String ticketIdStr) {
        try {
            UUID ticketID = UUID.fromString(ticketIdStr);
            SimpleList<Report> onGoingList = reportRepository.getOnGoingReports();
            Iterator<Report> it = onGoingList.iterador();
            Report targetReport = null;
            
            while (it.hasNext()) {
                Report current = it.Next();
                if (current.getTicketID().equals(ticketID)) {
                    targetReport = current;
                    break;
                }
            }
            
            if (targetReport == null) {
                return new ResponseDTO(false, "El siniestro no se encontró en la lista de progreso.");
            }
            
            reportRepository.removeFinishedReportFromStack(targetReport);
            targetReport.setStatus(ReportStatus.DONE); 
            reportRepository.pushToConfirm(targetReport);
            
            return new ResponseDTO(true, "Tareas finalizadas en campo. Enviado a confirmación.");
        } catch (IllegalArgumentException e) {
            return new ResponseDTO(false, "El formato del ID del reporte es inválido.");
        } catch (Exception e) {
            return new ResponseDTO(false, "Ocurrió un error inesperado al finalizar las tareas.");
        }
    }
}