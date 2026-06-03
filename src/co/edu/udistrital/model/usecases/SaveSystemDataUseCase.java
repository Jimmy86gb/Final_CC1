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


public class SaveSystemDataUseCase {

	private final ProfileRepository profileRepo;
	private final ClientRepository clientRepo;
	private final TechnicianRepository technicianRepo;
	private final ServiceUnitRepository unitRepo;
	private final KitRepository kitRepo;
	private final ReportRepository reportRepo;

	
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

	
	public ResponseDTO execute() {
		try {
			DatabaseSnapshot snapshot = new DatabaseSnapshot();

			
			snapshot.setUsersList(profileRepo.getUsersList());

			
			snapshot.setClientList(clientRepo.getClientList());
			snapshot.setTechnicianList(technicianRepo.getTechnicianList());

			snapshot.setUnitList(unitRepo.getUnitList());
			snapshot.setToConfirmUnitStack(unitRepo.getToConfirmStack());

			snapshot.setKitList(kitRepo.getKitList());
			snapshot.setMaintenanceKitStack(kitRepo.getMaintenanceKitStack());

			
			snapshot.setUndoQueue(reportRepo.getUndoQueue());
			snapshot.setHighPriorityQueue(reportRepo.getHighPriorityQueue());
			snapshot.setMediumPriorityQueue(reportRepo.getMediumPriorityQueue());
			snapshot.setLowPriorityQueue(reportRepo.getLowPriorityQueue());

			snapshot.setOnGoingReportStack(reportRepo.getOnGoingReportStack());
			snapshot.setToConfirmReportStack(reportRepo.getToConfirmStack());
			snapshot.setAllHistoricalReports(reportRepo.getAllHistoricalReports());
			
			
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