package co.edu.udistrital.model.usecases;

import co.edu.udistrital.model.dtos.KitDTO;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.repositories.KitRepository;
import co.edu.udistrital.model.structures.SimpleList;

public class GetAvailableKitsByTypeUseCase {
    private final KitRepository kitRepository;

    public GetAvailableKitsByTypeUseCase(KitRepository kitRepository) {
        this.kitRepository = kitRepository;
    }

    public SimpleList<KitDTO> execute(String problemType) {
        SimpleList<KitDTO> result = new SimpleList<>();
        // Mapeo seguro: Si el tipo no existe, retorna un "Kit General" en lugar de fallar
        String kitType = mapProblemToKitType(problemType);
        
        SimpleList.Iterator<Kit> it = kitRepository.getAllKits().iterador();
        while (it.hasNext()) {
            Kit k = it.Next();
            // Compara usando equalsIgnoreCase para evitar errores de formato
            if (k.getStatus().getDisplayName().equalsIgnoreCase("Disponible") && 
                k.getType().getDisplayName().equalsIgnoreCase(kitType)) {
            	result.add(new KitDTO(k.getId(), k.getType().getDisplayName(), k.getStatus().getDisplayName(), false));
            }
        }
        return result;
    }

    private String mapProblemToKitType(String problemType) {
        if (problemType == null) return "Kit General";
        
        switch (problemType) {
            case "Mecanico General": return "Kit General";
            case "Electrico Automotriz": return "Kit de Electricidad";
            case "Cerrajero de Vehiculos": return "Kit de Cerrajeria";
            case "Operador de Grua": return "Kit de Grua";
            case "Operario Montallantas": return "Kit de Montallantas";
            default: return "Kit General"; // Fallback de seguridad
        }
    }
}