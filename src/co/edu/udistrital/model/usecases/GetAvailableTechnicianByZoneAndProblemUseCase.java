package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.TechnicianDTO;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianFactory;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.TechnicianRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase encargada de ejecutar el caso de uso de devolucion de lista de los
 * tecnicos disponibles por especialidad y zona
 *
 * @author Juan David Diaz Perez
 */
public class GetAvailableTechnicianByZoneAndProblemUseCase {

	/**
	 * Instancia privada del repositorio de los tecnicos en memoria
	 */
	private final TechnicianRepository technicianRepository;

	/**
	 * Instancia del factory de la especializacion del tecnico
	 */
	private final TechnicianFactory technicianFactory = new TechnicianFactory();

	/**
	 * Instancia del factory de la zona de las entidades
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();

	/**
	 * Constructor que inyecta el repositorio de tecnicos al caso de uso
	 * 
	 * @param technicianRepository El repositorio de tecnicos
	 */
	public GetAvailableTechnicianByZoneAndProblemUseCase(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	/**
	 * Metodo encargado de retornar la lista de DTO filtrada para la vista
	 * 
	 * @return Lista basada en TecnicianDTO
	 */
	public SimpleList<TechnicianDTO> execute(String zone, String speciality) {

		SimpleList<TechnicianDTO> resultList = new SimpleList<>();

		OperationZone operationZone = zoneFactory.generateOperationZone(zone);
		TechnicianSpecialty technicianSpecialty = technicianFactory.generaTechnicianSpecialty(speciality);

		SimpleList<Technician> orderedList = technicianRepository.getAvailableTechnicians(operationZone,
				technicianSpecialty);

		Iterator<Technician> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Technician technician = iterator.Next();

			String status = technician.getStatus().getDisplayName();

			TechnicianDTO dto = new TechnicianDTO(technician.getId().toString(), technician.getName(), speciality, zone,
					status, false);

			resultList.add(dto);
		}
		
		

		return resultList;
	}
}
