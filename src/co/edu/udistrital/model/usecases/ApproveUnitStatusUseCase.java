package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;

/**
 * Caso de uso para aprobar el cambio de estado de una unidad de servicio
 *
 * @author Juan David Diaz Perez
 */
public class ApproveUnitStatusUseCase {

	/**
	 * Instancia del repositorio que manneja la memoria de las unidades de servicio
	 */
	private final ServiceUnitRepository serviceUnitRepository;

	/**
	 * Constructor del caso de uso que inyecta el repositorio de unidades de
	 * servicio
	 * 
	 * @param serviceUnitRepository Repositorio de unidades de servicio
	 */
	public ApproveUnitStatusUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	/**
	 * Metodo que se encarga de actualizar el estado de una unidad de servicio
	 * cuando se es aprovada
	 * 
	 * @return DTO de muestra en la vista
	 */
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
