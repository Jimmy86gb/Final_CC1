package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;


public class RetireKitFromMaintenanceUseCase {

	
	private final KitRepository kitRepository;

	
	public RetireKitFromMaintenanceUseCase(KitRepository kitRepository) {
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

			Kit restoredKit = new Kit(topKit.getType(), UnitStatus.INACTIVE);
			restoredKit.setId(topKit.getId());

			boolean isUpdated = kitRepository.updatekit(actualKit, restoredKit);

			if (isUpdated) {
				return new ResponseDTO(true, "El kit " + restoredKit.getId() + " ha sido dado de baja  exitosamente.");
			} else {
				kitRepository.maintainKit(topKit);
				return new ResponseDTO(false, "Error interno. No se pudo eliminar de circulacion el kit.");
			}

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado eliminar de circulacion el kit.");
		}
	}
}
