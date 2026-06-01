package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista completa
 * ordenada con opciones de eliminar segun especificos de tecnicos
 *
 * @author Juan David Diaz Perez
 */
public class GetSortedAndDFilteredTechniciansUseCase {

	/**
	 * Instancia privada del repositorio de los tecnicos en memoria
	 */
	private final TechnicianRepository technicianRepository;

	/**
	 * Constructor que inyecta el repositorio de tecnicos al caso de uso
	 * 
	 * @param technicianRepository El repositorio de tecnicos
	 */
	public GetSortedAndDFilteredTechniciansUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	/**
	 * Metodo que retorna una lista ordenada segun se requiera en el caso de uso
	 * 
	 * @return Lista ordenada
	 */
	private SimpleList<Technician> buildOrderedList() {
		SimpleList<Technician> orderedList = new SimpleList<Technician>();

		// Definimos el orden estricto en el que queremos que aparezcan las
		// especialidades
		TechnicianSpecialty[] specialtiesOrder = { TechnicianSpecialty.AUTOMOTIVE_ELECTRICITY,
				TechnicianSpecialty.GENERAL_MECHANICS, TechnicianSpecialty.VEHICULAR_LOCKSMITH,
				TechnicianSpecialty.CRANE_OPERATION, TechnicianSpecialty.PLUMBING };

		// Definimos el orden estricto en el que queremos que aparezcan los estados
		TechnicianStatus[] statusOrder = { TechnicianStatus.AVAILABLE, TechnicianStatus.INACTIVE,
				TechnicianStatus.BUSY };

		// Iteramos cruzando ambas condiciones
		for (int i = 0; i < specialtiesOrder.length; i++) {
			for (int j = 0; j < statusOrder.length; j++) {

				// Consultamos al repositorio por esta combinación específica
				SimpleList<Technician> subList = technicianRepository
						.getTechnicianBySpecialityAndStatuSimpleList(specialtiesOrder[i], statusOrder[j]);

				Iterator<Technician> iterator = subList.iterador();

				// Agregamos los resultados encontrados a la lista maestra
				while (iterator.hasNext()) {
					orderedList.add(iterator.Next());
				}
			}
		}

		return orderedList;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la vista ya ordenada
	 * 
	 * @return Lista basada en kitDTO
	 */
	public SimpleList<TechnicianDTO> execute() {

		SimpleList<TechnicianDTO> resultList = new SimpleList<>();

		SimpleList<Technician> orderedList = buildOrderedList();

		Iterator<Technician> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Technician technician = iterator.Next();

			boolean canEdit = (technician.getStatus() == TechnicianStatus.AVAILABLE
					|| technician.getStatus() == TechnicianStatus.INACTIVE);

			String speciality = technician.getSpecialty().getDisplayName();
			String status = technician.getStatus().getDisplayName();
			String zone = technician.getZone().getDisplayName();

			TechnicianDTO dto = new TechnicianDTO(technician.getId(), zone, speciality, zone, status, canEdit);

			resultList.add(dto);
		}

		return resultList;
	}
}
