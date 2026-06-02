package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;

/**
 * Caso de uso que orquesta la asignación manual de recursos a un siniestro.
 * Bloquea los recursos cambiándolos de estado y mueve el reporte a la pila de
 * emergencias activas (On-Going).
 *
 * @author Juan David Diaz Perez
 */
public class AssignResourcesReportUseCase {

	/**
	 * Instancia privada del repositorio de memoria de reportes
	 */
	private final ReportRepository reportRepository;

	/**
	 * Instancia privada del repositorio de memoria de tecnicos
	 */
	private final TechnicianRepository technicianRepository;

	/**
	 * Instancia privada del repositorio de memoria de unidades de servicio
	 */
	private final ServiceUnitRepository serviceUnitRepository;

	/**
	 * Instancia privada del repositorio de memoria de kits
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta al caso de uso todos los repositotios necesarios en
	 * la logica
	 * 
	 * @param reportRepository      Repositorio de reporte
	 * @param technicianRepository  Repositorio de tecnico
	 * @param serviceUnitRepository Repositorio de unidades de servicio
	 * @param kitRepository         Repositorio de kits
	 */
	public AssignResourcesReportUseCase(ReportRepository reportRepository, TechnicianRepository technicianRepository,
			ServiceUnitRepository serviceUnitRepository, KitRepository kitRepository) {
		this.reportRepository = reportRepository;
		this.technicianRepository = technicianRepository;
		this.serviceUnitRepository = serviceUnitRepository;
		this.kitRepository = kitRepository;
	}

	/**
	 * Ejecuta la asignación transaccional de la emergencia.
	 * 
	 * @param reportIdStr ID del reporte a atender.
	 * @param techIdStr   ID del técnico seleccionado.
	 * @param unitIdStr   ID de la unidad de servicio seleccionada.
	 * @param kitIdStr    ID del kit seleccionado.
	 * @return DTO con el resultado de la operación.
	 */
	public ResponseDTO execute(String techIdStr, String unitIdStr, String kitIdStr) {
		try {

			UUID techID = UUID.fromString(techIdStr);
			UUID unitID = UUID.fromString(unitIdStr);
			UUID kitID = UUID.fromString(kitIdStr);

			Report targetReport = reportRepository.getNextPendingReport();

			if (targetReport == null || targetReport.getStatus() != ReportStatus.PENDING) {
				return new ResponseDTO(false, "El siniestro ya no está disponible en las colas de espera.");
			}

			Technician selectedTech = technicianRepository.getTechnicianByID(techID);
			ServiceUnit selectedUnit = serviceUnitRepository.getServiceUnitByID(unitID);
			Kit selectedKit = kitRepository.getKitByID(kitID);

			if (selectedTech == null || selectedTech.getStatus() != TechnicianStatus.AVAILABLE) {
				reportRepository.registerRevertedReport(targetReport);
				return new ResponseDTO(false, "El técnico seleccionado ya no está disponible.");
			}
			if (selectedUnit == null || selectedUnit.getStatus() != UnitStatus.AVAILABLE) {
				reportRepository.registerRevertedReport(targetReport);
				return new ResponseDTO(false, "La unidad seleccionada ya no está disponible.");
			}
			if (selectedKit == null || selectedKit.getStatus() != UnitStatus.AVAILABLE) {
				reportRepository.registerRevertedReport(targetReport);
				return new ResponseDTO(false, "El kit seleccionado ya no está disponible.");
			}

			selectedTech.setStatus(TechnicianStatus.BUSY);
			selectedUnit.setStatus(UnitStatus.ASSIGNED);
			selectedKit.setStatus(UnitStatus.ASSIGNED);

			targetReport.setAssignedTechnician(selectedTech);
			targetReport.setAssignedUnit(selectedUnit);
			targetReport.setAssignedKit(selectedKit);
			targetReport.setStatus(ReportStatus.ON_GOING);

			reportRepository.pushToOnProgress(targetReport);

			return new ResponseDTO(true, "Siniestro asignado exitosamente. Los recursos han sido despachados.");

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Uno o más identificadores seleccionados tienen un formato inválido.");
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado durante el despacho de la emergencia.");
		}
	}
}