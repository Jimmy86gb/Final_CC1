package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.KitsView;

public class KitsController {

	private final AppController appController;
	private final KitsView view;

	private final RegisterKitUseCase registerKitUseCase;
	private final UpdateKitUseCase updateKitUseCase;
	private final GetSortedAndFilteredKitsUseCase getSortedKitsUseCase;
	private final RetireKitFromMaintenanceUseCase retireKitFromMaintenanceUseCase;
	private final ReturnKitToServiceUseCase returnKitToServiceUseCase;
	private final GetKitTypeLabelsUseCase getKitTypeLabelsUseCase;
	private final GetMaintenanceKitsUseCase getMaintenanceKitsUseCase;

	public KitsController(AppController appController, String role, KitRepository kitRepo) {
		this.appController = appController;
		this.view = new KitsView(role);
		this.view.setController(this);

		this.registerKitUseCase = new RegisterKitUseCase(kitRepo);
		this.updateKitUseCase = new UpdateKitUseCase(kitRepo);
		this.getSortedKitsUseCase = new GetSortedAndFilteredKitsUseCase(kitRepo);
		this.retireKitFromMaintenanceUseCase = new RetireKitFromMaintenanceUseCase(kitRepo);
		this.returnKitToServiceUseCase = new ReturnKitToServiceUseCase(kitRepo);
		this.getKitTypeLabelsUseCase = new GetKitTypeLabelsUseCase();
		this.getMaintenanceKitsUseCase = new GetMaintenanceKitsUseCase(kitRepo);
	}

	public KitsView getView() { return view; }

	public void refreshView() {
		view.clearTable();
        
		SimpleList.Iterator<KitDTO> mIt = getMaintenanceKitsUseCase.execute().iterador();
		while (mIt.hasNext()) {
			view.addKit(mIt.Next());
		}
        
		SimpleList.Iterator<KitDTO> kIt = getSortedKitsUseCase.execute().iterador();
		while (kIt.hasNext()) {
			KitDTO kit = kIt.Next();
			if (!kit.getStatus().equalsIgnoreCase("En mantenimiento") && !kit.getStatus().equalsIgnoreCase("Mantenimiento")) {
				view.addKit(kit);
			}
		}
	}

	public void registerKit(String type, int quantity) {
		appController.handleResponse(registerKitUseCase.execute(type, quantity), appController::refreshAllViews);
	}

	public void updateKitToMaintenance(String id, String type) {
		appController.handleResponse(updateKitUseCase.execute(id, type, "Mantenimiento"), appController::refreshAllViews);
	}

	public void updateKitStatus(String id, String type, String newStatus) {
		appController.handleResponse(updateKitUseCase.execute(id, type, newStatus), appController::refreshAllViews);
	}

	public void returnKitToService() {
		appController.handleResponse(returnKitToServiceUseCase.execute(), appController::refreshAllViews);
	}

	public void retireKitFromMaintenance() {
		appController.handleResponse(retireKitFromMaintenanceUseCase.execute(), appController::refreshAllViews);
	}

	public SimpleList<String> getKitTypeLabels() {
		return getKitTypeLabelsUseCase.execute();
	}
}