package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.ServiceUnitDTO;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.UnitsView;

public class UnitsController {

	private final AppController appController;
	private final UnitsView view;

	private final RegisterServiceUnitUseCase registerServiceUnitUseCase;
	private final UpdateServiceUnitUseCase updateServiceUnitUseCase;
	private final ApproveUnitStatusUseCase approveUnitStatusUseCase;
	private final RejectUnitStatusUseCase rejectUnitStatusUseCase;
	private final GetToConfirmServiceUnitsUseCase getToConfirmServiceUnitsUseCase;
	private final GetServiceUnitsStatusLabelsUseCase getServiceUnitsStatusLabelsUseCase;
	private final GetServiceUnitsTypeLabelsUseCase getServiceUnitsTypeLabelsUseCase;
	private final GetSortedAndFilteredServiceUnitsUseCase getSortedAndFilteredServiceUnitsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;

	public UnitsController(AppController appController, String role, ServiceUnitRepository unitRepo) {
		this.appController = appController;
		this.view = new UnitsView(role);
		this.view.setController(this);

		this.registerServiceUnitUseCase = new RegisterServiceUnitUseCase(unitRepo);
		this.updateServiceUnitUseCase = new UpdateServiceUnitUseCase(unitRepo);
		this.approveUnitStatusUseCase = new ApproveUnitStatusUseCase(unitRepo);
		this.rejectUnitStatusUseCase = new RejectUnitStatusUseCase(unitRepo);
		this.getToConfirmServiceUnitsUseCase = new GetToConfirmServiceUnitsUseCase(unitRepo);
		this.getServiceUnitsStatusLabelsUseCase = new GetServiceUnitsStatusLabelsUseCase();
		this.getServiceUnitsTypeLabelsUseCase = new GetServiceUnitsTypeLabelsUseCase();
		this.getSortedAndFilteredServiceUnitsUseCase = new GetSortedAndFilteredServiceUnitsUseCase(unitRepo);
		this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();
	}

	public UnitsView getView() { return view; }

	public void refreshView() {
		view.clearTable();
        
		SimpleList<ServiceUnitDTO> pendingUnits = getToConfirmServiceUnitsUseCase.execute();
		SimpleList<ServiceUnitDTO> allUnits = getSortedAndFilteredServiceUnitsUseCase.execute();
        
		SimpleList.Iterator<ServiceUnitDTO> uIt = allUnits.iterador();
		while (uIt.hasNext()) {
			ServiceUnitDTO activeUnit = uIt.Next();
			boolean isPending = false;
            
			SimpleList.Iterator<ServiceUnitDTO> checkIt = pendingUnits.iterador();
			while (checkIt.hasNext()) {
				if (checkIt.Next().getId().equals(activeUnit.getId())) {
					isPending = true;
					break;
				}
			}
            
			if (!isPending) {
				view.addUnit(activeUnit, false);
			}
		}
        
		SimpleList.Iterator<ServiceUnitDTO> pIt = pendingUnits.iterador();
		while (pIt.hasNext()) {
			view.addUnit(pIt.Next(), true);
		}
	}

	public void registerUnit(String type, String zone, int quantity) {
		appController.handleResponse(registerServiceUnitUseCase.execute(type, zone, quantity), appController::refreshAllViews);
	}

	public void updateServiceUnit(String id, String type, String status, String zone) {
		appController.handleResponse(updateServiceUnitUseCase.execute(id, type, status, zone), appController::refreshAllViews);
	}

	public void approveUnitStatus() {
		appController.handleResponse(approveUnitStatusUseCase.execute(), appController::refreshAllViews);
	}

	public void rejectUnitStatus() {
		appController.handleResponse(rejectUnitStatusUseCase.execute(), appController::refreshAllViews);
	}

	public SimpleList<String> getUnitTypeLabels() { return getServiceUnitsTypeLabelsUseCase.execute(); }
	public SimpleList<String> getUnitStatusLabels() { return getServiceUnitsStatusLabelsUseCase.execute(); }
	public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
}