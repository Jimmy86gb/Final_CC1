package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ReportDTO;
import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.DashboardView;

public class DashboardController {

	private final AppController appController;
	private final DashboardView view;

	private final GetSortedAndFilteredServiceUnitsUseCase getSortedAndFilteredServiceUnitsUseCase;
	private final GetMaintenanceKitsUseCase getMaintenanceKitsUseCase;
	private final GetSortedAndFilteredReportsUseCase getSortedAndFilteredReportsUseCase;
	private final GenerateDailyCSVUseCase generateDailyCSVUseCase;

	public DashboardController(AppController appController, String role, ServiceUnitRepository serviceUnitRepo, KitRepository kitRepo, ReportRepository reportRepo) {
		this.appController = appController;
		this.view = new DashboardView(role, this); // Asumiendo que espera un DashboardController, actualiza la vista

		this.getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(serviceUnitRepo);
		this.getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepo);
		this.getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepo);
		this.generateDailyCSVUseCase = new GenerateDailyCSVUseCase(reportRepo);
	}

	public DashboardView getView() { return view; }

	public void refreshView() {
		int activeUnits = 0, critical = 0, maint = 0, total = 0;
		
		SimpleList.Iterator<ServiceUnitDTO> uit = getSortedAndFilteredServiceUnitsUseCase.execute().iterador();
		while (uit.hasNext()) {
			if (!uit.Next().getStatus().equalsIgnoreCase("Mantenimiento")) {
				activeUnits++;
			}
		}
		
		maint = getMaintenanceKitsUseCase.execute().getSize();
		
		SimpleList.Iterator<ReportDTO> rit = getSortedAndFilteredReportsUseCase.execute().iterador();
		while (rit.hasNext()) {
			ReportDTO r = rit.Next();
			total++;
			if (r.getPriority() != null && r.getPriority().equalsIgnoreCase("Alta")) {
				critical++;
			}
		}
		
		view.updateStatistics(String.valueOf(activeUnits), String.valueOf(critical), String.valueOf(maint), String.valueOf(total));
	}

	public void exportDailyReport() {
		String defaultPath = System.getProperty("user.dir");
	    
	    appController.handleResponse(generateDailyCSVUseCase.execute(defaultPath), () -> {});
	}
}