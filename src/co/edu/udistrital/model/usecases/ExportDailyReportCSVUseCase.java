package co.edu.udistrital.model.usecases;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

public class ExportDailyReportCSVUseCase {
    private final ReportRepository reportRepository;

    public ExportDailyReportCSVUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public ResponseDTO execute() {
        SimpleList<Report> allReports = reportRepository.getAllReports();
        Iterator<Report> iterator = allReports.iterador();
        
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fileName = "Reporte_Diario_" + LocalDateTime.now().format(fileFormatter) + ".csv";
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("ID Ticket,Cliente,Prioridad,Zona,Fecha y Hora,Estado,Técnico Asignado,Unidad Asignada,Kit Usado\n");

            while (iterator.hasNext()) {
                Report report = iterator.Next();
                
                String ticket = report.getTicketID().toString().substring(0,8);
                String client = report.getClient().getName();
                String priority = report.getPriority().getDisplayName();
                String zone = report.getReportZone().getDisplayName();
                String date = report.getReportTime().format(dateFormater);
                String status = report.getStatus().getDisplayName();
                String technician = report.getAssignedTechnician() != null ? report.getAssignedTechnician().getName() : "N/A";
                String unit = report.getAssignedUnit() != null ? report.getAssignedUnit().getType().getDisplayName() : "N/A";
                String kit = report.getAssignedKit() != null ? report.getAssignedKit().getType().getDisplayName() : "N/A";

                writer.append(String.join(",", ticket, client, priority, zone, date, status, technician, unit, kit));
                writer.append("\n");
            }
            writer.flush();
            return new ResponseDTO(true, "Reporte exportado exitosamente como: " + fileName);
        } catch (IOException e) {
            return new ResponseDTO(false, "Ocurrió un error al intentar escribir el archivo CSV.");
        }
    }
}