package co.edu.udistrital.model.dtos;

import java.util.UUID;


public class ReportDTO {

	private final String ticketID;
	private final String clientID;
	private final String clientName;
	private final String clientContact;
	private final String problemDescription;
	private final String problemType;
	private final String priority;
	private final String reportZone;
	private final String reportTime;
	private final String reportStatus;

	private final UUID technicianID;
	private final String technicianName;
	private final String technicianSpeciality;
	private final UUID unitID;
	private final String unitType;
	private final UUID kitID;

	private final boolean canCancel;
	private final boolean canUndo;
	private final boolean canFinish;
	private final boolean canConfirm;

	
	public ReportDTO(String ticketID, String clientID, String clientName, String clientContact,
			String problemDescription, String problemType, String priority, String reportZone, String reportTime,
			String reportStatus, UUID technicianID, String technicianName, String technicianSpeciality, UUID unitID,
			String unitType, UUID kitID, boolean canCancel, boolean canUndo, boolean canFinish, boolean canConfirm) {

		this.ticketID = ticketID;
		this.clientID = clientID;
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.problemDescription = problemDescription;
		this.problemType = problemType;
		this.priority = priority;
		this.reportZone = reportZone;
		this.reportTime = reportTime;
		this.reportStatus = reportStatus;

		this.technicianID = technicianID;
		this.technicianName = technicianName;
		this.technicianSpeciality = technicianSpeciality;
		this.unitID = unitID;
		this.unitType = unitType;
		this.kitID = kitID;

		this.canCancel = canCancel;
		this.canUndo = canUndo;
		this.canFinish = canFinish;
		this.canConfirm = canConfirm;
	}

	
	public String getTicketID() {
		return ticketID;
	}

	
	public String getClientID() {
		return clientID;
	}

	
	public String getClientName() {
		return clientName;
	}

	
	public String getClientContact() {
		return clientContact;
	}

	
	public String getProblemDescription() {
		return problemDescription;
	}

	
	public String getProblemType() {
		return problemType;
	}

	
	public String getPriority() {
		return priority;
	}

	
	public String getReportZone() {
		return reportZone;
	}

	
	public String getReportTime() {
		return reportTime;
	}

	
	public String getReportStatus() {
		return reportStatus;
	}

	
	public UUID getTechnicianID() {
		return technicianID;
	}

	
	public String getTechnicianName() {
		return technicianName;
	}

	
	public String getTechnicianSpeciality() {
		return technicianSpeciality;
	}

	
	public UUID getUnitID() {
		return unitID;
	}

	
	public String getUnitType() {
		return unitType;
	}

	
	public boolean isCanCancel() {
		return canCancel;
	}

	
	public boolean isCanUndo() {
		return canUndo;
	}

	
	public boolean isCanFinish() {
		return canFinish;
	}

	
	public boolean isCanConfirm() {
		return canConfirm;
	}

	
	public UUID getKitID() {
		return kitID;
	}

	
	public boolean canCancel() {
		return canCancel;
	}

	
	public boolean canUndo() {
		return canUndo;
	}

	
	public boolean canFinish() {
		return canFinish;
	}

	
	public boolean canConfirm() {
		return canConfirm;
	}
}