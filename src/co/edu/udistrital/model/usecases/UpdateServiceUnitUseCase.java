package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitFactory;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitStatusFactory;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;

/**
 * Clase de caso de uso que se encarga de actualizar la informacion de las
 * unidades de servicio recibidas desde la vista
 *
 * @author Juan David Diaz Perez
 */
public class UpdateServiceUnitUseCase {

	/**
	 * Instancia privada del factory del tipo de unidad
	 */
	private final UnitFactory unitFactory = new UnitFactory();

	/**
	 * Instancia privada del factory de la zona de la unidad
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();

	/**
	 * Instancia privada del factory de el status de la unidad
	 */
	private final UnitStatusFactory unitStatusFactory = new UnitStatusFactory();

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
	public UpdateServiceUnitUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	/**
	 * Metodo que ejecuta el caso de uso de actualizacion de datos de la unidad de
	 * servicio
	 * 
	 * @param id     ID de ambas unidades de servicio
	 * @param type   Tipo de ambas unidades de servicio
	 * @param status Nuevo status de la unidad de servicio
	 * @param zone   Nueva zona de la unidad de servicio
	 * @return DTO con respuesta para implementar en la vista
	 */
	public ResponseDTO execute(UUID id, String type, String status, String zone) {
		try {

			UnitType unitType = unitFactory.generateUnitType(type);
			OperationZone operationZone = zoneFactory.generateOperationZone(zone);
			UnitStatus unitStatus = unitStatusFactory.generateUnitStatus(status);

			ServiceUnit actualServiceUnit = serviceUnitRepository.getServiceUnitByID(id);

			if (actualServiceUnit == null) {
				return new ResponseDTO(false,
						"La unidad de servicio no se encontró en el sistema. Es posible que haya sido eliminado.");
			}

			ServiceUnit newServiceUnit = new ServiceUnit(unitType, unitStatus, operationZone);

			newServiceUnit.setId(id);

			boolean isUpdated = serviceUnitRepository.update(actualServiceUnit, newServiceUnit);

			if (isUpdated) {
				return new ResponseDTO(true, "La información de la unidad de servicio " + newServiceUnit.getId()
						+ " fue actualizada exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo actualizar la información de la unidad de servicio.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false,
					"Ocurrió un error inesperado al actualizar los datos de la unidad de servicio.");
		}
	}
}
