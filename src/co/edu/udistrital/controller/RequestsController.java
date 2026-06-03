package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.*;
import co.edu.udistrital.model.repositories.*;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.RequestsView;

public class RequestsController {

	private final AppController appController;
	private final RequestsView view;

	private final RegisterReportUseCase registerReportUseCase;
	private final AssignResourcesReportUseCase assignResourcesReportUseCase;
	private final FinishReportInFieldUseCase finishReportInFieldUseCase;
	private final ApproveReportActionUseCase approveReportActionUseCase;
	private final RejectReportActionUseCase rejectReportActionUseCase;
	private final RequestReportCancellationUseCase requestReportCancellationUseCase;
	private final GetSortedAndFilteredReportsUseCase getSortedAndFilteredReportsUseCase;
	private final GetPendingReportsUseCase getPendingReportsUseCase;
	private final GetOnGoingReportsUseCase getOnGoingReportsUseCase;
	private final GetToConfirmReportsUseCase getToConfirmReportsUseCase;
	private final GetNextPendingReportUseCase getNextPendingReportUseCase;
	
	// Casos de uso compartidos necesarios para crear reportes
	private final GetClientByIDUseCase getClientByIDUseCase;
	private final GetAvailableTechnicianByZoneAndProblemUseCase getAvailableTechnicianUseCase;
	private final GetAvailableUnitsByZoneUseCase getAvailableUnitsUseCase;
	private final GetAvailableKitsByTypeUseCase getAvailableKitsUseCase;
	private final GetCriticLevelLabelsUseCase getCriticLevelLabelsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;
	private final GetTechnicianSpecialityLabelsUseCase getSpecialityLabelsUseCase;
	
	public RequestsController(AppController appController, String role, ReportRepository reportRepo, ClientRepository clientRepo, TechnicianRepository techRepo, ServiceUnitRepository unitRepo, KitRepository kitRepo) {
		this.appController = appController;
		this.view = new RequestsView(role);
		this.view.setController(this);

		this.registerReportUseCase = new RegisterReportUseCase(clientRepo, reportRepo);
		this.assignResourcesReportUseCase = new AssignResourcesReportUseCase(reportRepo, techRepo, unitRepo, kitRepo);
		this.finishReportInFieldUseCase = new FinishReportInFieldUseCase(reportRepo);
		this.approveReportActionUseCase = new ApproveReportActionUseCase(reportRepo, kitRepo);
		this.rejectReportActionUseCase = new RejectReportActionUseCase(reportRepo);
		this.requestReportCancellationUseCase = new RequestReportCancellationUseCase(reportRepo);
		this.getSortedAndFilteredReportsUseCase = new GetSortedAndFilteredReportsUseCase(reportRepo);
		this.getPendingReportsUseCase = new GetPendingReportsUseCase(reportRepo);
		this.getOnGoingReportsUseCase = new GetOnGoingReportsUseCase(reportRepo);
		this.getToConfirmReportsUseCase = new GetToConfirmReportsUseCase(reportRepo);
		this.getNextPendingReportUseCase = new GetNextPendingReportUseCase(reportRepo);

		this.getClientByIDUseCase = new GetClientByIDUseCase(clientRepo);
		this.getAvailableTechnicianUseCase = new GetAvailableTechnicianByZoneAndProblemUseCase(techRepo);
		this.getAvailableUnitsUseCase = new GetAvailableUnitsByZoneUseCase(unitRepo);
		this.getAvailableKitsUseCase = new GetAvailableKitsByTypeUseCase(kitRepo);
		this.getCriticLevelLabelsUseCase = new GetCriticLevelLabelsUseCase();
		this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();
		this.getSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
	}

	public RequestsView getView() { return view; }

	public void refreshView() {
		view.clearPanels();

		SimpleList.Iterator<ReportDTO> pendIt = getPendingReportsUseCase.execute().iterador();
		while (pendIt.hasNext()) {
			ReportDTO r = pendIt.Next();
			if (r.canCancel()) view.addReportCard(r, "PENDING");
		}

		SimpleList.Iterator<ReportDTO> ongoIt = getOnGoingReportsUseCase.execute().iterador();
		while (ongoIt.hasNext()) {
			ReportDTO r = ongoIt.Next();
			if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
				view.addReportCard(r, "ONGOING");
			}
		}

