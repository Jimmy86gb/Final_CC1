package co.edu.udistrital.model.dtos;

import java.util.UUID;

/**
 * Clase de transmisión de datos (Data Transfer Object) de la lógica a la vista
 * para la entidad Reporte. Esta versión incluye banderas lógicas para el
 * renderizado dinámico de botones en las tablas de JavaFX según la máquina de
 * estados.
 *
 * @author Juan David Diaz Perez
 */
public class ReportDTO {

	private final UUID ticketID;
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

	/**
	 * Método constructor del DTO de reportes con todos los campos necesarios para
	 * las diferentes vistas del ciclo de vida del siniestro.
	 * 
	 * @param ticketID             ID único del tiquete/reporte actual.
	 * 
	 * @param clientID             ID del cliente relacionado al siniestro.
	 * @param clientName           Nombre del cliente para visualización rápida.
	 * @param clientContact        Número o medio de contacto del cliente.
	 * @param problemDescription   Descripción detallada dada por el cliente.
	 * @param problemType          Especialidad o categoría requerida para el
	 *                             rescate.
	 * @param priority             Nivel de criticidad (Alta, Media, Baja).
	 * @param reportZone           Zona donde ocurrió el siniestro (Ej: Norte, Sur).
	 * @param reportTime           Fecha y hora de registro en formato String.
	 * @param reportStatus         Estado actual del reporte (PENDING, ON_GOING,
	 *                             etc.).
	 * @param technicianID         ID del técnico asignado (null si está PENDING).
	 * @param technicianName       Nombre del técnico asignado (null si está
	 *                             PENDING).
	 * @param technicianSpeciality Especialidad del técnico despachado.
	 * @param unitID               ID de la unidad (moto/grúa) asignada
	 * @param unitType             Typo de la unidad (moto/grúa) asignada.
	 * @param kitID                ID del kit de herramientas/botiquín asignado.
	 * @param canCancel            Habilita el botón "Cancelar" en la vista general.
	 * @param canUndo              Habilita el botón "Deshacer" (retorno a cola).
	 * @param canFinish            Habilita el botón "Finalizar Tareas" en campo.
	 * @param canConfirm           Habilita el botón "Aprobar Cierre" al gerente.
	 */
	public ReportDTO(UUID ticketID, String clientID, String clientName, String clientContact, String problemDescription,
			String problemType, String priority, String reportZone, String reportTime, String reportStatus,
			UUID technicianID, String technicianName, String technicianSpeciality, UUID unitID, UUID kitID,
			String unitType, boolean canCancel, boolean canUndo, boolean canFinish, boolean canConfirm) {

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

	/**
	 * @return ID del tiquete actual. Usado como identificador principal en tablas.
	 */
	public UUID getTicketID() {
		return ticketID;
	}

	/**
	 * @return ID del cliente relacionado.
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * @return Nombre del cliente. Usado para mostrar a quién se va a asistir.
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @return Contacto del cliente para que el técnico pueda llamarlo.
	 */
	public String getClientContact() {
		return clientContact;
	}

	/**
	 * @return Descripción del problema relatado en la llamada.
	 */
	public String getProblemDescription() {
		return problemDescription;
	}

	/**
	 * @return Tipo de problema que define qué especialidad técnica se necesita.
	 */
	public String getProblemType() {
		return problemType;
	}

	/**
	 * @return Prioridad del reporte (Determina en qué cola está formado).
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @return Zona operativa del incidente para asignar recursos cercanos.
	 */
	public String getReportZone() {
		return reportZone;
	}

	/**
	 * @return Fecha y hora de creación del reporte formateada para la UI.
	 */
	public String getReportTime() {
		return reportTime;
	}

	/**
	 * @return Estado actual en la máquina de estados (PENDING, ON_GOING, DONE,
	 *         CANCELED).
	 */
	public String getReportStatus() {
		return reportStatus;
	}

	/**
	 * @return ID del técnico despachado. Retorna null si el caso aún no se asigna.
	 */
	public UUID getTechnicianID() {
		return technicianID;
	}

	/**
	 * @return Nombre del técnico despachado para mostrar en la tabla de activos.
	 */
	public String getTechnicianName() {
		return technicianName;
	}

	/**
	 * @return Especialidad del técnico para verificar correspondencia con el
	 *         problema.
	 */
	public String getTechnicianSpeciality() {
		return technicianSpeciality;
	}

	/**
	 * @return ID de la unidad (Grúa/Moto) despachada.
	 */
	public UUID getUnitID() {
		return unitID;
	}

	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}

	/**
	 * @return the canCancel
	 */
	public boolean isCanCancel() {
		return canCancel;
	}

	/**
	 * @return the canUndo
	 */
	public boolean isCanUndo() {
		return canUndo;
	}

	/**
	 * @return the canFinish
	 */
	public boolean isCanFinish() {
		return canFinish;
	}

	/**
	 * @return the canConfirm
	 */
	public boolean isCanConfirm() {
		return canConfirm;
	}

	/**
	 * @return ID del kit provisto para el servicio.
	 */
	public UUID getKitID() {
		return kitID;
	}

	/**
	 * Indica a la vista si el botón de "Cancelar Siniestro" debe estar activo.
	 * 
	 * @return true si el reporte está PENDING y puede ser cancelado; false de lo
	 *         contrario.
	 */
	public boolean canCancel() {
		return canCancel;
	}

	/**
	 * Indica a la vista si el botón de "Deshacer Asignación (Ctrl+Z)" debe estar
	 * activo.
	 * 
	 * @return true si el reporte está en el tope de la pila On-Going; false de lo
	 *         contrario.
	 */
	public boolean canUndo() {
		return canUndo;
	}

	/**
	 * Indica a la vista si el botón de "Reportar Tareas Finalizadas" debe estar
	 * activo.
	 * 
	 * @return true si el reporte está en proceso (ON_GOING) y el técnico terminó en
	 *         campo.
	 */
	public boolean canFinish() {
		return canFinish;
	}

	/**
	 * Indica a la vista si el botón administrativo de "Aprobar Cierre / Rechazar"
	 * debe estar activo.
	 * 
	 * @return true si el reporte está en el tope de la pila de confirmación; false
	 *         de lo contrario.
	 */
	public boolean canConfirm() {
		return canConfirm;
	}
}