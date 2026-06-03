package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;


public class ReturnKitToServiceUseCase {

	
	private final KitRepository kitRepository;

	
	public ReturnKitToServiceUseCase(KitRepository kitRepository) {
		this.kitRepository = kitRepository;
	}

	
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