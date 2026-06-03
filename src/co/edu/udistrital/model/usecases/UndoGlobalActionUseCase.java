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

/**
 * Caso de uso maestro (Ctrl+Z Global). Se encarga de extraer la última acción
 * operativa del ActionLogRepository y restaurar el estado previo de la entidad
 * utilizando el patrón Memento.
 *
 * @author Juan David Diaz Perez / Jimmy86gb
 */
public class UndoGlobalActionUseCase {

	private final ActionLogRepository logRepo;
	private final TechnicianRepository techRepo;
	private final ServiceUnitRepository unitRepo;
	private final KitRepository kitRepo;
	private final ClientRepository clientRepo;
	private final UndoReportResourcesUseCase undoReportUseCase;

	/**
	 * Inyección masiva de dependencias. Como este es el orquestador global de
	 * reversiones, necesita acceso de escritura a todos los repositorios del
	 * modelo.
	 */
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

	/**
	 * Ejecuta la reversión de la última acción registrada en el sistema.
	 * 
	 * @return DTO con el resultado de la restauración para notificar a la vista.
	 */
	public ResponseDTO execute() {

		// 1. Extraemos el recuerdo (Memento) de la cima de la Pila LIFO
		ActionRecord lastAction = logRepo.popLastAction();

		if (lastAction == null) {
			return new ResponseDTO(false, "El historial está vacío. No hay acciones recientes para deshacer.");
		}

		try {
			// 2. Preparamos los identificadores y los datos antiguos
			UUID targetId = (lastAction.getTargetId() != null) ? UUID.fromString(lastAction.getTargetId()) : null;
			String[] oldData = lastAction.getPreviousState();

			// 3. Enrutamos la restauración según el tipo de acción
			switch (lastAction.getType()) {

			case DISPATCH_REPORT:
				// El despacho ya tenía su propia lógica compleja de reversión. ¡La reciclamos!
				return undoReportUseCase.execute();

			case UPDATE_TECHNICIAN:
				Technician t = techRepo.getTechnicianByID(targetId);
				if (t == null) {
					return new ResponseDTO(false, "El técnico original ya no existe.");
				}

				// Orden del arreglo guardado: [0]Nombre, [1]Especialidad, [2]Zona, [3]Estado
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

				// Orden del arreglo guardado: [0]Tipo, [1]Estado, [2]Zona
				// Nota: Si el UpdateUnitUseCase había mandado un borrador a la pila de
				// confirmación,
				// esta reversión sobreescribe directamente la entidad en la lista principal.
				u.setStatus(UnitStatus.valueOf(oldData[1]));
				u.setZone(OperationZone.valueOf(oldData[2]));

				return new ResponseDTO(true,
						"Reversión exitosa: La unidad de servicio regresó a su zona y estado previos.");

			case UPDATE_KIT:
				Kit k = kitRepo.getKitByID(targetId);
				if (k == null) {
					return new ResponseDTO(false, "El kit original ya no existe.");
				}

				// Orden del arreglo guardado: [0]Tipo, [1]Estado
				UnitStatus previousKitStatus = UnitStatus.valueOf(oldData[1]);

				// Si el kit había sido mandado a mantenimiento, técnicamente habría que sacarlo
				// de la pila.
				// Para mantener la integridad, simplemente le devolvemos su estado natural.
				// (En un sistema hiper-estricto, se haría un método en KitRepository para
				// retirarlo de la pila LIFO).
				k.setStatus(previousKitStatus);

				return new ResponseDTO(true, "Reversión exitosa: El estado del Kit ha sido restaurado a "
						+ previousKitStatus.getDisplayName() + ".");

			case UPDATE_CLIENT:
				Client c = clientRepo.getClientByID(lastAction.getTargetId()); // Usamos String directo
				if (c == null) {
					return new ResponseDTO(false, "El cliente original ya no existe en el directorio.");
				}

				// Orden del arreglo guardado: [0]Nombre, [1]Tipo, [2]Contacto
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