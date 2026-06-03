package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.DatabaseSnapshot;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ProfileRepository;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.utils.BinaryDatabaseManager;

/**
 * Caso de uso encargado de recolectar todas las estructuras de datos de los
 * repositorios, empaquetarlas en un Memento (Snapshot) y delegar su guardado en
 * un archivo binario.
 *
 * @author Juan David Diaz Perez
 */
public class SaveSystemDataUseCase {

	private final ProfileRepository profileRepo;
	private final ClientRepository clientRepo;
	private final TechnicianRepository technicianRepo;
	private final ServiceUnitRepository unitRepo;
	private final KitRepository kitRepo;
	private final ReportRepository reportRepo;

	/**
	 * Constructor que recibe los repositorios desde los cuales se obtendrá
	 * la información para construir el snapshot del sistema.
	 *
	 * @param profileRepo Repositorio de perfiles de usuario.
	 * @param clientRepo Repositorio de clientes.
	 * @param technicianRepo Repositorio de técnicos.
	 * @param unitRepo Repositorio de unidades de servicio.
	 * @param kitRepo Repositorio de kits.
	 * @param reportRepo Repositorio de reportes.
	 */
	public SaveSystemDataUseCase(ProfileRepository profileRepo, ClientRepository clientRepo,
			TechnicianRepository technicianRepo, ServiceUnitRepository unitRepo, KitRepository kitRepo,
			ReportRepository reportRepo) {
		this.profileRepo = profileRepo;
		this.clientRepo = clientRepo;
		this.technicianRepo = technicianRepo;
		this.unitRepo = unitRepo;
		this.kitRepo = kitRepo;
		this.reportRepo = reportRepo;
	}

	/**
	 * Ejecuta el proceso de respaldo del sistema, recopilando la información
	 * almacenada en los repositorios y guardándola en un archivo binario.
	 *
	 * @return Un ResponseDTO indicando si la operación fue exitosa o si ocurrió
	 *         algún error durante el proceso de guardado.
	 */
	public ResponseDTO execute() {
		try {
			DatabaseSnapshot snapshot = new DatabaseSnapshot();

			// 1. Empaquetar Perfiles
			snapshot.setUsersList(profileRepo.getUsersList());

			// 2. Empaquetar Recursos Básicos
			snapshot.setClientList(clientRepo.getClientList());
			snapshot.setTechnicianList(technicianRepo.getTechnicianList());

			snapshot.setUnitList(unitRepo.getUnitList());
			snapshot.setToConfirmUnitStack(unitRepo.getToConfirmStack());

			snapshot.setKitList(kitRepo.getKitList());
			snapshot.setMaintenanceKitStack(kitRepo.getMaintenanceKitStack());

			// 3. Empaquetar Estructuras de Siniestros
			snapshot.setUndoQueue(reportRepo.getUndoQueue());
			snapshot.setHighPriorityQueue(reportRepo.getHighPriorityQueue());
			snapshot.setMediumPriorityQueue(reportRepo.getMediumPriorityQueue());
			snapshot.setLowPriorityQueue(reportRepo.getLowPriorityQueue());

			snapshot.setOnGoingReportStack(reportRepo.getOnGoingReportStack());
			snapshot.setToConfirmReportStack(reportRepo.getToConfirmStack());
			snapshot.setAllHistoricalReports(reportRepo.getAllHistoricalReports());
			
			// 4. Delegar el guardado a la capa de infraestructura
			boolean success = BinaryDatabaseManager.saveSnapshot(snapshot);

			if (success) {
				return new ResponseDTO(true, "Base de datos binaria guardada exitosamente.");
			} else {
				return new ResponseDTO(false, "Ocurrió un error de I/O al intentar escribir el archivo binario.");
			}

		} catch (Exception e) {
			return new ResponseDTO(false, "Error inesperado al empaquetar el estado del sistema: " + e.getMessage());
		}
	}
}