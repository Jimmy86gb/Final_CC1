package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;

/**
 * Caso de uso para finalizar el mantenimiento de un kit (en la cima de la pila
 * LIFO) y devolverlo al servicio como disponible.
 *
 * @author Juan David Diaz Perez
 */
public class ReturnKitToServiceUseCase {

	/**
	 * Instancia del repositorio que maneja la memoria de los kits
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta el repositorio de kits
	 * 
	 * @param kitRepository Repositorio de memoria de kits
	 */
	public ReturnKitToServiceUseCase(KitRepository kitRepository) {
		this.kitRepository = kitRepository;
	}

	/**
	 * Metodo que retorna a circulacion un kit que se encontraba en mantenimiento
	 * 
	 * @return DTO de respuesta para la vista
	 */
	public ResponseDTO execute() {
		try {

			Kit topKit = kitRepository.dispatchKit();

			if (topKit == null) {
				return new ResponseDTO(false, "No hay kits en la pila de mantenimiento.");
			}

			Kit actualKit = kitRepository.getKitByID(topKit.getId());

			if (actualKit == null) {
				return new ResponseDTO(false, "Error: El kit original ya no existe en el sistema.");
			}

			Kit restoredKit = new Kit(topKit.getType(), UnitStatus.AVAILABLE);
			restoredKit.setId(topKit.getId());

			boolean isUpdated = kitRepository.updatekit(actualKit, restoredKit);

			if (isUpdated) {
				return new ResponseDTO(true, "Mantenimiento finalizado. El kit " + restoredKit.getId() + " ahora está "
						+ UnitStatus.AVAILABLE.getDisplayName() + ".");
			} else {
				kitRepository.maintainKit(topKit);
				return new ResponseDTO(false, "Error interno. No se pudo reincorporar el kit a la lista general.");
			}

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al devolver el kit al servicio.");
		}
	}
}