package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;


public class RejectUnitStatusUseCase {

	
	private final ServiceUnitRepository serviceUnitRepository;

	
	public RejectUnitStatusUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	
	public ResponseDTO execute() {
		try {
			ServiceUnit rejectedDraft = serviceUnitRepository.popOnConfirm();

			if (rejectedDraft == null) {
				return new ResponseDTO(false, "No hay solicitudes pendientes por rechazar.");
			}

			return new ResponseDTO(true, "Solicitud de cambio de estado para la unidad " + rejectedDraft.getId()
					+ " ha sido rechazada exitosamente.");

		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al rechazar la solicitud.");
		}
	}
}
