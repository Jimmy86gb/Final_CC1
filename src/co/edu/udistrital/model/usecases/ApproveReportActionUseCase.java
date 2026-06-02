package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ReportRepository;

/**
 * Caso de uso para terminar confirmar un cambio de estado de reporte y hacerle
 * pop en la fila de confirmaciones
 *
 * @author Juan David Diaz Perez
 */
public class ApproveReportActionUseCase {

	/**
	 * Instancia privada del repositorio de memoria de reportes
	 */
	private final ReportRepository reportRepository;

	/**
	 * Instancia privada del repositorio de memoria de kits;
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta al caso de uso todos los repositotios necesarios en
	 * la logica
	 * 
	 * @param reportRepository Repositorio de reporte
	 * @param kitRepository    Repositorio de kits
	 */
	public ApproveReportActionUseCase(ReportRepository reportRepository, KitRepository kitRepository) {
		this.reportRepository = reportRepository;
		this.kitRepository = kitRepository;
	}

	/**
	 * Método que se encarga de guardar estados definitivamente y liberar recursos
	 * si es un reporte terminado
	 * 
	 * @return DTO de muestra en la vista con el resultado de la operación.
	 */
	public ResponseDTO execute() {
		try {

			Report confirmReport = reportRepository.popOnConfirm();

			if (confirmReport == null) {
				return new ResponseDTO(false, "No hay reportes pendientes en la pila de confirmaciones.");
			}

			if (confirmReport.getStatus() == ReportStatus.DONE) {

				if (confirmReport.getAssignedTechnician() != null) {
					confirmReport.getAssignedTechnician().setStatus(TechnicianStatus.AVAILABLE);
				}

				if (confirmReport.getAssignedUnit() != null) {
					confirmReport.getAssignedUnit().setStatus(UnitStatus.AVAILABLE);
				}

				if (confirmReport.getAssignedKit() != null) {
					Kit usedKit = confirmReport.getAssignedKit();
					usedKit.setStatus(UnitStatus.MAINTENANCE);
					kitRepository.maintainKit(usedKit);
				}

				return new ResponseDTO(true,
						"Reporte aprobado como DONE. Recursos liberados y Kit enviado a mantenimiento.");
			} else if (confirmReport.getStatus() == ReportStatus.CANCELLED) {
				return new ResponseDTO(true, "Cancelación aprobada. El siniestro ha sido archivado como CANCELLED.");
			}

			return new ResponseDTO(false, "El reporte extraído tiene un estado no reconocible para esta acción.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al intentar auditar el reporte.");
		}
	}
}
