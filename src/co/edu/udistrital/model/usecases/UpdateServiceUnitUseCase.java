package co.edu.udistrital.model.usecases;

import java.util.UUID;
import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.enums.ActionType;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.UnitFactory;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitStatusFactory;
import co.edu.udistrital.model.enums.UnitType;
import co.edu.udistrital.model.enums.ZoneFactory;
import co.edu.udistrital.model.repositories.ActionLogRepository;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;

/**
 * Caso de uso que actualiza la información de las unidades de servicio.
 * Delega los cambios de estado a la pila de confirmación del administrador.
 */
public class UpdateServiceUnitUseCase {

	private final UnitFactory unitFactory = new UnitFactory();
	private final ZoneFactory zoneFactory = new ZoneFactory();
	private final UnitStatusFactory unitStatusFactory = new UnitStatusFactory();
	private final ServiceUnitRepository serviceUnitRepository;
	private final ActionLogRepository logRepo;

	public UpdateServiceUnitUseCase(ServiceUnitRepository serviceUnitRepository, ActionLogRepository logRepo) {
		this.serviceUnitRepository = serviceUnitRepository;
		this.logRepo = logRepo;
	}

	public ResponseDTO execute(String id, String type, String status, String zone) {
		try {
			UUID unitId = UUID.fromString(id);
			UnitType unitType = unitFactory.generateUnitType(type);
			OperationZone operationZone = zoneFactory.generateOperationZone(zone);
			UnitStatus requestedStatus = unitStatusFactory.generateUnitStatus(status);

			ServiceUnit actualServiceUnit = serviceUnitRepository.getServiceUnitByID(unitId);

			if (actualServiceUnit == null) {
				return new ResponseDTO(false, "La unidad de servicio no se encontró en el sistema.");
			}

			logRepo.logAction(new ActionRecord("Actualización de Unidad: "
					+ actualServiceUnit.getId().toString().substring(0, 8), ActionType.UPDATE_UNIT,
					actualServiceUnit.getId().toString(), actualServiceUnit.getType().name(),
					actualServiceUnit.getStatus().name(), actualServiceUnit.getZone().name()));

			// CORRECCIÓN CRÍTICA: Cualquier cambio de estado requiere confirmación del admin.
			// Si el operador solo cambió la zona, se actualiza directamente.
			boolean requiresConfirmation = (actualServiceUnit.getStatus() != requestedStatus);

			// Si requiere confirmación, la lista principal conserva el estado original momentáneamente.
			UnitStatus statusForMainList = requiresConfirmation ? actualServiceUnit.getStatus() : requestedStatus;

			ServiceUnit updatedUnitForMainList = new ServiceUnit(unitType, statusForMainList, operationZone);
			updatedUnitForMainList.setId(unitId);

			boolean isUpdated = serviceUnitRepository.update(actualServiceUnit, updatedUnitForMainList);

			if (isUpdated) {
				if (requiresConfirmation) {
					// Enviar el borrador con el nuevo estado a la pila LIFO
					ServiceUnit unitForConfirmation = new ServiceUnit(unitType, requestedStatus, operationZone);
					unitForConfirmation.setId(unitId);
					serviceUnitRepository.pushToConfirm(unitForConfirmation);

					return new ResponseDTO(true, "Datos guardados. El cambio de estado a "
							+ requestedStatus.getDisplayName() + " fue enviado a la pila de confirmación.");
				}

				return new ResponseDTO(true, "La zona de la unidad de servicio fue actualizada exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo actualizar la información de la unidad de servicio.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al actualizar la unidad de servicio.");
		}
	}
}