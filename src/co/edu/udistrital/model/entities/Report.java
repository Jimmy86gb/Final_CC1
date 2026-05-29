package co.edu.udistrital.model.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.ReportStatus;

public class Report {
	
	private UUID ticketID;
	private Client client;
	private String problemDescription;
	private CriticLevel priority;
	private OperationZone reportZone;
	private LocalDateTime reportTime;
	private ReportStatus status;
	
	private Technician assignedTechnician;
	private Kit assignedKit;
	
	public Report(Client client, String problemDescription, CriticLevel priority, OperationZone zone) {
		
		this.ticketID=UUID.randomUUID();
		this.reportTime=LocalDateTime.now();
		this.status=ReportStatus.PENDING;
		
		this.client=client;
		this.problemDescription=problemDescription;
		this.priority=priority;
		this.reportZone=zone;
		
		this.assignedTechnician=null;
		this.assignedKit=null;
	}
	
	//setters
	
	public void setAssignedTechnician(Technician assignedTechnician) {
		this.assignedTechnician=assignedTechnician;
	}
	
	public void setAssignedKit(Kit assignedKit) {
		this.assignedKit=assignedKit;
	}
	
	public void setClient(Client client) {
        this.client = client;
    }
	
	public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }
	
	public void setPriority(CriticLevel priority) {
        this.priority = priority;
    }
	
	public void setReportZone(OperationZone reportZone) {
        this.reportZone = reportZone;
    }
	
	public void setStatus(ReportStatus status) {
        this.status = status;
    }
	
	//getters
	
	public UUID getTicketID() {
        return ticketID;
    }
	
	public Client getClient() {
        return client;
    }
	
	public String getProblemDescription() {
        return problemDescription;
    }
	
	public CriticLevel getPriority() {
        return priority;
    }
	
	public OperationZone getReportZone() {
        return reportZone;
    }
	
	public LocalDateTime getReportTime() {
        return reportTime;
    }
	
	public ReportStatus getStatus() {
        return status;
    }
	
	public Technician getAssignedTechnician() {
        return assignedTechnician;
    }
	
	public Kit getAssignedKit() {
        return assignedKit;
    }
}
