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


public class LoadSystemDataUseCase {

	private final ProfileRepository profileRepo;
	private final ClientRepository clientRepo;
	private final TechnicianRepository technicianRepo;
	private final ServiceUnitRepository unitRepo;
	private final KitRepository kitRepo;
	private final ReportRepository reportRepo;

	
	public LoadSystemDataUseCase(ProfileRepository profileRepo, ClientRepository clientRepo,
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
			
			DatabaseSnapshot snapshot = BinaryDatabaseManager.loadSnapshot();

			
			
			if (snapshot == null) {
				return new ResponseDTO(true, "No se encontró base de datos previa. El sistema inició en blanco.");
			}

			
			if (snapshot.getUsersList() != null) {
				profileRepo.setUsersList(snapshot.getUsersList());
			}

			
			if (snapshot.getClientList() != null) {
				clientRepo.setClientList(snapshot.getClientList());
			}
			if (snapshot.getTechnicianList() != null) {
				technicianRepo.setTechnicianList(snapshot.getTechnicianList());
			}

			if (snapshot.getUnitList() != null) {
				unitRepo.setUnitList(snapshot.getUnitList());
			}
			if (snapshot.getToConfirmUnitStack() != null) {
				unitRepo.setToConfirmStack(snapshot.getToConfirmUnitStack());
			}

			if (snapshot.getKitList() != null) {
				kitRepo.setKitList(snapshot.getKitList());
			}
			if (snapshot.getMaintenanceKitStack() != null) {
				kitRepo.setMaintenanceKitStack(snapshot.getMaintenanceKitStack());
			}

			
			if (snapshot.getUndoQueue() != null) {
				reportRepo.setUndoQueue(snapshot.getUndoQueue());
			}
			if (snapshot.getHighPriorityQueue() != null) {
				reportRepo.setHighPriorityQueue(snapshot.getHighPriorityQueue());
			}
			if (snapshot.getMediumPriorityQueue() != null) {
				reportRepo.setMediumPriorityQueue(snapshot.getMediumPriorityQueue());
			}
			if (snapshot.getLowPriorityQueue() != null) {
				reportRepo.setLowPriorityQueue(snapshot.getLowPriorityQueue());
			}

			if (snapshot.getOnGoingReportStack() != null) {
				reportRepo.setOnGoingReportStack(snapshot.getOnGoingReportStack());
			}
			if (snapshot.getToConfirmReportStack() != null) {
				reportRepo.setToConfirmStack(snapshot.getToConfirmReportStack());
			}
			
			if (snapshot.getAllHistoricalReports() != null) {
				reportRepo.setAllHistoricalReports(snapshot.getAllHistoricalReports());
			}
			
			return new ResponseDTO(true, "Estado del sistema restaurado exitosamente desde archivo binario.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Error crítico al intentar cargar la base de datos: " + e.getMessage());
		}
	}
}