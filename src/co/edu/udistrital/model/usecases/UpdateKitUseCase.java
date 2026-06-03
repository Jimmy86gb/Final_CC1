package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.ActionType;
import co.edu.udistrital.model.enums.KitFactory;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitStatusFactory;
import co.edu.udistrital.model.repositories.ActionLogRepository;
import co.edu.udistrital.model.repositories.KitRepository;


public class UpdateKitUseCase {

	
	private final KitFactory kitFactory = new KitFactory();

	
	private final UnitStatusFactory unitStatusFactory = new UnitStatusFactory();

	
	private final KitRepository kitRepository;
	private final ActionLogRepository logRepo;

	
	public UpdateKitUseCase(KitRepository kitRepository, ActionLogRepository logRepo) {
		this.kitRepository = kitRepository;
		this.logRepo = logRepo;
	}

	
	public ResponseDTO execute(String idDkit, String type, String status) {
		try {

			UUID id = UUID.fromString(idDkit);

			KitType kitType = kitFactory.generateKitType(type);
			UnitStatus requestedStatus = unitStatusFactory.generateUnitStatus(status);

			Kit actualKit = kitRepository.getKitByID(id);

			if (actualKit == null) {
				return new ResponseDTO(false,
						"El kit no se encontró en el sistema. Es posible que haya sido eliminado.");
			}

			logRepo.logAction(new ActionRecord("Actualización de Kit: " + actualKit.getId().toString().substring(0, 8),
					ActionType.UPDATE_KIT, actualKit.getId().toString(), actualKit.getType().name(),
					actualKit.getStatus().name()));

			boolean goesToMaintenance = (actualKit.getStatus() == UnitStatus.AVAILABLE
					&& requestedStatus == UnitStatus.MAINTENANCE);

			Kit updatedKit = new Kit(kitType, requestedStatus);
			updatedKit.setId(id);

			boolean isUpdated = kitRepository.updatekit(actualKit, updatedKit);

			if (isUpdated) {

				if (goesToMaintenance) {
					kitRepository.maintainKit(updatedKit);
					return new ResponseDTO(true, "Datos actualizados. El kit fue apilado en mantenimiento.");
				}

				return new ResponseDTO(true, "La información del kit fue actualizada exitosamente.");
			} else {
				return new ResponseDTO(false, "No se pudo actualizar la información del kit.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Datos inválidos: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error inesperado al actualizar los datos del kit.");
		}
	}
}
