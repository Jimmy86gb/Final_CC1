package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;


public class ApproveUnitStatusUseCase {

	
	private final ServiceUnitRepository serviceUnitRepository;

	
	public ApproveUnitStatusUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	
	public ResponseDTO execute() {
		try {
			ServiceUnit unitDraft = serviceUnitRepository.popOnConfirm();

			if (unitDraft == null) {
				return new ResponseDTO(false, "No hay solicitudes de cambio de estado pendientes.");
			}

			ServiceUnit actualUnit = serviceUnitRepository.getServiceUnitByID(unitDraft.getId());

			if (actualUnit == null) {
				return new ResponseDTO(false, "Error: La unidad original ya no existe en el sistema.");
			}

			boolean isUpdated = serviceUnitRepository.update(actualUnit, unitDraft);

			if (isUpdated) {
				return new ResponseDTO(true,
						"Cambio de estado aprobado. La unidad ahora está " + unitDraft.getStatus().name() + ".");
			} else {
				return new ResponseDTO(false, "Error interno al aplicar el cambio en la memoria.");
			}

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al aprobar el cambio de estado.");
		}
	}
}
