package co.edu.udistrital.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.enums.TechnicianSpecialty;

/**
 * Entidad que representa un reporte de servicio dentro del sistema.
 * Permite almacenar la información relacionada con la solicitud realizada
 * por un cliente, incluyendo su descripción, prioridad, ubicación, estado
 * y los recursos asignados para su atención.
 * 
 * @author ChrZ
 */
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

	/**
	 * Constructor principal para crear una instancia de Report.
	 * Genera automáticamente un identificador único, registra la fecha
	 * y hora de creación y establece el estado inicial como PENDING.
	 * 
	 * @param client El cliente que realiza el reporte.
	 * @param problemDescription La descripción del problema reportado.
	 * @param problemType El tipo de especialidad técnica requerida.
	 * @param priority El nivel de prioridad asignado al reporte.
	 * @param zone La zona operativa donde se presenta el incidente.
	 */
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

	/**
	 * Asigna o actualiza el técnico responsable del reporte.
	 * 
	 * @param assignedTechnician El técnico asignado.
	 */
	public void setAssignedTechnician(Technician assignedTechnician) {
		this.assignedTechnician = assignedTechnician;
	}

	/**
	 * Asigna o actualiza la unidad de servicio encargada del reporte.
	 * 
	 * @param assignedUnit La unidad de servicio asignada.
	 */
	public void setAssignedUnit(ServiceUnit assignedUnit) {
		this.assignedUnit = assignedUnit;
	}

	/**
	 * Asigna o actualiza el kit de trabajo destinado al reporte.
	 * 
	 * @param assignedKit El kit asignado.
	 */
	public void setAssignedKit(Kit assignedKit) {
		this.assignedKit = assignedKit;
	}

	/**
	 * Actualiza el cliente asociado al reporte.
	 * 
	 * @param client El nuevo cliente asociado.
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Actualiza la descripción del problema reportado.
	 * 
	 * @param problemDescription La nueva descripción del problema.
	 */
	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	/**
	 * Define o cambia el tipo de especialidad técnica requerida.
	 * 
	 * @param problemType La nueva especialidad técnica.
	 */
	public void setProblemType(TechnicianSpecialty problemType) {
		this.problemType = problemType;
	}

	/**
	 * Actualiza el nivel de prioridad del reporte.
	 * 
	 * @param priority La nueva prioridad.
	 */
	public void setPriority(CriticLevel priority) {
		this.priority = priority;
	}

	/**
	 * Actualiza la zona operativa asociada al reporte.
	 * 
	 * @param reportZone La nueva zona operativa.
	 */
	public void setReportZone(OperationZone reportZone) {
		this.reportZone = reportZone;
	}

	/**
	 * Actualiza el estado actual del reporte.
	 * 
	 * @param status El nuevo estado del reporte.
	 */
	public void setStatus(ReportStatus status) {
		this.status = status;
	}

	/**
	 * Obtiene el identificador único del reporte.
	 * 
	 * @return El ID del reporte.
	 */
	public UUID getTicketID() {
		return ticketID;
	}

	/**
	 * Obtiene el cliente asociado al reporte.
	 * 
	 * @return El cliente que realizó el reporte.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Obtiene la descripción del problema reportado.
	 * 
	 * @return La descripción del problema.
	 */
	public String getProblemDescription() {
		return problemDescription;
	}

	/**
	 * Obtiene el tipo de especialidad técnica requerida.
	 * 
	 * @return La especialidad técnica asociada.
	 */
	public TechnicianSpecialty getProblemType() {
		return problemType;
	}

	/**
	 * Obtiene el nivel de prioridad del reporte.
	 * 
	 * @return La prioridad asignada.
	 */
	public CriticLevel getPriority() {
		return priority;
	}

	/**
	 * Obtiene la zona operativa donde se registró el reporte.
	 * 
	 * @return La zona operativa del reporte.
	 */
	public OperationZone getReportZone() {
		return reportZone;
	}

	/**
	 * Obtiene la fecha y hora en que se registró el reporte.
	 * 
	 * @return La fecha y hora de creación del reporte.
	 */
	public LocalDateTime getReportTime() {
		return reportTime;
	}

	/**
	 * Obtiene el estado actual del reporte.
	 * 
	 * @return El estado del reporte.
	 */
	public ReportStatus getStatus() {
		return status;
	}

	/**
	 * Obtiene el técnico asignado al reporte.
	 * 
	 * @return El técnico asignado o null si aún no existe asignación.
	 */
	public Technician getAssignedTechnician() {
		return assignedTechnician;
	}

	/**
	 * Obtiene la unidad de servicio asignada al reporte.
	 * 
	 * @return La unidad asignada o null si aún no existe asignación.
	 */
	public ServiceUnit getAssignedUnit() {
		return assignedUnit;
	}

	/**
	 * Obtiene el kit asignado para la atención del reporte.
	 * 
	 * @return El kit asignado o null si aún no existe asignación.
	 */
	public Kit getAssignedKit() {
		return assignedKit;
	}

	/**
	 * Compara este reporte con otro objeto. La igualdad se determina
	 * exclusivamente mediante la coincidencia de sus identificadores (ticketID).
	 * 
	 * @param obj Objeto a comparar con la instancia actual.
	 * @return true si ambos reportes tienen el mismo ticketID,
	 *         false en caso contrario.
	 */
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