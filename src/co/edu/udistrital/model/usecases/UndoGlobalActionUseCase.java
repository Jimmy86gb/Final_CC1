package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.ActionRecord;
import co.edu.udistrital.model.entities.Client;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.entities.ServiceUnit;
import co.edu.udistrital.model.entities.Technician;
import co.edu.udistrital.model.enums.ClientType;
import co.edu.udistrital.model.enums.OperationZone;
import co.edu.udistrital.model.enums.TechnicianSpecialty;
import co.edu.udistrital.model.enums.TechnicianStatus;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.ActionLogRepository;
import co.edu.udistrital.model.repositories.ClientRepository;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.repositories.ServiceUnitRepository;
import co.edu.udistrital.model.repositories.TechnicianRepository;


public class UndoGlobalActionUseCase {

	private final ActionLogRepository logRepo;
	private final TechnicianRepository techRepo;
	private final ServiceUnitRepository unitRepo;
	private final KitRepository kitRepo;
	private final ClientRepository clientRepo;
	private final UndoReportResourcesUseCase undoReportUseCase;

	
	public UndoGlobalActionUseCase(ActionLogRepository logRepo, TechnicianRepository techRepo,
			ServiceUnitRepository unitRepo, KitRepository kitRepo, ClientRepository clientRepo,
			UndoReportResourcesUseCase undoReportUseCase) {
		this.logRepo = logRepo;
		this.techRepo = techRepo;
		this.unitRepo = unitRepo;
		this.kitRepo = kitRepo;
		this.clientRepo = clientRepo;
		this.undoReportUseCase = undoReportUseCase;
	}

	
	public ResponseDTO execute() {

		
		ActionRecord lastAction = logRepo.popLastAction();

		if (lastAction == null) {
			return new ResponseDTO(false, "El historial está vacío. No hay acciones recientes para deshacer.");
		}

		try {
			
			UUID targetId = (lastAction.getTargetId() != null) ? UUID.fromString(lastAction.getTargetId()) : null;
			String[] oldData = lastAction.getPreviousState();

			
			switch (lastAction.getType()) {

			case DISPATCH_REPORT:
				
				return undoReportUseCase.execute();

			case UPDATE_TECHNICIAN:
				Technician t = techRepo.getTechnicianByID(targetId);
				if (t == null) {
					return new ResponseDTO(false, "El técnico original ya no existe.");
				}

				
				t.setName(oldData[0]);
				t.setSpecialty(TechnicianSpecialty.valueOf(oldData[1]));
				t.setZone(OperationZone.valueOf(oldData[2]));
				t.setStatus(TechnicianStatus.valueOf(oldData[3]));

				return new ResponseDTO(true,
						"Reversión exitosa: El técnico " + t.getName() + " recuperó su estado anterior.");

			case UPDATE_UNIT:
				ServiceUnit u = unitRepo.getServiceUnitByID(targetId);
				if (u == null) {
					return new ResponseDTO(false, "La unidad original ya no existe.");
				}

				
				
				
				
				u.setStatus(UnitStatus.valueOf(oldData[1]));
				u.setZone(OperationZone.valueOf(oldData[2]));

				return new ResponseDTO(true,
						"Reversión exitosa: La unidad de servicio regresó a su zona y estado previos.");

			case UPDATE_KIT:
				Kit k = kitRepo.getKitByID(targetId);
				if (k == null) {
					return new ResponseDTO(false, "El kit original ya no existe.");
				}

				
				UnitStatus previousKitStatus = UnitStatus.valueOf(oldData[1]);

				
				
				
				
				
				k.setStatus(previousKitStatus);

				return new ResponseDTO(true, "Reversión exitosa: El estado del Kit ha sido restaurado a "
						+ previousKitStatus.getDisplayName() + ".");

			case UPDATE_CLIENT:
				Client c = clientRepo.getClientByID(lastAction.getTargetId()); 
				if (c == null) {
					return new ResponseDTO(false, "El cliente original ya no existe en el directorio.");
				}

				
				c.setName(oldData[0]);
				c.setType(ClientType.valueOf(oldData[1]));
				c.setContactInfo(oldData[2]);

				return new ResponseDTO(true,
						"Reversión exitosa: Los datos del cliente " + c.getName() + " fueron restaurados.");

			default:
				return new ResponseDTO(false, "El sistema no reconoce el tipo de acción a revertir.");
			}

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false,
					"Fallo de consistencia: Los datos guardados en el historial están corruptos o desactualizados.");
		} catch (Exception e) {
			return new ResponseDTO(false, "Error crítico interno al ejecutar la reversión: " + e.getMessage());
		}
	}
}