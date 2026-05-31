package co.edu.udistrital.dtos;

import java.util.UUID;

/**
 * Clase de transmision de datos de logica a vista del reporte
 *
 * @author Juan David Diaz Perez
 */
public class ReportDTO {

	private final UUID ticketID;
	private final String clientID;
	private final String clientName;
	private final String clientContact;
	private final String problemDescription;
	private final String priority;
	private final String reportZone;
	private final String reportTime;
	private final String reportStatus;
	private final UUID technicianID;
	private final String technicianName;
	private final String technicianSpeciality;
	private final UUID kitID;

	/**
	 * Metodo constructor del DTO de reportes
	 * 
	 * @param ticketID             ID del tiquete actual
	 * @param clientID             ID del cliente relacionado al tiquete
	 * @param clientName           Nombre del cliente relacionado al tiquete
	 * @param clientContact        Contacto del cliente relacionado al tiquete
	 * @param problemDescription   Descripcion del problema del reporte
	 * @param priority             Prioridad del reporte actual
	 * @param reportZone           Zona del reporte actual
	 * @param reportTime           Tiempo del reporte actual
	 * @param reportStatus         Status del reporte actual
	 * @param technicianID         ID del tecnico relacionado al reporte
	 * @param technicianName       Nombre del tecnico relacionado al reporte
	 * @param technicianSpeciality Especialidad del tecnico relacionado al reporte
	 * @param kitID                ID del kit utilizado en el reporte
	 */
	public ReportDTO(UUID ticketID, String clientID, String clientName, String clientContact, String problemDescription,
			String priority, String reportZone, String reportTime, String reportStatus, UUID technicianID,
			String technicianName, String technicianSpeciality, UUID kitID) {
		super();
		this.ticketID = ticketID;
		this.clientID = clientID;
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.problemDescription = problemDescription;
		this.priority = priority;
		this.reportZone = reportZone;
		this.reportTime = reportTime;
		this.reportStatus = reportStatus;
		this.technicianID = technicianID;
		this.technicianName = technicianName;
		this.technicianSpeciality = technicianSpeciality;
		this.kitID = kitID;
	}

	/**
	 * @return ID del tiquete actual
	 */
	public UUID getTicketID() {
		return ticketID;
	}

	/**
	 * @return ID del cliente relacionado al tiquete
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * @return Nombre del cliente relacionado al tiquete
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @return Contacto del cliente relacionado al tiquete
	 */
	public String getClientContact() {
		return clientContact;
	}

	/**
	 * @return Descripcion del problema del reporte
	 */
	public String getProblemDescription() {
		return problemDescription;
	}

	/**
	 * @return Prioridad del reporte actual
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @return Zona del reporte actual
	 */
	public String getReportZone() {
		return reportZone;
	}

	/**
	 * @return Tiempo del reporte actual
	 */
	public String getReportTime() {
		return reportTime;
	}

	/**
	 * @return Status del reporte actual
	 */
	public String getReportStatus() {
		return reportStatus;
	}

	/**
	 * @return ID del tecnico relacionado al reporte
	 */
	public UUID getTechnicianID() {
		return technicianID;
	}

	/**
	 * @return Nombre del tecnico relacionado al reporte
	 */
	public String getTechnicianName() {
		return technicianName;
	}

	/**
	 * @return Especialidad del tecnico relacionado al reporte
	 */
	public String getTechnicianSpeciality() {
		return technicianSpeciality;
	}

	/**
	 * @return ID del kit utilizado en el reporte
	 */
	public UUID getKitID() {
		return kitID;
	}
}
