package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitFactory;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;

/**
 * Clase que representa el caso de uso de registrar una nueva unidad de servicio
 * en el sistema.
 *
 * @author Juan David Diaz Perez
 */
public class RegisterServiceUnitUseCase {

	/**
	 * Instancia privada del factory del tipo de unidad
	 */
	private final UnitFactory unitFactory = new UnitFactory();

	/**
	 * Instancia privada del factory de la zona de la unidad
	 */
	private final ZoneFactory zoneFactory = new ZoneFactory();

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
	public RegisterServiceUnitUseCase(ServiceUnitRepository serviceUnitRepository) {
		this.serviceUnitRepository = serviceUnitRepository;
	}

	public ResponseDTO execute(String type, String zone, int quantity) {

		if (quantity <= 0) {
			return new ResponseDTO(false, "La cantidad debe ser mayor a cero.");
		}

		try {

			UnitType unitType = unitFactory.generateUnitType(type);
			OperationZone operationZone = zoneFactory.generateOperationZone(zone);
			int successCount = 0;

			for (int i = 0; i < quantity; i++) {
				ServiceUnit newServiceUnit = new ServiceUnit(unitType, UnitStatus.AVAILABLE, operationZone);

				serviceUnitRepository.saveUnit(newServiceUnit);

				successCount++;
			}

			return new ResponseDTO(true,
					"Se registraron exitosamente " + successCount + " kits de tipo " + unitType.getDisplayName() + ".");

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Tipo de kit inválido: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error al registrar el lote de kits.");
		}
	}
}
