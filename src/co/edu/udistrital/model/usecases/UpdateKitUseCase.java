package co.edu.udistrital.model.usecases;

import java.util.UUID;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.KitFactory;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.enums.UnitStatusFactory;
import co.edu.udistrital.model.repositories.KitRepository;

/**
 * Clase de caso de uso que se encarga de actualizar la informacion de los kits
 * recibidas desde la vista
 *
 * @author Juan David Diaz Perez
 */
public class UpdateKitUseCase {

	/**
	 * Instancia del factory del tipo de kit
	 */
	private final KitFactory kitFactory = new KitFactory();

	/**
	 * Instancia privada del factory del status del kit
	 */
	private final UnitStatusFactory unitStatusFactory = new UnitStatusFactory();

	/**
	 * Instancia del repositorio que maneja la memoria de los kits
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta el repositorio de kits
	 * 
	 * @param kitRepository Repositorio de memoria de kits
	 */
	public UpdateKitUseCase(KitRepository kitRepository) {
		this.kitRepository = kitRepository;
	}

	/**
	 * Metodo del caso de uso que actualiza la informacion de los kits
	 * 
	 * @param id     ID de ambos kits
	 * @param type   Tipo de ambos kits
	 * @param status Status a actualizar
	 * @return DTO de respuesta frente a la accion
	 */
	public ResponseDTO execute(UUID id, String type, String status) {
		try {
			KitType kitType = kitFactory.generateKitType(type);
			UnitStatus requestedStatus = unitStatusFactory.generateUnitStatus(status);

			Kit actualKit = kitRepository.getKitByID(id);

			if (actualKit == null) {
				return new ResponseDTO(false,
						"El kit no se encontró en el sistema. Es posible que haya sido eliminado.");
			}

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