		SimpleList.Iterator<ReportDTO> confIt = getToConfirmReportsUseCase.execute().iterador();
		while (confIt.hasNext()) {
			ReportDTO r = confIt.Next();
			if (r != null && r.getTicketID() != null && !r.getTicketID().toString().trim().isEmpty()) {
				view.addReportCard(r, "CONFIRM");
			}
		}
		
		SimpleList.Iterator<ReportDTO> histIt = getSortedAndFilteredReportsUseCase.execute().iterador();
		while (histIt.hasNext()) {
			view.addReportToSummary(histIt.Next());
		}
	}

	// Lógica de Creación Delegada
	public void processNewRequest(String clientId) {
		ClientDTO client = getClientByIDUseCase.execute(clientId);
		if (client == null) {
			appController.triggerAddClientDialog(clientId);
		} else {
			view.showCreateReportDialog(client);
		}
	}

	public void submitReportCreation(String cliId, String desc, String type, String prio, String zone) {
		appController.handleResponse(registerReportUseCase.execute(cliId, desc, type, prio, zone), appController::refreshAllViews);
	}

	public void assignReportResources(String techId, String unitId, String kitId) {
		appController.handleResponse(assignResourcesReportUseCase.execute(techId, unitId, kitId), appController::refreshAllViews);
	}

	public void finishReport(String ticketId) {
		appController.handleResponse(finishReportInFieldUseCase.execute(ticketId), appController::refreshAllViews);
	}

	public void approveReport() { appController.handleResponse(approveReportActionUseCase.execute(), appController::refreshAllViews); }
	public void rejectReport() { appController.handleResponse(rejectReportActionUseCase.execute(), appController::refreshAllViews); }
	public void cancelReport(String ticketId) { appController.handleResponse(requestReportCancellationUseCase.execute(ticketId), appController::refreshAllViews); }

	public ReportDTO getNextPendingReport() { return getNextPendingReportUseCase.execute(); }

	// Sugerencias para listas desplegables
	public SimpleList<EntityItem> getSuggestedTechnicians(String zoneName, String specialtyName) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String zNorm = zoneName != null ? zoneName.replace(" ", "") : "";
		String sNorm = specialtyName != null ? specialtyName.replace(" ", "") : "";
		SimpleList<TechnicianDTO> dtos = getAvailableTechnicianUseCase.execute(zNorm, sNorm);
		if (dtos.getSize() == 0) dtos = getAvailableTechnicianUseCase.execute(zoneName, specialtyName);
		SimpleList.Iterator<TechnicianDTO> it = dtos.iterador();
		while (it.hasNext()) { TechnicianDTO t = it.Next(); list.add(new EntityItem(t.getId(), t.getName())); }
		return list;
	}

	public SimpleList<EntityItem> getSuggestedUnits(String zoneName) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String zNorm = zoneName != null ? zoneName.replace(" ", "") : "";
		SimpleList<ServiceUnitDTO> dtos = getAvailableUnitsUseCase.execute(zNorm);
		if (dtos.getSize() == 0) dtos = getAvailableUnitsUseCase.execute(zoneName);
		SimpleList.Iterator<ServiceUnitDTO> it = dtos.iterador();
		while (it.hasNext()) { ServiceUnitDTO u = it.Next(); list.add(new EntityItem(u.getId(), u.getType())); }
		return list;
	}

	public SimpleList<EntityItem> getSuggestedKits(String problemType) {
		SimpleList<EntityItem> list = new SimpleList<>();
		String pNorm = problemType != null ? problemType.replace(" ", "") : "";
		SimpleList<KitDTO> dtos = getAvailableKitsUseCase.execute(pNorm);
		if (dtos.getSize() == 0) dtos = getAvailableKitsUseCase.execute(problemType);
		SimpleList.Iterator<KitDTO> it = dtos.iterador();
		while (it.hasNext()) { KitDTO k = it.Next(); list.add(new EntityItem(k.getId(), k.getType())); }
		return list;
	}

	public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
	public SimpleList<String> getCriticLevelLabels() { return getCriticLevelLabelsUseCase.execute(); }
	public SimpleList<String> getSpecialityLabels() { 
		return getSpecialityLabelsUseCase.execute(); 
	}
}