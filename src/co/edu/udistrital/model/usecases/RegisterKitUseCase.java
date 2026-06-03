package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.KitFactory;
import co.edu.udistrital.model.enums.KitType;
import co.edu.udistrital.model.enums.UnitStatus;
import co.edu.udistrital.model.repositories.KitRepository;


public class RegisterKitUseCase {

	
	private final KitFactory kitFactory = new KitFactory();

	
	private final KitRepository kitRepository;

	
	public RegisterKitUseCase(KitRepository kitRepository) {
		super();
		this.kitRepository = kitRepository;
	}

	
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
