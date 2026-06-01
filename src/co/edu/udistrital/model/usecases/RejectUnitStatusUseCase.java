package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;

/**
 * Caso de uso para desaprovar el cambio de estado de una unidad de servicio
 *
 * @author Juan David Diaz Perez
 */
public class RejectUnitStatusUseCase {

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
	public RejectUnitStatusUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	/**
	 * Metodo que se encarga de actualizar el estado de una unidad de servicio
	 * cuando se es denegada
	 * 
	 * @return DTO de muestra en la vista
	 */
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
