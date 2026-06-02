package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.enums.TechnicianSpecialty;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID ticketID;
	private Client client;
	private String problemDescription;
	private TechnicianSpecialty problemType;
	private CriticLevel priority;
	private OperationZone reportZone;
	private LocalDateTime reportTime;
	private ReportStatus status;

	private Technician assignedTechnician;
	private ServiceUnit assignedUnit;
	private Kit assignedKit;

	public Report(Client client, String problemDescription, TechnicianSpecialty problemType, CriticLevel priority,
			OperationZone zone) {

		this.ticketID = UUID.randomUUID();
		this.reportTime = LocalDateTime.now();
		this.status = ReportStatus.PENDING;

		this.client = client;
		this.problemDescription = problemDescription;
		this.problemType = problemType;
		this.priority = priority;
		this.reportZone = zone;

		this.assignedTechnician = null;
		this.assignedUnit = null;
		this.assignedKit = null;
	}

	public void setAssignedTechnician(Technician assignedTechnician) {
		this.assignedTechnician = assignedTechnician;
	}

	public void setAssignedUnit(ServiceUnit assignedUnit) {
		this.assignedUnit = assignedUnit;
	}

	public void setAssignedKit(Kit assignedKit) {
		this.assignedKit = assignedKit;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public void setProblemType(TechnicianSpecialty problemType) {
		this.problemType = problemType;
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

	public UUID getTicketID() {
		return ticketID;
	}

	public Client getClient() {
		return client;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public TechnicianSpecialty getProblemType() {
		return problemType;
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

	public ServiceUnit getAssignedUnit() {
		return assignedUnit;
	}

	public Kit getAssignedKit() {
		return assignedKit;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Report report = (Report) obj;
		return Objects.equals(report.ticketID, ticketID);
	}
}