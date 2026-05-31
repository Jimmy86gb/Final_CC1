package co.edu.udistrital.usecases;

import co.edu.udistrital.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.KitFactory;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;

/**
 * Clase que representa el caso de uso de registrar un nuevo kit en el sistema.
 *
 * @author Juan David Diaz Perez
 */
public class RegisterKitUseCase {

	/**
	 * Instancia del factory del tipo de kit
	 */
	private final KitFactory kitFactory = new KitFactory();

	/**
	 * Instancia del repositorio que maneja la memoria de los kits
	 */
	private final KitRepository kitRepository;

	/**
	 * Constructor que inyecta el repositorio de kits
	 * 
	 * @param kitRepository Repositorio de memoria de kits
	 */
	public RegisterKitUseCase(KitRepository kitRepository) {
		super();
		this.kitRepository = kitRepository;
	}

	/**
	 * Caso de uso que ejecuta el registro en bloque de kits
	 * 
	 * @param type     Tipo de kit a usar
	 * @param quantity Cantidad de kits a registrar
	 * @return La respuesta de la operacion al controller
	 */
	public ResponseDTO execute(String type, int quantity) {

		if (quantity <= 0) {
			return new ResponseDTO(false, "La cantidad debe ser mayor a cero.");
		}

		try {
			KitType kitType = kitFactory.generateKitType(type);
			UnitStatus initialStatus = UnitStatus.AVAILABLE;
			int successCount = 0;

			for (int i = 0; i < quantity; i++) {
				Kit newKit = new Kit(kitType, initialStatus);

				kitRepository.saveKit(newKit);
				successCount++;
			}

			return new ResponseDTO(true,
					"Se registraron exitosamente " + successCount + " kits de tipo " + kitType.getDisplayName() + ".");

		} catch (IllegalArgumentException e) {
			return new ResponseDTO(false, "Tipo de kit inválido: " + e.getMessage());
		} catch (Exception e) {
			return new ResponseDTO(false, "Ocurrió un error al registrar el lote de kits.");
		}
	}

}
