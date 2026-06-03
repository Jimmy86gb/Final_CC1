package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.repositories.ActionLogRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.TechniciansView;


public class TechniciansController {

	private final AppController appController;
	private final TechniciansView view;

	private final RegisterTechnicianUseCase registerTechnicianUseCase;
	private final UpdateTechnicianUseCase updateTechnicianUseCase;
	private final GetSortedAndDFilteredTechniciansUseCase getSortedAndDFilteredTechniciansUseCase;
	private final GetTechnicianStatusLabelsUseCase getTechnicianStatusLabelsUseCase;
	private final GetTechnicianSpecialityLabelsUseCase getTechnicianSpecialityLabelsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;

	
	public TechniciansController(AppController appController, String role, TechnicianRepository techRepo,
		ActionLogRepository actionLogRepository) {
		this.appController = appController;
		this.view = new TechniciansView(role);
		this.view.setController(this);

		this.registerTechnicianUseCase = new RegisterTechnicianUseCase(techRepo);
		this.updateTechnicianUseCase = new UpdateTechnicianUseCase(techRepo, actionLogRepository);
		this.getSortedAndDFilteredTechniciansUseCase = new GetSortedAndDFilteredTechniciansUseCase(techRepo);
		this.getTechnicianStatusLabelsUseCase = new GetTechnicianStatusLabelsUseCase();
		this.getTechnicianSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
		this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();
	}

	
	public TechniciansView getView() { return view; }

	
	public void refreshView() {
		view.clearTable();
		SimpleList.Iterator<TechnicianDTO> tIt = getSortedAndDFilteredTechniciansUseCase.execute().iterador();
		while (tIt.hasNext()) {
			TechnicianDTO t = tIt.Next();
			view.addTechnician(t.getId().toString(), t.getName(), t.getSpecialty(), t.getStatus(), t.getZone(), t.isEditable());
		}
	}

	
	public void registerTechnician(String name, String specialty, String zone) {
		appController.handleResponse(registerTechnicianUseCase.execute(name, specialty, zone), appController::refreshAllViews);
	}

	
	public void updateTechnician(String id, String name, String specialty, String zone, String status) {
		appController.handleResponse(updateTechnicianUseCase.execute(id, name, specialty, zone, status), appController::refreshAllViews);
	}

	
	public SimpleList<String> getSpecialityLabels() { return getTechnicianSpecialityLabelsUseCase.execute(); }

	
	public SimpleList<String> getTechnicianStatusLabels() { return getTechnicianStatusLabelsUseCase.execute(); }

	
	public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
}