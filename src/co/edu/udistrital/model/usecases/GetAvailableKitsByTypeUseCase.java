package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

public class GetAvailableKitsByTypeUseCase {
	private final KitRepository kitRepository;

	public GetAvailableKitsByTypeUseCase(KitRepository kitRepository) {
		this.kitRepository = kitRepository;
	}

	/**
	 * Metodo de apoyo factory para convertir la especialidad del tecnico en el tipo
	 * de kit
	 * 
	 * @param technicianSpeciality Especialidad del tecnico en string
	 * @return Enum del tipo del kit
	 */
	private KitType getType(String technicianSpeciality) {
		return switch (technicianSpeciality.replaceAll("\\s+", "").toLowerCase()) {
		case "electricoautomotriz" -> KitType.ELECTRICITY_KIT;
		case "operadordegrua" -> KitType.CRANE_KIT;
		case "mecanicogeneral" -> KitType.GENERAL_KIT;
		case "operariomontallantas" -> KitType.PLUMBING_KIT;
		case "cerrajerodevehiculos" -> KitType.LOCKSMITH_KIT;
		default -> throw new IllegalArgumentException("Tipo de kit no soportado: " + technicianSpeciality);
		};
	}

	/**
	 * Metodo encargado de retornar la lista de DTO para la seleccion de recursos
	 * 
	 * @return Lista basada en kitDTO
	 */
	public SimpleList<KitDTO> execute(String speciality) {

		KitType kitType = getType(speciality);
		String type = kitType.getDisplayName();

		SimpleList<KitDTO> resultList = new SimpleList<KitDTO>();

		SimpleList<Kit> orderedList = kitRepository.getAvailableKitsByType(kitType);

		Iterator<Kit> iterator = orderedList.iterador();

		while (iterator.hasNext()) {
			Kit kit = iterator.Next();

			String status = kit.getStatus().getDisplayName();

			KitDTO dto = new KitDTO(kit.getId().toString(), type, status, false);

			resultList.add(dto);
		}

		return resultList;
	}
}
