package co.edu.udistrital.model.usecases;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

public class GetToConfirmReportsUseCase {
    private final ReportRepository reportRepository;

    public GetToConfirmReportsUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public SimpleList<ReportDTO> execute() {
        SimpleList<ReportDTO> resultList = new SimpleList<>();
        SimpleList<Report> confirmList = reportRepository.getToConfirmReports();
        Iterator<Report> iterator = confirmList.iterador();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        boolean isTopElement = true; 

        while (iterator.hasNext()) {
            Report report = iterator.Next();
            
            UUID ticketID = report.getTicketID();
            String clientID = report.getClient().getId();
            String clientName = report.getClient().getName();
            String clientContact = report.getClient().getContactInfo();
            String problemDesc = report.getProblemDescription();
            String problemType = report.getProblemType().getDisplayName();
            String priority = report.getPriority().getDisplayName();
            String zone = report.getReportZone().getDisplayName();
            String time = report.getReportTime().format(formatter);
            String status = report.getStatus().getDisplayName();
            
            UUID techID = report.getAssignedTechnician() != null ? report.getAssignedTechnician().getId() : null;
            String techName = report.getAssignedTechnician() != null ? report.getAssignedTechnician().getName() : "Sin asignar";
            String techSpec = report.getAssignedTechnician() != null ? report.getAssignedTechnician().getSpecialty().getDisplayName() : "N/A";
            UUID unitID = report.getAssignedUnit() != null ? report.getAssignedUnit().getId() : null;
            UUID kitID = report.getAssignedKit() != null ? report.getAssignedKit().getId() : null;

            boolean canCancel = false;
            boolean canUndo = false;
            boolean canFinish = false;
            boolean canConfirm = isTopElement; // Regla de negocio LIFO

            ReportDTO dto = new ReportDTO(ticketID, clientID, clientName, clientContact, problemDesc, problemType,
                    priority, zone, time, status, techID, techName, techSpec, unitID, kitID, 
                    canCancel, canUndo, canFinish, canConfirm);
                    
            resultList.add(dto);
            isTopElement = false;
        }
        return resultList;
    }
}