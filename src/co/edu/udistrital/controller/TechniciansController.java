package co.edu.udistrital.controller;

import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.usecases.*;
import co.edu.udistrital.view.TechniciansView;

/**
 * Sub-controlador encargado de la gestion y administracion de la nomina de tecnicos operativos.
 * Actua como enlace directo entre la vista del directorio (TechniciansView) y los casos de uso 
 * que controlan el registro, actualizacion y filtrado del personal en terreno, garantizando 
 * que la asignacion de zonas y estados se mantenga sincronizada.
 * * @author Jimmy86gb
 */
public class TechniciansController {

	private final AppController appController;
	private final TechniciansView view;

	private final RegisterTechnicianUseCase registerTechnicianUseCase;
	private final UpdateTechnicianUseCase updateTechnicianUseCase;
	private final GetSortedAndDFilteredTechniciansUseCase getSortedAndDFilteredTechniciansUseCase;
	private final GetTechnicianStatusLabelsUseCase getTechnicianStatusLabelsUseCase;
	private final GetTechnicianSpecialityLabelsUseCase getTechnicianSpecialityLabelsUseCase;
	private final GetZoneLabelsUseCase getZoneLabelsUseCase;

	/**
	 * Constructor de la clase TechniciansController.
	 * Inicializa la interfaz visual del directorio y configura todos los casos de uso 
	 * necesarios para la logica de negocio, inyectando el repositorio de tecnicos.
	 * * @param appController Controlador principal para delegar respuestas y sincronizar vistas.
	 * @param role Rol del usuario actual para ajustar los permisos de edicion en la interfaz.
	 * @param techRepo Repositorio en memoria que contiene los registros del personal tecnico.
	 */
	public TechniciansController(AppController appController, String role, TechnicianRepository techRepo) {
		this.appController = appController;
		this.view = new TechniciansView(role);
		this.view.setController(this);

		this.registerTechnicianUseCase = new RegisterTechnicianUseCase(techRepo);
		this.updateTechnicianUseCase = new UpdateTechnicianUseCase(techRepo);
		this.getSortedAndDFilteredTechniciansUseCase = new GetSortedAndDFilteredTechniciansUseCase(techRepo);
		this.getTechnicianStatusLabelsUseCase = new GetTechnicianStatusLabelsUseCase();
		this.getTechnicianSpecialityLabelsUseCase = new GetTechnicianSpecialityLabelsUseCase();
		this.getZoneLabelsUseCase = new GetZoneLabelsUseCase();
	}

	/**
	 * Obtiene la vista asociada a este sub-controlador.
	 * * @return Objeto TechniciansView con la interfaz grafica del directorio de tecnicos.
	 */
	public TechniciansView getView() { return view; }

	/**
	 * Refresca visualmente la tabla del directorio de tecnicos.
	 * Limpia los registros existentes en la vista y la vuelve a poblar iterando sobre 
	 * la estructura de datos filtrada y ordenada provista por el caso de uso.
	 */
	public void refreshView() {
		view.clearTable();
		SimpleList.Iterator<TechnicianDTO> tIt = getSortedAndDFilteredTechniciansUseCase.execute().iterador();
		while (tIt.hasNext()) {
			TechnicianDTO t = tIt.Next();
			view.addTechnician(t.getId().toString(), t.getName(), t.getSpecialty(), t.getStatus(), t.getZone(), t.isEditable());
		}
	}

	/**
	 * Ejecuta el registro de un nuevo tecnico operativo en el sistema, vinculandolo 
	 * a una especialidad base y a una zona geografica de cobertura.
	 * * @param name Nombre completo del tecnico.
	 * @param specialty Especialidad operativa (ej. Grua, Mecanico, Electrico).
	 * @param zone Zona de cobertura asignada por defecto.
	 */
	public void registerTechnician(String name, String specialty, String zone) {
		appController.handleResponse(registerTechnicianUseCase.execute(name, specialty, zone), appController::refreshAllViews);
	}

	/**
	 * Ejecuta la actualizacion de los datos operativos de un tecnico.
	 * Permite reasignar su zona de cobertura o cambiar su estado actual de disponibilidad.
	 * * @param id Identificador inmutable del tecnico.
	 * @param name Nombre del tecnico.
	 * @param specialty Especialidad del tecnico.
	 * @param zone Nueva zona geografica asignada.
	 * @param status Nuevo estado operativo (ej. Disponible, Ocupado, Inactivo).
	 */
	public void updateTechnician(String id, String name, String specialty, String zone, String status) {
		appController.handleResponse(updateTechnicianUseCase.execute(id, name, specialty, zone, status), appController::refreshAllViews);
	}

	/**
	 * Obtiene el listado de etiquetas validas para las especialidades tecnicas.
	 * @return Estructura SimpleList con los nombres de las especialidades.
	 */
	public SimpleList<String> getSpecialityLabels() { return getTechnicianSpecialityLabelsUseCase.execute(); }

	/**
	 * Obtiene el listado de etiquetas validas para los estados operativos del tecnico.
	 * @return Estructura SimpleList con los nombres de los estados disponibles.
	 */
	public SimpleList<String> getTechnicianStatusLabels() { return getTechnicianStatusLabelsUseCase.execute(); }

	/**
	 * Obtiene el listado de etiquetas validas para las zonas geograficas de cobertura.
	 * @return Estructura SimpleList con los nombres de las zonas.
	 */
	public SimpleList<String> getZoneLabels() { return getZoneLabelsUseCase.execute(); }
}